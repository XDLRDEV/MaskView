package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"Starry/util"
	"bufio"
	"bytes"
	"crypto/sha256"
	"crypto/tls"
	"encoding/base64"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/disintegration/imaging"
	"github.com/labstack/echo/v4"
	"io"
	"io/ioutil"
	mathRand "math/rand"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"
)

/**
安全交易部分
*/

// 安全交易---上架图片
func SellForSecureTra(c echo.Context) error {
	// 1. 登陆者uid
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	userUid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(userUid, 10, 64)
	imgPrice := c.FormValue("imgPrice")
	imgPriceUint, _ := strconv.ParseUint(imgPrice, 10, 64)
	imgTopic := c.FormValue("imgTopic")
	// 2. 接收上传的图片
	file, err := c.FormFile("file")
	if err != nil {
		return err
	}
	if file != nil {
		fileName := file.Filename
		originPath := "img/" + userUid + "/" + fileName
		// 3. 图片已上架, 删除对应的txt及数据
		if gorm_mysql.HasSellImgName(originPath) {
			sellInfo := gorm_mysql.GetOneSellInfo(uidUint, originPath)
			txtPath := sellInfo.TxtPath
			if txtPath != "" {
				err := os.Remove(txtPath)
				if err != nil {
					return c.JSON(http.StatusOK, models.SimpleResult{
						Result: false,
						Note:   "原txt删除失败",
						Data:   "",
					})
				} else {
					fmt.Println("原txt删除成功")
				}
			}
			_ = gorm_mysql.DeleteSell(uidUint, originPath)
		}
		// 4. 保存原图至img文件夹
		if !saveOrigin(file, userUid) {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "保存原图失败",
				Data:   "",
			})
		}
		// 5. 生成缩略图并存至thumbnailImg文件夹
		if !saveThumbnailImg(originPath, fileName, userUid) {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "保存缩略图失败",
				Data:   "",
			})
		}
		// 6. 生成txt文件名, 随机30位
		randomTxtName := randomLength() + ".txt"
		// 7. 对原图base64加密，生成txt文件保存,并对txt取哈希
		filePath := "img/" + userUid + "/" + fileName
		result, txtHash := encode(userUid, filePath, randomTxtName)
		// 8. 加密完成, 上链操作
		if result {
			txtPath := "base64File/" + userUid + "/" + randomTxtName
			if httpSell(randomTxtName, txtHash) {
				// 9. 上链成功后入库
				thumbnailPath := "thumbnailImg/" + userUid + "/" + fileName
				sellMsg := models.SellMsg{
					Uid:           uidUint,
					ImgName:       originPath,
					ThumbnailPath: thumbnailPath,
					ImageTopic:    imgTopic,
					ImgPrice:      imgPriceUint,
					FiscoKey:      randomTxtName,
					TxtPath:       txtPath,
					CreatedAt:     time.Time{},
				}
				// 入库
				if err := gorm_mysql.CreateSellInfo(&sellMsg); err != nil {
					return err
				} else {
					return c.JSON(http.StatusOK, models.SimpleResult{
						Result: true,
						Note:   "上架成功",
						Data:   "",
					})
				}
			} else {
				return c.JSON(http.StatusOK, models.SimpleResult{
					Result: false,
					Note:   "上链失败---java服务器通信失败",
					Data:   "",
				})
			}
		}

	}
	return c.JSON(http.StatusOK, models.SimpleResult{
		Result: false,
		Note:   "file未接收到",
		Data:   "",
	})
}

//------------------------------------ start -----------------------------------------//
// 生成缩略图
// <200kb, 直接复制原图
func saveThumbnailImg(originPath, imgName, uid string) bool {
	// 检验缩略图路径文件夹存在
	thumbnailDir := "thumbnailImg/" + uid
	exists, err := util.PathExists(thumbnailDir)
	if !exists {
		err = os.MkdirAll(thumbnailDir, os.ModePerm)
		if err != nil {
			return false
		}
	}
	thumbnailPath := thumbnailDir + "/" + imgName
	fis, _ := os.Stat(originPath)
	if fis.Size() > 200000 { // 200000B 即：200kB
		// >200kb, 缩略后保存
		imgData, _ := ioutil.ReadFile(originPath)
		fmt.Println("原图路径：" + originPath)
		buf := bytes.NewBuffer(imgData)
		image, err := imaging.Decode(buf)
		if err != nil {
			fmt.Println(err)
			return false
		}
		//生成缩略图，尺寸宽x,高传0表示等比例放缩
		image = imaging.Resize(image, 600, 0, imaging.Lanczos)
		err = imaging.Save(image, thumbnailPath)
		fmt.Println("保存缩略路径：" + thumbnailPath)
		if err != nil {
			fmt.Println(err)
			return false
		}
		return true
	} else {
		// <200kb,直接复制原图保存
		readFile, err := os.Open(originPath)
		if err != nil {
			return false
		}
		writeFile, err := os.OpenFile(thumbnailPath, os.O_WRONLY|os.O_CREATE, os.ModePerm)
		if err != nil {
			return false
		}
		defer readFile.Close()
		defer writeFile.Close()
		_, _ = io.Copy(writeFile, readFile)
		return true
	}
}

// 随机生成30位
func randomLength() string {
	kinds := [][]int{{10, 48}, {26, 97}, {26, 65}}
	result := make([]byte, 30) //30位
	for i := 0; i < 30; i++ {
		randKind := mathRand.Intn(3)
		scope, base := kinds[randKind][0], kinds[randKind][1]
		result[i] = uint8(base + mathRand.Intn(scope))
	}
	return string(result)
}

// base64加密生成txt文件,并保存,并取文件哈希返回
func encode(uid string, imgPath string, txtName string) (bool, string) {
	// 判断文件夹是否存在
	base64Dir := "base64File/" + uid
	exists, err := util.PathExists(base64Dir)
	if !exists {
		err = os.MkdirAll(base64Dir, os.ModePerm)
		if err != nil {
			return false, ""
		}
	}
	outPath := base64Dir + "/" + txtName
	inFile, err := os.Open(imgPath)
	if err != nil {
		fmt.Println("未找到原图，请确定路径是否正确。")
		return false, ""
	}
	myBytes := make([]byte, 1024*99999)
	n, err := inFile.Read(myBytes)
	if err != nil {
		fmt.Println("代码读入失败，请检查权限。")
	}
	get := base64.StdEncoding.EncodeToString(myBytes[:n])
	file, err := os.OpenFile(outPath, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Println("写出失败。")
		fmt.Println(err)
		return false, ""
	}
	buf := bufio.NewWriter(file)
	_, _ = buf.WriteString(get)
	_ = buf.Flush()
	defer file.Close()

	// 对txt取哈希-sha256
	txtFile, err := os.Open(outPath)
	if txtFile != nil {
		if err == nil {
			hb := sha256.New()
			_, err := io.Copy(hb, txtFile)
			if err == nil {
				hash := hb.Sum(nil)
				hashValue := hex.EncodeToString(hash)
				defer txtFile.Close()
				return true, hashValue
			} else {
				defer txtFile.Close()
				return false, "something wrong when use sha256 interface..."
			}
		} else {
			defer txtFile.Close()
			return false, "failed to open"
		}
	}
	return false, "last one"
}

// https请求java服务器上链
func httpSell(txtName, txtHash string) bool {
	url := JavaServer() + "/fisco-bcos/sell"
	method := "POST"

	payload := strings.NewReader("txtName=" + txtName + "&txtHash=" + txtHash)
	// 信任所有证书
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	client := &http.Client{
		Transport: tr,
	}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return false
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")
	res, err := client.Do(req)

	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)

	fmt.Println(string(body))
	//解析返回的JSON数据
	var message httpResponse
	err = json.Unmarshal(body, &message)
	if err != nil {
		return false
	}
	if message.Result {
		return true
	} else {
		return false
	}
}

type httpResponse struct {
	Result bool   `json:"result"`
	Note   string `json:"note"`
}

//------------------------------------ end -----------------------------------------//

//------------------------------------ start -----------------------------------------//
// 安全交易---购买---获取该图片的信息(txt文件和imgName)
func PurchaseForSecureTra(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	purchaseUid := claims["uid"].(string)
	purchaseUidUint, _ := strconv.ParseUint(purchaseUid, 10, 64)
	imgPath := c.FormValue("imgPath")
	imgInfo, result := gorm_mysql.GetOneSellImgInfoByImgPath(imgPath)
	if result {
		if imgInfo.Uid == purchaseUidUint {
			return c.JSON(http.StatusOK, models.PurchaseForSecureTraResult{
				Result: false,
				Note:   "他本来是就是你的",
			})
		} else if imgInfo.TxtPath != "" {
			return c.JSON(http.StatusOK, models.PurchaseForSecureTraResult{
				Result:      true,
				Note:        "获取信息成功",
				PurchaseUid: purchaseUidUint,
				Data:        *imgInfo,
			})
		} else {
			return c.JSON(http.StatusOK, models.PurchaseForSecureTraResult{
				Result: false,
				Note:   "该图不符合安全交易条件",
			})
		}
	} else {
		return c.JSON(http.StatusOK, models.PurchaseForSecureTraResult{
			Result: false,
			Note:   "无该图片信息",
		})
	}
}

//------------------------------------ end -----------------------------------------//

// 安全交易---链上查询用户积分
func GetMyPoints(uid uint64) int {
	uidString := strconv.Itoa(int(uid))
	url := JavaServer() + "/fisco-bcos/getMyPoints"
	method := "POST"
	payload := strings.NewReader("uid=" + uidString)
	// 信任所有证书
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	client := &http.Client{
		Transport: tr,
	}
	req, err := http.NewRequest(method, url, payload)
	if err != nil {
		fmt.Println(err)
		return -1
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")
	res, err := client.Do(req)
	if err != nil {
		return -1
	}
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)
	//解析返回的JSON数据
	var message httpGetMyPoints
	err = json.Unmarshal(body, &message)
	if err != nil {
		return -1
	}
	if message.Result {
		return message.Data
	} else {
		return -1
	}
}

type httpGetMyPoints struct {
	Result bool   `json:"result"`
	Note   string `json:"note"`
	Data   int    `json:"data"`
}

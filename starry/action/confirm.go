package action

import (
	"Starry/api/debug"
	"Starry/gorm_mysql"
	"Starry/models"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"io/ioutil"
	"net/http"
	"strconv"
	"strings"
)

func httpJavaConfirm(phoneNumber uint64, imgName string, key string) bool {
	url := JavaServer() + "/fisco-bcos/confirm"
	method := "POST"
	phoneNumberS := strconv.Itoa(int(phoneNumber))
	payload := strings.NewReader("phoneNumber=" + phoneNumberS +
		"&imgName=" + imgName + "&key=" + key)
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

//确权图片，需要登陆
func ConfirmImg(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	userInfo := gorm_mysql.GetUserSecret(&uidUint)

	imgName := c.FormValue("imgName")
	imgKey := c.FormValue("imgKey")

	confirmMsg := models.ImageKey{
		Uid:         uidUint,
		PhoneNumber: userInfo.PhoneNumber,
		ImageName:   imgName,
		ImgKey:      imgKey,
	}

	if httpJavaConfirm(userInfo.PhoneNumber, imgName, imgKey) {
		// 上链成功, 入库
		if err := gorm_mysql.CreateImgKey(&confirmMsg); err != nil {
			debug.PanicErr(err)
		}

		return c.JSON(http.StatusOK,
			models.ConfirmImgResult{
				Result: true,
				Note:   "确权信息备份成功",
			})
	} else {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "与区块链节点通信失败",
				Data:   "",
			})
	}

	// 之前的确权版本
	/*if grpc.ConfirmClient(&confirmMsg) {
		if err := gorm_mysql.CreateImgKey(&confirmMsg); err != nil {
			debug.PanicErr(err)
		}

		return c.JSON(http.StatusOK,
			models.ConfirmImgResult{
				Result: true,
				Note:   "确权信息备份成功",
			})
	} else {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "与区块链节点通信失败",
				Data:   "",
			})
	}*/

}

package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"Starry/util"
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

//购买功能，需要登陆----------------已添加：购买后清除购物车购买项
func PurchaseImg(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//买家uid
	purchaserUidString := claims["uid"].(string)
	purchaserUid, _ := strconv.ParseUint(purchaserUidString, 10, 64)
	//买家信息
	//purchaserInfo := gorm_mysql.GetUserSecret(&purchaserUid)
	//获取卖家用户名
	sellerName := c.FormValue("sellerName")
	//获取交易图片名称
	imgName := c.FormValue("imgName")
	//获取价钱
	price, _ := strconv.ParseUint(c.FormValue("imgPrice"), 10, 64)
	//获取哈希序列
	dataHash := c.FormValue("dataHash")
	//卖家信息
	sellerInfo := gorm_mysql.GetUserInfoByUserName(sellerName)
	//卖家uid
	sellerUid := sellerInfo.Uid

	//strconv.Itoa(int(a)) : 将uint64->int->string
	//判断图片路径是否存在
	imgPath := "thumbnailImg/" + strconv.Itoa(int(sellerUid)) + "/" + imgName
	res, _ := util.PathExists(imgPath)
	if !res {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "图片不存在",
			Data:   "",
		})
	}

	//判断买家积分是否充足
	purchaserPoints := GetMyPoints(purchaserUid)
	if purchaserPoints == -1 {
		return c.JSON(http.StatusOK,
			models.Token{
				Result: false,
				Token:  "链上获取积分失败",
			},
		)
	}
	if uint64(purchaserPoints) < price {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "您的积分不足",
			Data:   ""})
	}
	/*purchaserPoints := gorm_mysql.GetUserSecret(&purchaserUid).Points
	if purchaserPoints < price {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "您的积分不足",
			Data:   ""})
	}*/

	//积分充足,实现购买,数据入库
	m := models.TradeHistory{
		PurchaserId: purchaserUid,
		SellerId:    sellerUid,
		ImageName:   imgPath,
		TradePrice:  price,
		DataHash:    dataHash,
	}

	if normalTrade(purchaserUid, sellerUid, price) {
		// 上链成功, 入库
		if err := gorm_mysql.CreateTradeHistory(&m); err != nil {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "交易失败-添加交易记录数据失败",
				Data:   ""})
		}
		//若是在购物车页面选择购买,购买完成后删除购物车中该信息
		gorm_mysql.DeleteOneShoppingCartInfoAfterPurchase(purchaserUid, imgPath)
		return c.JSON(http.StatusOK, models.PurchaserImgResult{
			Result: true,
			Note:   "购买成功",
		})
	} else {
		return c.JSON(http.StatusOK, models.PurchaserImgResult{
			Result: false,
			Note:   "普通交易失败---区块链节点通信失败",
		})
	}

	/*if result, err := grpc.TradeClient(&m); err != nil {
		panic(err)
		return err
	} else if result.Result {
		// 上链成功
		if err := gorm_mysql.CreateTradeHistory(&m); err != nil {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "交易失败-添加交易记录数据失败",
				Data:   ""})
		} else {

			// 通知Java端, 链上扣除/加相应积分
			if normalTrade(purchaserUid, sellerUid, price) {
				//若是在购物车页面选择购买,购买完成后删除购物车中该信息
				gorm_mysql.DeleteOneShoppingCartInfoAfterPurchase(purchaserUid, imgPath)
				return c.JSON(http.StatusOK, models.PurchaserImgResult{
					Result: true,
					Note:   "购买成功",
				})
			} else {
				return c.JSON(http.StatusOK, models.PurchaserImgResult{
					Result: false,
					Note:   "普通交易失败",
				})
			}
		}
	} else {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "与区块链节点通信失败",
			Data:   ""})
	}*/

}

// 通知智能合约扣除/加积分
func normalTrade(purchaserUid uint64, sellerUid uint64, imgPrice uint64) bool {
	url := JavaServer() + "/fisco-bcos/normalTrade"
	method := "POST"
	purchaserUidString := strconv.FormatUint(purchaserUid, 10)
	sellerUidString := strconv.FormatUint(sellerUid, 10)
	imgPriceString := strconv.FormatUint(imgPrice, 10)
	payload := strings.NewReader("purchaserUid=" +
		purchaserUidString +
		"&sellerUid=" + sellerUidString +
		"&imgPrice=" + imgPriceString)
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
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")

	res, err := client.Do(req)
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)
	//解析返回的JSON数据
	var message httpNormalTrade
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

type httpNormalTrade struct {
	Result bool   `json:"result"`
	Note   string `json:"note"`
	Data   int    `json:"data"`
}

//购买时判断照片是否是自己的
func IsMyImage(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//买家uid
	purchaserUidString := claims["uid"].(string)
	purchaserUid, _ := strconv.ParseUint(purchaserUidString, 10, 64)
	//买家信息
	purchaserInfo := gorm_mysql.GetUserSecret(&purchaserUid)
	//获取卖家用户名
	sellerName := c.FormValue("sellerName")

	if sellerName == purchaserInfo.UserName {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "他本来就是你的",
				Data:   "",
			})
	} else {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: true,
				Note:   "可以购买",
				Data:   "",
			})
	}
}

package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

//我的购买记录
func GetMyPurchaseRecords(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//登录的uid
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)

	purchaseRecords, isExist := gorm_mysql.GetPurchaseRecords(uidUint)

	if isExist == false {
		return c.JSON(http.StatusOK,
			models.MyPurchaseRecordsResult{
				Result: false,
				Note:   "你还没有购买",
				Data:   nil,
			})
	} else {
		return c.JSON(http.StatusOK,
			models.MyPurchaseRecordsResult{
				Result: true,
				Note:   "我的购买记录",
				Data:   purchaseRecords,
			})
	}
}

//我的出售记录
func GetMySaleRecords(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//登录的uid
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)

	saleRecords, isExist := gorm_mysql.GetSaleRecords(uidUint)

	if isExist == false {
		return c.JSON(http.StatusOK,
			models.MySaleRecordsResult{
				Result: false,
				Note:   "无出售记录",
				Data:   nil,
			})
	} else {
		return c.JSON(http.StatusOK,
			models.MySaleRecordsResult{
				Result: true,
				Note:   "我的出售记录",
				Data:   saleRecords,
			})
	}
}

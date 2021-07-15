package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

// 查看某用户的基本信息及所有上架照片(未登录)
func GetUserInfoAndSellListNotLogin(c echo.Context) error {
	userName := c.FormValue("userName")
	userInfo := gorm_mysql.GetUserInfoByUserName(userName)
	if userInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "用户名有误---无此用户信息",
				Data:   "",
			})
	}
	// 获取用户基本信息
	//info := gorm_mysql.GetUserInfoAndFansNotLogin(userInfo.Uid)
	// TODO：川大注入
	uidString := strconv.Itoa(int(userInfo.Uid))
	info := gorm_mysql.GetUserInfoAndFansNotLogin(uidString)

	// 获取该用户上架图片集合
	sellImgPathList := gorm_mysql.GetSellImgNameByUid(userInfo.Uid)
	return c.JSON(http.StatusOK,
		models.GetUserInfoAndSellListResult{
			Result:             true,
			Note:               "成功",
			Uid:                info.Uid,
			PhoneNumber:        info.PhoneNumber,
			UserName:           info.UserName,
			HeadViewPath:       info.HeadViewPath,
			BackgroundWallPath: info.BackgroundWallPath,
			FocusCount:         info.FocusCount,
			FansCount:          info.FansCount,
			SellImgPathList:    sellImgPathList,
			SellImgPriceList:   nil,
		})
}

// 查看某用户的基本信息及所有上架照片,以及登陆者与该用户的关注关系(登录后)
func GetUserInfoAndSellListAfterLogin(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	myUidString := claims["uid"].(string)
	myUid, _ := strconv.ParseUint(myUidString, 10, 64)
	// 查看人信息的昵称
	userName := c.FormValue("userName")
	userInfo := gorm_mysql.GetUserInfoByUserName(userName)
	if userInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "用户名有误---无此用户信息",
				Data:   "",
			})
	}
	if myUid == userInfo.Uid {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "自己的信息请点击我的查看",
				Data:   "",
			})
	}
	// 获取用户基本信息
	info := gorm_mysql.GetUserInfoAndFansAfterLogin(myUid, userInfo.Uid)
	// 获取该用户上架图片集合
	sellImgPathList := gorm_mysql.GetSellImgNameByUid(userInfo.Uid)
	sellImgPriceList := gorm_mysql.GetSellImgPriceByUid(userInfo.Uid)

	return c.JSON(http.StatusOK,
		models.GetUserInfoAndSellListResult{
			Result:             true,
			Note:               "成功",
			Uid:                info.Uid,
			PhoneNumber:        info.PhoneNumber,
			UserName:           info.UserName,
			HeadViewPath:       info.HeadViewPath,
			BackgroundWallPath: info.BackgroundWallPath,
			FocusCount:         info.FocusCount,
			FansCount:          info.FansCount,
			Relation:           info.Relation,
			SellImgPathList:    sellImgPathList,
			SellImgPriceList:   sellImgPriceList,
		})
}

package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"crypto/rand"
	"crypto/sha256"
	"encoding/hex"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"golang.org/x/crypto/pbkdf2"
	"net/http"
	"os"
	"strconv"
	"strings"
)

// 查看个人信息，需要登陆
func GetMyInfo(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	userInfo := gorm_mysql.GetMyInfo(&uidUint)
	//userInfo := gorm_mysql.GetMyInfo(&uidString)
	points := GetMyPoints(uidUint)
	if points == -1 {
		return c.JSON(http.StatusOK,
			models.Token{
				Result: false,
				Token:  "链上获取积分失败",
			},
		)
	}
	return c.JSON(http.StatusOK,
		models.MyInfoResult{
			Result:         true,
			UserName:       userInfo.UserName,
			UserPhone:      userInfo.UserPhone,
			Points:         uint64(points), //Points:         userInfo.Points,
			HeadView:       userInfo.HeadViewPath,
			BackgroundWall: userInfo.BackgroundWallPath,
			Sex:            userInfo.Sex,
			Birth:          userInfo.Birth,
			FocusCount:     userInfo.FocusCount,
			FansCount:      userInfo.FansCount,
		})
}

//设置,更改昵称
func UpdateMyNickName(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	newNickName := c.FormValue("newNickName")

	//更新用户名
	if gorm_mysql.UpdateUserName(uidUint, newNickName) {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: true,
				Note:   "修改成功",
				Data:   "",
			})
	} else {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "修改失败---重名",
				Data:   "",
			})
	}

}

//设置,更改性别
func UpdateMySex(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	mySex := c.FormValue("sex")

	if err := gorm_mysql.UpdateUserSex(uidUint, mySex); err != nil {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "更新性别失败",
				Data:   "",
			})
	}
	return c.JSON(http.StatusOK,
		models.SimpleResult{
			Result: true,
			Note:   "更新性别成功",
			Data:   "",
		})
}

//设置,更改出生日期
func UpdateMyBirth(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	myBirth := c.FormValue("birth")

	if err := gorm_mysql.UpdateUserBirth(uidUint, myBirth); err != nil {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "更新出生日期失败",
				Data:   "",
			})
	}
	return c.JSON(http.StatusOK,
		models.SimpleResult{
			Result: true,
			Note:   "更新出生日期成功",
			Data:   "",
		})

}

//设置,更改头像---每更换头像,删除之前的头像图片
func UpdateMyHeadView(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	myHeadViewName := c.FormValue("headViewName")
	userInfo := gorm_mysql.GetUserSecret(&uidUint)
	thumbnailImgPath := userInfo.HeadView
	//拼接thumbnailImg/x/xx.jpg路径
	newPath := "thumbnailImg/" + uidString + "/" + myHeadViewName
	if err := gorm_mysql.UpdateUserHeadView(uidUint, newPath); err != nil {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "更新头像失败",
				Data:   "",
			})
	}
	// 数据库更新成功， 删除原图片及缩略图
	if len(thumbnailImgPath) > 12 {
		index := strings.Index(thumbnailImgPath, "/")
		originImgPath := "img" + thumbnailImgPath[index:]
		_ = os.Remove(originImgPath)
		_ = os.Remove(thumbnailImgPath)
	}
	return c.JSON(http.StatusOK,
		models.SimpleResult{
			Result: true,
			Note:   "更新头像成功",
			Data:   "",
		})
}

// 设置,更新背景墙
func UpdateMyBackGroundWall(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	backgroundWallName := c.FormValue("imgName")
	userInfo := gorm_mysql.GetUserSecret(&uidUint)
	thumbnailImgPath := userInfo.BackgroundWall
	// 拼接缩略图路径
	newPath := "thumbnailImg/" + uidString + "/" + backgroundWallName
	if err := gorm_mysql.UpdateUserBackgroundWall(uidUint, newPath); err != nil {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "更新背景墙失败",
				Data:   "",
			})
	}
	// 数据库更新成功， 删除缩略图和原图
	if len(thumbnailImgPath) > 12 {
		index := strings.Index(thumbnailImgPath, "/")
		originImgPath := "img" + thumbnailImgPath[index:]
		_ = os.Remove(originImgPath)
		_ = os.Remove(thumbnailImgPath)
	}
	return c.JSON(http.StatusOK,
		models.SimpleResult{
			Result: true,
			Note:   "更新背景墙成功",
			Data:   "",
		})
}

//更改密码,需要登录
func UpdatePwd(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	newPassword := c.FormValue("newPassword")

	salt := make([]byte, 32)
	_, err := rand.Read(salt)
	if err != nil {
		return err
	}

	key := pbkdf2.Key([]byte(newPassword), salt, 1323, 32, sha256.New)
	update := models.User{
		Uid:  uidUint,
		Salt: hex.EncodeToString(salt),
		Key:  hex.EncodeToString(key),
	}
	err = gorm_mysql.UpdateUserInfo(update)
	if err != nil {
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "密码更新失败，请稍后重试"})
	}
	return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "密码更新成功"})
}

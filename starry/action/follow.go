package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

// 关注某人
func FocusUser(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	// 关注人的昵称
	focusUserName := c.FormValue("userName")
	focusInfo := gorm_mysql.GetUserInfoByUserName(focusUserName)
	if uidUint == focusInfo.Uid {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "不能关注自己",
			})
	}
	if focusInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "用户不存在",
			})
	}
	if gorm_mysql.IsHasFollower(uidUint, focusInfo.Uid) {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "已经关注了!",
			})
	}
	m1 := models.Follower{
		UserId:   uidUint,
		FocusUid: focusInfo.Uid,
	}
	m2 := models.Fans{
		UserId:  focusInfo.Uid,
		FansUid: uidUint,
	}
	if err := gorm_mysql.CreateFollowerAndFans(&m1, &m2); err != nil {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "关注失败",
			})
	}
	return c.JSON(http.StatusOK,
		models.FocusUserResult{
			Result: true,
			Note:   "关注成功",
		})
}

// 取消关注
func CancelFocus(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	// 取消关注人的昵称
	userName := c.FormValue("userName")
	cancelUserInfo := gorm_mysql.GetUserInfoByUserName(userName)
	if gorm_mysql.CancelFocus(uidUint, cancelUserInfo.Uid) {
		return c.JSON(http.StatusOK,
			models.CancelFocusResult{
				Result: true,
				Note:   "取关成功",
			})
	}
	return c.JSON(http.StatusOK,
		models.CancelFocusResult{
			Result: false,
			Note:   "请先关注",
		})
}

// 获取用户关注的所有人的信息,及登陆者与每个用户的关注关系(登录状态下)
func GetUserAllFocusInfo(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	myUid, _ := strconv.ParseUint(uid, 10, 64)
	herUserName := c.FormValue("userName")
	herInfo := gorm_mysql.GetUserInfoByUserName(herUserName)
	if herInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "用户不存在",
			})
	}
	finalInfo := gorm_mysql.GetUserAllFocusInfo(myUid, herInfo.Uid)
	return c.JSON(http.StatusOK,
		models.GetUserAllFocusInfoResult{
			Result: true,
			Note:   "获取成功",
			Data:   *finalInfo,
		})
}

// 未登录获取关注人的信息
func GetUserAllFocusInfoNotLogin(c echo.Context) error {
	herUserName := c.FormValue("userName")
	herInfo := gorm_mysql.GetUserInfoByUserName(herUserName)
	if herInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "用户不存在",
			})
	}
	finalInfo := gorm_mysql.GetUserAllFocusInfo(0, herInfo.Uid)
	return c.JSON(http.StatusOK,
		models.GetUserAllFocusInfoResult{
			Result: true,
			Note:   "获取成功",
			Data:   *finalInfo,
		})
}

// 获取用户所有粉丝的信息,及登陆者与每个用户的关注关系
func GetUserAllFansInfo(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	myUid, _ := strconv.ParseUint(uid, 10, 64)
	herUserName := c.FormValue("userName")
	herInfo := gorm_mysql.GetUserInfoByUserName(herUserName)
	if herInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "用户不存在",
			})
	}
	finalInfo := gorm_mysql.GetUserAllFansInfo(myUid, herInfo.Uid)
	return c.JSON(http.StatusOK,
		models.GetUserAllFansInfoResult{
			Result: true,
			Note:   "获取成功",
			Data:   *finalInfo,
		})
}

// 未登录获取粉丝的信息
func GetUserAllFansInfoNotLogin(c echo.Context) error {
	herUserName := c.FormValue("userName")
	herInfo := gorm_mysql.GetUserInfoByUserName(herUserName)
	if herInfo.Uid == 0 {
		return c.JSON(http.StatusOK,
			models.FocusUserResult{
				Result: false,
				Note:   "用户不存在",
			})
	}
	finalInfo := gorm_mysql.GetUserAllFansInfo(0, herInfo.Uid)
	return c.JSON(http.StatusOK,
		models.GetUserAllFansInfoResult{
			Result: true,
			Note:   "获取成功",
			Data:   *finalInfo,
		})
}

package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"Starry/util"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"io/ioutil"
	"net/http"
	"regexp"
	"strconv"
)

func ImgList(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uid = checkId(uid)

	fmt.Printf("current uid is %s\n", uid)
	path := "img/" + uid
	res, _ := util.PathExists(path)

	var fileList []interface{}
	if res {
		files, _ := ioutil.ReadDir(path)
		reg := regexp.MustCompile(`^([\d]{6})-.*$`)
		for _, f := range files {
			name := f.Name()
			isMatch := reg.Match([]byte(name))
			if isMatch {
				fileList = append(fileList, path+"/"+name)
			}
		}
		return c.JSON(http.StatusOK, models.FileResult{
			Result: true,
			Note:   "获取图片成功",
			Data:   fileList,
		})
	}
	return c.JSON(http.StatusOK, models.FileResult{
		Result: false,
		Note:   "获取图片失败",
		Data:   nil,
	})
}

/*func GetPoints(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	id, _ := strconv.ParseUint(uid, 10, 64)
	dbuser := gorm_mysql.GetUserSecret(&id)

	return c.JSON(http.StatusOK, models.SimpleResult{
		Result: true,
		Note:   "获取积分成功",
		Data:   strconv.Itoa(int(dbuser.Points)),
	})
}*/

func GetPoints(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	id, _ := strconv.ParseUint(uid, 10, 64)
	points := GetMyPoints(id)
	if points == -1 {
		return c.JSON(http.StatusOK,
			models.Token{
				Result: false,
				Token:  "链上获取积分失败",
			},
		)
	}

	return c.JSON(http.StatusOK, models.SimpleResult{
		Result: true,
		Note:   "获取积分成功",
		Data:    strconv.Itoa(points),
	})
}

func GetTradeHistory(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	iuid, _ := strconv.ParseUint(uid, 10, 64)
	tradeList := gorm_mysql.GetTradeHistory(&iuid)
	return c.JSON(http.StatusOK, models.TradeListResult{
		Result: true,
		Note:   "获取交易记录成功（我卖出去的记录）",
		Data:   *tradeList,
	})
}

func GetImgKeyList(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUInt, _ := strconv.ParseUint(uid, 10, 64)
	keyList := gorm_mysql.GetImageKeyList(&uidUInt)
	return c.JSON(http.StatusOK, models.ImgKeyListResult{
		Result: true,
		Note:   "获取imgKey列表成功",
		Data:   *keyList,
	})
}

func GetUid(c echo.Context) error {
	phoneNumber := c.FormValue("phoneNumber")
	phone,_ := strconv.ParseUint(phoneNumber, 10, 64)
	if res:= util.CheckNumber(phone); !res {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "手机号错误",
			Data:   "",
		})
	}
	uidUint := gorm_mysql.GetUserIdByPhoneNumber(&phone)
	if uidUint == 0 {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "用户不存在",
			Data:   "",
		})
	}
	uid := checkId(strconv.Itoa(int(uidUint)))
	return c.JSON(http.StatusOK, models.SimpleResult{
		Result: true,
		Note:   "获取id成功",
		Data:   uid,
	})
}

package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

func GetUserList(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uid, _ := strconv.ParseUint(uidString, 10, 64)
	if list, number := gorm_mysql.GetUserList(&uid); number == 0 {
		return c.JSON(http.StatusOK, models.ResultData{Result: false, Note: "无查询结果"})
	} else {
		return c.JSON(http.StatusOK, models.ResultData{Result: true, Note: "查询成功", Data: list})
	}
}

func GetByDataHash(c echo.Context) error {
	m := new(models.RequestData)
	if err := c.Bind(m); err != nil {
		return err
	}
	switch m.SearchCode {
	case 1:
		if list, number := gorm_mysql.GetData(&m.DataHash); number == 0 {
			return c.JSON(http.StatusOK, models.ResultData{Result: false, Note: "无查询结果"})
		} else {
			return c.JSON(http.StatusOK, models.ResultData{Result: true, Note: "查询成功", Data: list})
		}
	case 2:
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "我觉得你可能没睡醒"})
	case 3:
		if list, number := gorm_mysql.GetDataAuthRight(&m.DataHash); number == 0 {
			return c.JSON(http.StatusOK, models.ResultAuthRight{Result: false, Note: "无查询结果"})
		} else {
			return c.JSON(http.StatusOK, models.ResultAuthRight{Result: true, Note: "查询成功", AuthRight: list})
		}
	case 4:
		if list, number := gorm_mysql.GetDataConfirm(&m.DataHash); number == 0 {
			return c.JSON(http.StatusOK, models.ResultConfirm{Result: false, Note: "无查询结果"})
		} else {
			return c.JSON(http.StatusOK, models.ResultConfirm{Result: true, Note: "查询成功", Confirm: list})
		}
	default:
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "我觉得你可能没睡醒"})
	}
}

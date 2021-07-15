package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

func GetNumber(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uid, _ := strconv.ParseUint(uidString, 10, 64)
	m := models.Data{Uid: uid}
	err := gorm_mysql.CreateData(&m)
	if err != nil {
		return err
	}
	return c.JSON(http.StatusOK, models.Result{Result: true})
}

/*func DataUpload(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uid, _ := strconv.ParseUint(uidString, 10, 64)
	d := new(models.Data)
	if err := c.Bind(d); err != nil {
		return err
	}
	fmt.Printf("uid为: %d /n ", uid)
	num, err := gorm_mysql.CountData()
	if err != nil {
		return err
	}
	d.Uid = uid
	d.DataNumber = num + 1
	d.DataType = "pic"
	r := grpc.CreateDataClient(d)
	fmt.Println("Result is ", r.Result)
	fmt.Println("d is ", d)
	if r.Result {
		d.TxId = r.TxId
		err := gorm_mysql.CreateData(d)
		fmt.Println("err is ", err)
		if err != nil {
			return err
		}
		return c.JSON(http.StatusOK, models.Result{Result: true})
	}
	return c.JSON(http.StatusOK, models.Result{Result: false})
}*/



package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"Starry/util"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

//上架功能，需要登陆
func Sell(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	imgName := c.FormValue("imgName")
	imgPrice := c.FormValue("imgPrice")
	imgTopic := c.FormValue("imgTopic")
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	imgPriceUint, _ := strconv.ParseUint(imgPrice, 10, 64)
	imgPath := fmt.Sprintf("img/%s/%s", uidString, imgName)
	thumbnailPath := fmt.Sprintf("thumbnailImg/%s/%s", uidString, imgName)
	if imgTopic == "" {
		imgTopic = "无主题"
	}

	sellMsg := models.SellMsg{
		Uid:           uidUint,
		ImgName:       imgPath,
		ThumbnailPath: thumbnailPath,
		ImgPrice:      imgPriceUint,
		ImageTopic:    imgTopic,
	}

	//如果该照片已经上架,更新该数据
	if gorm_mysql.HasSellImgName(sellMsg.ImgName) {
		//再次上架,更新数据
		if err := gorm_mysql.UpdateSellMsg(uidUint, imgPath, imgPriceUint, imgTopic); err != nil {
			return err
		} else {
			return c.JSON(http.StatusOK, models.SellResult{
				Result: true,
				Note:   "该照片已上架--更新成功",
			})
		}
	} else if err := gorm_mysql.CreateSellInfo(&sellMsg); err != nil {
		return err
	} else {
		return c.JSON(http.StatusOK, models.SellResult{
			Result: true,
			Note:   "上架成功",
		})
	}
}

//获取上架信息集合，需要登陆
func GetSellInfoList(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	sellInfoList := gorm_mysql.GetSellInfoList(&uidUint)
	return c.JSON(http.StatusOK, models.SellInfoListResult{
		Result: true,
		Note:   "获取上架信息成功",
		Data:   *sellInfoList,
	})
}

//获取上架图片集合
func GeySellImgList(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	sellImgList := gorm_mysql.GetSellImgNameByUid(uidUint)
	if len(sellImgList) == 0 {
		return c.JSON(http.StatusOK,
			models.GetSellImgListResult{
				Result: false,
				Note:   "无上架图片",
				Data:   nil,
			})
	} else {
		return c.JSON(http.StatusOK,
			models.GetSellImgListResult{
				Result: true,
				Note:   "获取上架图片集合成功",
				Data:   sellImgList,
			})
	}
}

//下架图片,需要登录
func DeleteSellImg(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	imgName := c.FormValue("imgName")
	imgPath := "img/" + strconv.Itoa(int(uidUint)) + "/" + imgName
	thumbnailPath := "thumbnailImg/" + strconv.Itoa(int(uidUint)) + "/" + imgName

	//判断是否有该照片数据
	if !gorm_mysql.IsHasSellImgName(uidUint, imgPath) {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "无该图片数据",
			Data:   "",
		})
	}
	//判断是否有该图片
	if res, _ := util.PathExists(imgPath); !res {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "无该图片",
			Data:   "",
		})
	}

	//删除数据
	_ = gorm_mysql.DeleteSell(uidUint, imgPath)
	//删除照片
	//_ = os.Remove(imgPath)
	//删除对应购物车的照片信息
	_ = gorm_mysql.DeleteShoppingCartInfoByImgPath(thumbnailPath)

	return c.JSON(http.StatusOK, models.DeleteSellImgResult{
		Result: true,
		Note:   "删除成功",
	})
}

//修改上架图片价格
func UpdateSellImgPrice(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uid := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	imgName := c.FormValue("imgName")
	newImgPrice := c.FormValue("newImgPrice")

	imgPath := "img/" + strconv.Itoa(int(uidUint)) + "/" + imgName
	newImgPriceInt, _ := strconv.Atoi(newImgPrice)

	//判断是否有图片数据
	if !gorm_mysql.IsHasSellImgName(uidUint, imgPath) {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "无该图片数据",
			Data:   "",
		})
	}

	//更新图片价格
	if err := gorm_mysql.UpdateSellMsgImgPrice(uidUint, imgPath, newImgPriceInt); err != nil {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "修改价格失败",
			Data:   "",
		})
	} else {

		return c.JSON(http.StatusOK, models.UpdateSellImgPriceResult{
			Result: true,
			Note:   "修改价格成功",
		})
	}
}

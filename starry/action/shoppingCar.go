package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

//获取我的购物车数据，需要登陆
func GetMyShoppingCarInfo(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//买家uid
	purchaserIdString := claims["uid"].(string)
	purchaserId, _ := strconv.ParseUint(purchaserIdString, 10, 64)

	//获取该用户购物车下所有卖家用户名
	allSellerName := gorm_mysql.GetAllSellerNameByPurchaserId(purchaserId)
	var data []models.ShoppingCarOneSellerGoodsInfo
	//
	for i := 0; i < len(allSellerName); i++ {
		goodsInfo := gorm_mysql.GetGoodsInfoBySellerName(purchaserId, allSellerName[i])
		data = append(data, models.ShoppingCarOneSellerGoodsInfo{
			SellerName: allSellerName[i],
			AllGoods:   goodsInfo,
		})
	}
	if len(allSellerName) != 0 {
		return c.JSON(http.StatusOK, models.GetShoppingCarResult{
			Result: true,
			Note:   "获取购物车信息成功",
			Data:   data,
		})
	} else {
		return c.JSON(http.StatusOK, models.GetShoppingCarResult{
			Result: false,
			Note:   "购物车为空",
			Data:   nil,
		})
	}
}

//添加图片到购物车，需要登陆
func AddOneToShoppingCar(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//买家uid
	purchaserUidString := claims["uid"].(string)
	purchaserUid, _ := strconv.ParseUint(purchaserUidString, 10, 64)
	sellerName := c.FormValue("sellerName")
	imgPath := c.FormValue("imgPath")
	sellerUid := gorm_mysql.GetUserIdByUserName(sellerName)
	_, isExists := gorm_mysql.GetOneSellImgInfoByImgName(imgPath)
	if !isExists {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "加入购物车失败",
			Data:   "无该照片信息"})
	}

	m := models.ShoppingCar{
		PurchaserId: purchaserUid,
		SellerId:    sellerUid,
		ImgPath:     imgPath,
	}

	//验证自己的商品不可加入购物车，本来就是你的
	if sellerUid == purchaserUid {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "加入购物车失败",
			Data:   "它就是你的！！！"})
	}

	//先验证是否有此卖家
	if !gorm_mysql.IsHasUserName(sellerName) {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "加入购物车失败",
			Data:   "无此卖家信息"})
	}
	//验证是否已经加入购物车
	if gorm_mysql.IsHasDataShoppingCar(purchaserUid, imgPath) {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "加入购物车失败",
			Data:   "已经加入购物车"})
	} else {
		if err := gorm_mysql.CreateShoppingCar(&m); err != nil {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "加入购物车失败",
				Data:   "添加数据失败"})
		} else {
			return c.JSON(http.StatusOK, models.AddOneToShoppingCarResult{
				Result: true,
				Note:   "添加购物车成功",
			})
		}
	}
}

//移除购物车中的某条数据,需要登陆
func DeleteOneToShoppingCar(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//买家uid
	purchaserUidString := claims["uid"].(string)
	purchaserUid, _ := strconv.ParseUint(purchaserUidString, 10, 64)
	imgPath := c.FormValue("imgPath")
	//判断是否有此商品
	if !gorm_mysql.IsHasDataShoppingCar(purchaserUid, imgPath) {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "删除失败",
			Data:   "没有此商品信息"})
	}
	//删除操作
	if err := gorm_mysql.DeleteOneDataToShoppingCar(purchaserUid, imgPath); err != nil {
		return err
	} else {
		return c.JSON(http.StatusOK, models.DeleteOneToShoppingCarResult{
			Result: true,
			Note:   "删除成功",
		})
	}
}

//清空某用户的购物车,需要登录
func ClearMyShoppingCart(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	//登陆者的uid
	purchaserUidString := claims["uid"].(string)
	purchaserUid, _ := strconv.ParseUint(purchaserUidString, 10, 64)

	if gorm_mysql.ClearShoppingCart(purchaserUid) {
		return c.JSON(http.StatusOK,
			models.ClearMyShoppingCartResult{
				Result: true,
				Note:   "清空购物车成功",
			})
	}
	return c.JSON(http.StatusOK,
		models.ClearMyShoppingCartResult{
			Result: false,
			Note:   "购物车为空",
		})
}

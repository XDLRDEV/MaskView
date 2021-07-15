package action

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

// 获取展厅信息---未登录
func GetDisplayInfoNotLogin(c echo.Context) error {
	m := gorm_mysql.GetDisplayHallNotLogin()
	return c.JSON(http.StatusOK,
		models.GetDisplayInfoResult{
			Result: true,
			Note:   "获取展厅信息成功---未登录状态",
			Data:   *m,
		})
}

// 获取展厅信息---登录
func GetDisplayInfoAfterLogin(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	m := gorm_mysql.GetDisplayHallInfoAfterLogin(uidUint)

	return c.JSON(http.StatusOK,
		models.GetDisplayInfoResult{
			Result: true,
			Note:   "获取展厅信息成功---登录状态",
			Data:   *m,
		})
}

//展厅中某个照片的信息：所有者昵称,价格,上架日期
func GetOneSellImgInfo(c echo.Context) error {
	thumbnailPath := c.FormValue("imgName")
	imgInfo, isExists := gorm_mysql.GetOneSellImgInfoByImgName(thumbnailPath)
	if !isExists {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: false,
				Note:   "照片已下架",
				Data:   "",
			})
	}
	sellDate := imgInfo.CreatedAt.Format("2006-01-02") //必须是这个日期才能自动截取数据库的年月日
	uid := imgInfo.Uid
	userInfo := gorm_mysql.GetUserSecret(&uid)

	return c.JSON(http.StatusOK,
		models.OneSellImgInfoByImgNameResult{
			Result:   true,
			Note:     "获取该图片信息成功",
			UserName: userInfo.UserName,
			ImgTopic: imgInfo.ImageTopic,
			ImgPrice: imgInfo.ImgPrice,
			SellDate: sellDate,
		})
}

//展厅信息获取，不需要登陆(之前版本)
/*func GetMainViewInfo(c echo.Context) error {
	//拿到所有的uid
	allUid := gorm_mysql.GetAllUid()
	var allPhoneNumber []uint64
	var allUserName []string
	var allSellImgName []string
	var allSellImgPrice []uint

	//初始化结构体数组
	data := []models.MainViewInfo{}
	//循环对结构体数组赋值
	for i := 0; i < len(allUid); i++ {
		userInfo := gorm_mysql.GetUserSecret(&allUid[i])
		//获取对应uid的电话
		allPhoneNumber = append(allPhoneNumber, userInfo.PhoneNumber)
		//获取对应uid的昵称
		allUserName = append(allUserName, userInfo.UserName)
		//获取对应uid的所有上架图片名称
		allSellImgName = gorm_mysql.GetSellImgNameByUid(allUid[i])
		//获取对应uid的所有上架图片价格
		allSellImgPrice = gorm_mysql.GetSellImgPriceByUid(allUid[i])
		//筛选具有上架照片的用户
		if allSellImgName != nil {
			data = append(data,
				models.MainViewInfo{
					Uid:          allUid[i],
					PhoneNumber:  allPhoneNumber[i],
					UserName:     allUserName[i],
					HeadView:     userInfo.HeadView,
					SellImgPrice: allSellImgPrice,
					SellImgName:  allSellImgName,
				})
		}
	}

	return c.JSON(http.StatusOK,
		models.MainViewInfoResult{
			Result: true,
			Note:   "获取展厅信息成功",
			Data:   data,
		})
}*/

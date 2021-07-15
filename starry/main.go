package main

import (
	"Starry/action"
	"Starry/echarts"
	"Starry/gorm_mysql"
	"Starry/grpc"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"net/http"
	"runtime"
)

func main() {
	Init()
	e := echo.New()
	e.Pre(middleware.HTTPSRedirect())
	e.Use(middleware.Logger())
	e.Use(middleware.CORS())
	e.Use(middleware.RecoverWithConfig(middleware.RecoverConfig{
		StackSize: 4 << 10, // 4 KB
	}))
	e.Use(middleware.GzipWithConfig(middleware.GzipConfig{
		Level: 5,
	}))

	e.Static("/", "echarts")

	// 简单练习实例
	e.POST("/t1", action.T1)

	e.POST("/register", action.Register)                                             // 注册
	e.POST("/loginByPwd", action.LoginByPwd)                                         // 密码登录
	e.POST("/getPhoneCode", action.GetPhoneCode)                                     // 获取手机验证码
	e.POST("/loginByVCode", action.LoginByVCode)                                     // 验证码登陆
	e.POST("/autoLoginByCode", action.AutoLoginByCode)                               // 自动登录---验证码
	e.POST("/forgetPwdCheckCode", action.ForgetPwdCheckCode)                         // 忘记密码---校验手机验证码
	e.POST("/forgetResetPwd", action.ForgetResetPwd)                                 // 忘记密码---更改入库
	e.POST("/getDisplayInfoNotLogin", action.GetDisplayInfoNotLogin)                 // 获取展厅信息---Android版(未登录)
	e.POST("/getOneSellImgInfoByImgName", action.GetOneSellImgInfo)                  // 展厅信息---获取选中上架图片的信息
	e.POST("/getUserInfoAndSellListNotLogin", action.GetUserInfoAndSellListNotLogin) // 查看某人的信息和所有上架图片集合(未登录)
	e.POST("/getUserAllFocusInfoNotLogin", action.GetUserAllFocusInfoNotLogin)       // 获取某用户关注的人的信息(未登录)
	e.POST("/getUserAllFansInfoNotLogin", action.GetUserAllFansInfoNotLogin)         // 获取某用户粉丝的信息(未登录)

	e.POST("/test", action.Test)

	r := e.Group("/restricted")
	r.Use(middleware.JWT([]byte("secret")))
	r.POST("/uploadImg", action.UploadImg) //上传图片
	r.GET("/upload", action.GetNumber)
	//r.POST("/upload", action.DataUpload)
	//r.POST("/upload-win", action.SaveTradeImageFromWindows)

	//后续新添加的
	r.POST("/getDisplayInfoAfterLogin", action.GetDisplayInfoAfterLogin)                 // 获取展厅信息---登录后
	r.POST("/getUserInfoAndSellListAfterLogin", action.GetUserInfoAndSellListAfterLogin) // 查看某人的信息和所有上架图片集合(登录后)
	r.POST("/getMyInfo", action.GetMyInfo)                                               // 查看个人信息
	r.POST("/sellImg", action.Sell)                                                      // 上架照片
	r.POST("/getSellInfoList", action.GetSellInfoList)                                   // 查看上架信息
	r.POST("/getSellImgList", action.GeySellImgList)                                     // 查看上架图片集合
	r.POST("/confirmImg", action.ConfirmImg)                                             // 确权图片
	r.POST("/purchaseImg", action.PurchaseImg)                                           // 购买功能
	r.POST("/addOneToShoppingCar", action.AddOneToShoppingCar)                           // 添加到购物车
	r.POST("/deleteOneToShoppingCar", action.DeleteOneToShoppingCar)                     // 删除购物车
	r.POST("/getMyShoppingCarInfo", action.GetMyShoppingCarInfo)                         // 我的购物车信息
	r.POST("/clearMyShoppingCart", action.ClearMyShoppingCart)                           // 清空购物车
	r.POST("/deleteSellImg", action.DeleteSellImg)                                       // 删除上架图片
	r.POST("/updateSellImgPrice", action.UpdateSellImgPrice)                             // 修改上架图片价格
	r.POST("/getMyPurchaseRecords", action.GetMyPurchaseRecords)                         // 我的购买记录
	r.POST("/getMySaleRecords", action.GetMySaleRecords)                                 // 我的出售记录
	r.POST("/isMyImage", action.IsMyImage)                                               // 购买时,验证是否是自己的照片
	r.POST("/updateMyNickName", action.UpdateMyNickName)                                 // 修改,设置昵称
	r.POST("/updateMySex", action.UpdateMySex)                                           // 修改,设置性别
	r.POST("/updateMyBirth", action.UpdateMyBirth)                                       // 修改,设置出生日期
	r.POST("/updateMyHeadView", action.UpdateMyHeadView)                                 // 修改,设置头像
	r.POST("/updateMyBackgroundWall", action.UpdateMyBackGroundWall)                     // 修改，设置背景墙
	r.POST("/updatePwd", action.UpdatePwd)                                               // 更改密码
	r.POST("/focusUser", action.FocusUser)                                               // 关注某人
	r.POST("/cancelFocus", action.CancelFocus)                                           // 取消关注
	r.POST("/getUserAllFocusInfo", action.GetUserAllFocusInfo)                           // 获取某用户关注的人的信息(登录后)
	r.POST("/getUserAllFansInfo", action.GetUserAllFansInfo)                             // 获取某用户所有粉丝的信息(登录后)

	// 安全交易部分
	r.POST("/sellForSecureTra", action.SellForSecureTra)         // 安全交易---上架
	r.POST("/purchaseForSecureTra", action.PurchaseForSecureTra) // 安全交易---获取图片txt、价格及买卖双方

	// robust watermarkk
	//r.POST("/trade", action.Trade)
	r.POST("/images", action.ImgList)
	r.POST("/points", action.GetPoints)
	r.POST("/imgKey", action.GetImgKeyList)
	//r.POST("/imgKey-win", action.GetImageKeyFromWindows)
	r.POST("/tradeHistory", action.GetTradeHistory)
	r.POST("/getUid", action.GetUid)
	//r.POST("/preTrade", action.PreTrade)

	//zero watermark
	//r.POST("/getImageKey", action.GetImageKey)
	s := r.Group("/search")
	s.POST("/user/listing", action.GetUserList)
	s.POST("/data/hash", action.GetByDataHash)

	//c := e.Group("/visualization")
	e.GET("/test", echarts.BarHandler)

	//查看请求信息
	e.GET("/info", func(c echo.Context) error {
		req := c.Request()
		format := `
			<code>
				Protocol: %s<br>
				Host: %s<br>
				Remote Address: %s<br>
				Method: %s<br>
				Path: %s<br>
			</code>
		`
		return c.HTML(http.StatusOK, fmt.Sprintf(format, req.Proto, req.Host, req.RemoteAddr, req.Method, req.URL.Path))
	})

	//e.Logger.Fatal(e.StartTLS(":1718", "./cert.pem", "./key.pem"))
	e.Logger.Fatal(e.StartTLS(":718", "./cert.pem", "./key.pem"))
}

func Init() {
	runtime.GOMAXPROCS(runtime.NumCPU() * 6)
	fmt.Println("run CPUs number:", runtime.NumCPU())
	//base.Config()
	gorm_mysql.Init()
	grpc.Init()
}

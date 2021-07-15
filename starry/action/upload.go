package action

import (
	"Starry/models"
	"Starry/util"
	"bytes"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/disintegration/imaging"
	"github.com/labstack/echo/v4"
	"io"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"os"
)

/*func PreTrade(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	fromString := claims["uid"].(string)
	fromId, _ := strconv.ParseUint(fromString, 10, 64)

	userInfo := gorm_mysql.GetUserSecret(&fromId)
	fromPhone := userInfo.PhoneNumber
	from := strconv.Itoa(int(fromPhone))
	to := c.FormValue("to")
	v := c.FormValue("value")
	value, _ := strconv.ParseUint(v, 10, 64)
	code := strconv.Itoa(math_rand.Intn(8999) + 1000)
	// 30秒内只能请求一次验证码

	if value <= 0 {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "积分必须大于0",
			Data:   "",
		})
	}
	if from == to {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "不能给自己交易",
			Data:   "",
		})
	}
	record := gorm_mysql.GetTradeMsgByPhoneNumber(from, to)
	if record.ExpireTime-time.Now().Unix() > 270 {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "您发送短信的频率过于频繁",
			Data:   "",
		})
	}

	tradeMsg := models.TradeMsg{
		From:       from,
		To:         to,
		Code:       code,
		Value:      value,
		ExpireTime: time.Now().Unix() + 5*60,
	}
	if err := gorm_mysql.CreateTradeMsg(&tradeMsg); err != nil {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "发生错误，请重试",
			Data:   "",
		})
	}

	// 发送短信
	util.TradeNotify(&tradeMsg, userInfo.UserName)

	return c.JSON(http.StatusOK, models.SimpleResult{
		Result: true,
		Note:   "已经向对方发送验证码",
		Data:   "",
	})
}*/

/*func Trade(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	fromString := claims["uid"].(string)
	fromId, _ := strconv.ParseUint(fromString, 10, 64)
	fromUser := gorm_mysql.GetUserSecret(&fromId)
	fromPhoneString := strconv.Itoa(int(fromUser.PhoneNumber))

	phoneNumber, _ := strconv.ParseUint(c.FormValue("to"), 10, 64)
	toId := gorm_mysql.GetUserIdByPhoneNumber(&phoneNumber)
	toString := strconv.Itoa(int(toId))
	toPhoneString := strconv.Itoa(int(phoneNumber))

	value, _ := strconv.ParseUint(c.FormValue("value"), 10, 64)
	mold, _ := strconv.ParseUint(c.FormValue("mold"), 10, 64)
	imgName := c.FormValue("fileName")

	code := c.FormValue("code")

	// 比较验证码
	record := gorm_mysql.GetTradeMsgByPhoneNumber(fromPhoneString, toPhoneString)
	if record.ExpireTime < time.Now().Unix() || record.Code != code {
		fmt.Println(record.ExpireTime)
		fmt.Println(time.Now().Unix())
		fmt.Println(record.Code)
		fmt.Println(code)
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "验证码过期或者验证码不一致",
			Data:   "",
		})
	}
	// 判断交易积分是否被修改
	if record.Value != uint64(value) {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "积分被修改",
			Data:   "",
		})
	}

	fromString = checkId(fromString)
	toString = checkId(toString)
	exists, _ := util.PathExists("img/" + toString)
	if !exists {
		os.Mkdir("img/"+toString, os.ModePerm)
	}
	inputImagePath := fmt.Sprintf("img/%s/%s", fromString, imgName)
	outputImagePath := fmt.Sprintf("img/%s/%s%s-%s", toString, fromString, toString, imgName)

	if toId == 0 {
		return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "交易失败， 买家账号不存在", Data: ""})
	}

	res, _ := util.PathExists(inputImagePath)
	if !res {
		return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "图片不存在", Data: ""})
	}

	// 积分是否充足
	to := gorm_mysql.GetUserSecret(&toId)
	if to.Points < value {
		return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "对方积分不足", Data: ""})
	}

	// 调用水印算法，将交易信息嵌入，并将图片保存到本地，图片名为 from-to-txId
	fmt.Println("开始调用水印算法")
	tradeRes := util.Trade(fromString, toString, inputImagePath, outputImagePath)
	if !tradeRes {
		return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "交易失败-水印算法问题", Data: ""})
	}

	m := models.Trade{
		From:     fromId,
		To:       toId,
		Value:    value,
		Mold:     mold,
		DataHash: "",
		TxId:     "",
	}

	if value1, err := grpc.TradeClient(&m); err != nil {
		panic(err)
		return err
	} else if value1.Result {
		m.TxId = value1.TxId
		fmt.Println(value1)
		if err := gorm_mysql.CreateTrade(&m); err != nil {
			return err
		}
		var mutex sync.Mutex
		mutex.Lock()
		from := gorm_mysql.GetUserSecret(&fromId)
		from.Points = from.Points + value
		to.Points = to.Points - value
		fmt.Printf("对方的积分是%d", to.Points)
		if err := gorm_mysql.UpdateUser(from); err != nil {
			mutex.Unlock()
			return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "交易失败-更新失败", Data: ""})
		}
		if err := gorm_mysql.UpdateUser(to); err != nil {
			mutex.Unlock()
			return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "交易失败", Data: ""})
		}
		mutex.Unlock()
		// 发短信
		util.Seller(to.UserName, to.PhoneNumber, value, from.Points, from.PhoneNumber)
		util.Buyer(from.UserName, from.PhoneNumber, value, to.Points, to.PhoneNumber)
		return c.JSON(http.StatusOK, models.SimpleResult{Result: true, Note: "交易成功", Data: outputImagePath})
	}
	return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "交易失败--最后", Data: ""})
}*/

// get zero watermark key
/*func GetImageKey(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidString = checkId(uidString)

	fileName := c.FormValue("fileName")
	imageInputPath := fmt.Sprintf("img/%s/%s", uidString, fileName)
	imageOutputPath := fmt.Sprintf("img/%s/%s000-%s", uidString, uidString, fileName)

	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	userInfo := gorm_mysql.GetUserSecret(&uidUint)

	res, _ := util.PathExists(imageInputPath)
	if !res {
		return c.JSON(http.StatusOK, models.SimpleResult{Result: false, Note: "图片不存在", Data: ""})
	}

	imageKey := util.Add(uidString, "000", imageInputPath, imageOutputPath)

	// 存db
	if err := gorm_mysql.CreateImgKey(&models.ImageKey{
		Uid:         uidUint,
		PhoneNumber: userInfo.PhoneNumber,
		ImageName:   fmt.Sprintf("img/%s/%s", uidString, fileName),
		ImgKey:      imageKey,
	}); err != nil {
		debug.PanicErr(err)
	}

	// 上链
	num, err := gorm_mysql.CountData()
	if err != nil {
		return err
	}

	d := models.Data{
		Uid:        uidUint,
		DataNumber: num + 1, // 用业务层生成递增id需要加锁？
		DataType:   "pic",
		DataHash:   "",
		ZeroInfo:   imageKey,
		RobustInfo: "",
		TxId:       "",
	}
	r := grpc.CreateDataClient(&d)
	if r.Result {
		d.TxId = r.TxId
		// 存txId ?? 用户只能根据 imgKey 找到 txId 再到链上找证明了
		err := gorm_mysql.CreateData(&d)
		if err != nil {
			return err
		}
		return c.JSON(http.StatusOK, models.SimpleResult{Result: true, Note: imageOutputPath, Data: imageKey})
	}
	return c.JSON(http.StatusOK, models.Result{Result: false})
}*/

// 接受win客户端传过来的秘钥和图片名，存到db和链上
/*func GetImageKeyFromWindows(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	uidUint, _ := strconv.ParseUint(uidString, 10, 64)
	key := c.FormValue("key")
	fileName := c.FormValue("filename")
	userInfo := gorm_mysql.GetUserSecret(&uidUint)

	// 存db
	if err := gorm_mysql.CreateImgKey(&models.ImageKey{
		Uid:         uidUint,
		PhoneNumber: userInfo.PhoneNumber,
		ImageName:   fmt.Sprintf("img/%s/%s", uidString, fileName),
		ImgKey:      key,
	}); err != nil {
		debug.PanicErr(err)
	}
	// 存链上
	num, err := gorm_mysql.CountData()
	if err != nil {
		return err
	}

	d := models.Data{
		Uid:        uidUint,
		DataNumber: num + 1, // 用业务层生成递增id需要加锁？
		DataType:   "pic",
		DataHash:   "",
		ZeroInfo:   key,
		RobustInfo: "",
		TxId:       "",
	}
	r := grpc.CreateDataClient(&d)
	if r.Result {
		d.TxId = r.TxId
		// 存txId ?? 用户只能根据 imgKey 找到 txId 再到链上找证明了
		err := gorm_mysql.CreateData(&d)
		if err != nil {
			return err
		}
		return c.JSON(http.StatusOK, models.SimpleResult{Result: true, Note: "上链成功"})
	}
	return c.JSON(http.StatusOK, models.SimpleResult{
		Result: false,
		Note:   "发生错误",
		Data:   "",
	})
}*/

// 上传win客户端的交易图片，放到买放的图片目录下
/*func SaveTradeImageFromWindows(c echo.Context) error {
	phoneNumber := c.FormValue("phoneNumber")
	phone, _ := strconv.ParseUint(phoneNumber, 10, 64)
	uidUint := gorm_mysql.GetUserIdByPhoneNumber(&phone)
	uidString := strconv.Itoa(int(uidUint))
	file, err := c.FormFile("file")
	if err != nil {
		return err
	}
	if saveOrigin(file, uidString) {
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "图片上传成功"})
	}
	return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "图片上传失败"})
}*/

func UploadImg(c echo.Context) error {
	user := c.Get("user").(*jwt.Token)
	claims := user.Claims.(jwt.MapClaims)
	uidString := claims["uid"].(string)
	file, err := c.FormFile("file")
	if err != nil {
		return err
	}
	if saveOrigin(file, uidString) {
		// 保存原图后， 判断原图大小, 小于200kB不进行压缩处理
		path := "img/" + uidString + "/" + file.Filename
		fis, _ := os.Stat(path)
		if fis.Size() > 200000 { // 200000B 即：200kB
			if saveThumbnail(uidString, file.Filename) {
				return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "图片上传成功"})
			}
		} else {
			// 小于200kB, 直接原图复制到缩略图文件夹
			if saveOriginToThumbnail(file, uidString) {
				return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "原图保存,无缩略"})
			}
		}
	}
	return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "图片上传失败"})
}

// 保存原图
func saveOrigin(file *multipart.FileHeader, uid string) bool {
	src, err := file.Open()
	if err != nil {
		return false
	}
	defer src.Close()
	println(uid)
	imgDir := "img/" + uid
	exists, err := util.PathExists(imgDir)
	if !exists {
		err = os.MkdirAll(imgDir, os.ModePerm)
		if err != nil {
			return false
		}
	}
	dst, err := os.Create(imgDir + "/" + file.Filename)
	if err != nil {
		return false
	}
	if _, err = io.Copy(dst, src); err != nil {
		return false
	}
	defer dst.Close()
	return true
}

// 保存缩略图
func saveThumbnail(uid string, imgName string) bool {
	// 生成缩略图
	originPath := "img/" + uid + "/" + imgName
	imgData, _ := ioutil.ReadFile(originPath)
	fmt.Println("原图路径：" + originPath)
	buf := bytes.NewBuffer(imgData)
	image, err := imaging.Decode(buf)
	if err != nil {
		fmt.Println(err)
		return false
	}
	// 保存缩略图
	thumbnailDir := "thumbnailImg/" + uid
	exists, err := util.PathExists(thumbnailDir)
	if !exists {
		err = os.MkdirAll(thumbnailDir, os.ModePerm)
		if err != nil {
			return false
		}
	}
	//生成缩略图，尺寸宽x,高传0表示等比例放缩
	image = imaging.Resize(image, 600, 0, imaging.Lanczos)
	err = imaging.Save(image, thumbnailDir+"/"+imgName)
	fmt.Println("保存缩略路径：" + thumbnailDir + "/" + imgName)
	if err != nil {
		fmt.Println(err)
		return false
	}
	return true
}

// <200kB, 保存原图到缩略文件夹
func saveOriginToThumbnail(file *multipart.FileHeader, uid string) bool {
	src, err := file.Open()
	if err != nil {
		return false
	}
	defer src.Close()
	println(uid)
	imgDir := "thumbnailImg/" + uid
	exists, err := util.PathExists(imgDir)
	if !exists {
		err = os.MkdirAll(imgDir, os.ModePerm)
		if err != nil {
			return false
		}
	}
	dst, err := os.Create(imgDir + "/" + file.Filename)
	if err != nil {
		return false
	}
	if _, err = io.Copy(dst, src); err != nil {
		return false
	}
	defer dst.Close()
	return true
}

func checkId(uid string) string {
	if len(uid) == 1 {
		uid = "00" + uid
	}
	if len(uid) == 2 {
		uid = "0" + uid
	}
	if len(uid) > 3 {
		uid = uid[0:3]
	}
	return uid
}

package action

import (
	"Starry/api/debug"
	"Starry/gorm_mysql"
	"Starry/models"
	"Starry/util"
	"crypto/rand"
	"crypto/sha256"
	"crypto/tls"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
	"golang.org/x/crypto/pbkdf2"
	"io/ioutil"
	math_rand "math/rand"
	"net/http"
	"strconv"
	"strings"
	"time"
)

//密码登录
func LoginByPwd(c echo.Context) error {
	m := new(models.RequestUser)
	if err := c.Bind(m); err != nil {
		return err
	}
	fmt.Printf("手机号为: %d 密码为: %s \n", m.PhoneNumber, m.Password)
	if gorm_mysql.IsHasPhoneNumber(m.PhoneNumber) {
		userInfo := gorm_mysql.GetUserSecretByPhoneNumber(&m.PhoneNumber)
		getSalt, err := hex.DecodeString(userInfo.Salt)
		debug.PanicErr(err)
		key := pbkdf2.Key([]byte(m.Password), getSalt, 1323, 32, sha256.New)
		if hex.EncodeToString(key) == userInfo.Key {
			// CreateUser token
			token := jwt.New(jwt.SigningMethodHS256)
			// Set claims
			claims := token.Claims.(jwt.MapClaims)
			claims["uid"] = strconv.FormatUint(userInfo.Uid, 10)
			claims["userName"] = userInfo.UserName
			claims["userPhone"] = userInfo.PhoneNumber
			claims["exp"] = time.Now().Add(time.Hour * 72).Unix() //有效期三天
			// Generate encoded token and send it as response.
			t, err := token.SignedString([]byte("secret"))
			debug.PanicErr(err)
			points := GetMyPoints(userInfo.Uid)
			if points == -1 {
				return c.JSON(http.StatusOK,
					models.Token{
						Result: false,
						Token:  "链上获取积分失败",
					},
				)
			}
			return c.JSON(http.StatusOK,
				models.Token{
					Result:         true,
					Token:          t,
					UserName:       userInfo.UserName,
					Uid:            userInfo.Uid,
					UserPhone:      userInfo.PhoneNumber,
					Points:         uint64(points), //Points:         userInfo.Points,
					HeadView:       userInfo.HeadView,
					BackgroundWall: userInfo.BackgroundWall,
				},
			)
		} else {
			return c.JSON(http.StatusOK,
				models.Token{
					Result: false,
					Note:   "用户名或密码错误",
				},
			)
		}
	} else {
		return c.JSON(http.StatusOK,
			models.Token{
				Result: false,
				Token:  "未注册,请用手机验证码登录!",
			},
		)
	}

}

//忘记密码---1.验证码是否正确
func ForgetPwdCheckCode(c echo.Context) error {
	m := new(models.RequestForgetPwdCheckCode)
	if err := c.Bind(m); err != nil {
		return err
	}
	if !util.CheckNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "手机号不合法，请检查后重试",
			},
		)
	}

	msg := gorm_mysql.GetOthersCodeMsg(&m.PhoneNumber)
	if msg == nil || msg.Code != m.VCode || msg.ExpireTime+5*60 < time.Now().Unix() {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "验证码错误",
			},
		)
	} else {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: true,
				Note:   "验证码正确",
			})
	}
}

// 忘记密码---2.更改
func ForgetResetPwd(c echo.Context) error {
	m := new(models.RequestForgetPassword)
	if err := c.Bind(m); err != nil {
		return err
	}

	userInfo := gorm_mysql.GetUserSecretByPhoneNumber(&m.PhoneNumber)

	salt := make([]byte, 32)
	_, err := rand.Read(salt)
	if err != nil {
		return err
	}

	key := pbkdf2.Key([]byte(m.Password), salt, 1323, 32, sha256.New)
	user := models.User{
		Uid:      userInfo.Uid,
		UserName: userInfo.UserName,
		Salt:     hex.EncodeToString(salt),
		Key:      hex.EncodeToString(key),
	}
	err = gorm_mysql.UpdateUserInfo(user)
	if err != nil {
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "密码更新失败，请稍后重试"})
	}
	return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "密码更新成功"})
}

// 发送验证码---登陆与注册发送后存入的数据库的不同
func GetPhoneCode(c echo.Context) error {
	m := new(models.PhoneNumber)
	if err := c.Bind(m); err != nil {
		return err
	}
	if !util.CheckNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "手机号有误，请检查后重试"})
	}
	if gorm_mysql.IsHasPhoneNumber(m.PhoneNumber) {
		//登陆,忘记密码的验证码发送
		code := strconv.Itoa(math_rand.Intn(8999) + 1000)
		res := util.RegisterOrLoginOrForget(m.PhoneNumber, code)
		if res {
			t := time.Now().Unix() // 秒
			msg := models.OthersCode{
				PhoneNumber: m.PhoneNumber,
				Code:        code,
				ExpireTime:  t,
			}
			if gorm_mysql.IsHasOthersCodeMsg(m.PhoneNumber) {
				if err := gorm_mysql.UpdateOthersCodeMsg(&m.PhoneNumber, code); err != nil {
					return err
				}
			} else {
				if err := gorm_mysql.CreateOthersCodeMsg(&msg); err != nil {
					return err
				}
			}
		} else {
			return c.JSON(http.StatusOK,
				models.ResultWithNote{
					Result: false,
					Note:   "验证码发送失败"})
		}
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: true,
				Note:   "验证码已发送"})
	} else {
		//注册的验证码发送
		code := strconv.Itoa(math_rand.Intn(8999) + 1000)
		res := util.RegisterOrLoginOrForget(m.PhoneNumber, code)
		if res {
			t := time.Now().Unix() // 秒
			msg := models.RegisterCode{
				PhoneNumber: m.PhoneNumber,
				Code:        code,
				ExpireTime:  t,
			}
			if gorm_mysql.IsHasRegisterCodeMsg(m.PhoneNumber) {
				if err := gorm_mysql.UpdateRegisterCodeMsg(&m.PhoneNumber, code); err != nil {
					return err
				}
			} else {
				if err := gorm_mysql.CreateRegisterCodeMsg(&msg); err != nil {
					return err
				}
			}
		} else {
			return c.JSON(http.StatusOK,
				models.ResultWithNote{
					Result: false,
					Note:   "验证码发送失败",
				},
			)
		}
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: true,
				Note:   "验证码已发送"})
	}
}

// 注册上链
func httpRegister(uid uint64) bool {
	uidString := strconv.Itoa(int(uid))
	url := JavaServer() + "/fisco-bcos/register"
	method := "POST"
	payload := strings.NewReader("uid=" + uidString)
	// 信任所有证书
	tr := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}
	client := &http.Client{
		Transport: tr,
	}
	req, err := http.NewRequest(method, url, payload)
	if err != nil {
		fmt.Println(err)
		return false
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")
	res, err := client.Do(req)
	if err != nil {
		return false
	}
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)
	//解析返回的JSON数据
	var message httpGetMyPoints
	err = json.Unmarshal(body, &message)
	if err != nil {
		return false
	}
	if message.Result {
		return true
	} else {
		return false
	}
}

// 注册
func Register(c echo.Context) error {
	m := new(models.RequestRegister)
	if err := c.Bind(m); err != nil {
		return err
	}
	//t := time.Now()
	if !util.CheckNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "手机号有误，请检查后重试"})
	}
	if gorm_mysql.IsHasPhoneNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK,
			models.ResultLogin{
				Result: false,
				Note:   "已注册过",
			},
		)
	}
	// 信息入库
	//pbkdf2加密
	salt := make([]byte, 32)
	_, err := rand.Read(salt)
	if err != nil {
		return err
	}
	key := pbkdf2.Key([]byte(m.Password), salt, 1323, 32, sha256.New)
	User := models.User{
		PhoneNumber: m.PhoneNumber,
		UserName:    RandomUserName(),
		Salt:        hex.EncodeToString(salt),
		Key:         hex.EncodeToString(key),
	}
	if err = gorm_mysql.CreateUser(&User); err != nil {
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "信息入库失败",
			Data:   "",
		})
	} else {
		// 信息入库成功, 上链
		fmt.Println("注册成功，开始与节点通信，注册上链...")

		uid := gorm_mysql.GetUserIdByPhoneNumber(&m.PhoneNumber)
		println("uid为------")
		println(uid)
		if httpRegister(uid) {
			return c.JSON(http.StatusOK, models.ResultLogin{
				Result: true,
				Note:   "注册成功",
			})
		} else {
			err := gorm_mysql.DeleteUserInfoByUid(uid)
			if err != nil {
				return err
			}
			return c.JSON(http.StatusOK,
				models.ResultWithNote{
					Result: false,
					Note:   "与区块连节点通信失败",
				},
			)
		}
	}
	// 之前版本注册上链
	/*if grpc.CreateUserClient(m.PhoneNumber, t.Format("2006-01-02 15:04:05")) {
		//pbkdf2加密
		salt := make([]byte, 32)
		_, err := rand.Read(salt)
		if err != nil {
			return err
		}
		key := pbkdf2.Key([]byte(m.Password), salt, 1323, 32, sha256.New)
		User := models.User{
			PhoneNumber: m.PhoneNumber,
			UserName:    RandomUserName(),
			Salt:        hex.EncodeToString(salt),
			Key:         hex.EncodeToString(key),
			Points:      500,
		}
		if gorm_mysql.IsHasPhoneNumber(m.PhoneNumber) {
			return c.JSON(http.StatusOK,
				models.ResultLogin{
					Result: false,
					Note:   "已注册过",
				},
			)
		} else {
			if err = gorm_mysql.CreateUser(&User); err != nil {
				return err
			}
			return c.JSON(http.StatusOK,
				models.ResultLogin{
					Result: true,
					Note:   "注册成功",
				},
			)
		}
	} else {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "与区块连节点通信失败",
			},
		)
	}*/
}

//短信验证码登陆
func LoginByVCode(c echo.Context) error {
	m := new(models.RequestLoginByVCode)
	if err := c.Bind(m); err != nil {
		return err
	}
	if gorm_mysql.IsHasPhoneNumber(m.PhoneNumber) {
		//已经注册,返回登陆者信息
		msg := gorm_mysql.GetOthersCodeMsg(&m.PhoneNumber)
		if msg == nil || msg.Code != m.Vcode || msg.ExpireTime+5*60 < time.Now().Unix() {
			return c.JSON(http.StatusOK,
				models.ResultWithNote{
					Result: false,
					Note:   "验证码错误",
				},
			)
		}
		userInfo := gorm_mysql.GetUserSecretByPhoneNumber(&m.PhoneNumber)
		token := jwt.New(jwt.SigningMethodHS256)
		claims := token.Claims.(jwt.MapClaims)
		claims["uid"] = strconv.FormatUint(userInfo.Uid, 10)
		claims["userName"] = userInfo.UserName
		claims["exp"] = time.Now().Add(time.Hour * 72).Unix() //有效期三天
		t, err := token.SignedString([]byte("secret"))
		debug.PanicErr(err)
		return c.JSON(http.StatusOK,
			models.Token{
				Result:         true,
				Token:          t,
				UserName:       userInfo.UserName,
				Uid:            userInfo.Uid,
				UserPhone:      userInfo.PhoneNumber,
				Points:         userInfo.Points,
				HeadView:       userInfo.HeadView,
				BackgroundWall: userInfo.BackgroundWall,
				Note:           "登录请求成功",
			},
		)
	} else {
		//未注册,先判断验证码,再注册
		msg := gorm_mysql.GetRegisterCodeMsg(&m.PhoneNumber)
		if msg == nil || msg.Code != m.Vcode || msg.ExpireTime+5*60 < time.Now().Unix() {
			return c.JSON(http.StatusOK,
				models.ResultWithNote{
					Result: false,
					Note:   "验证码错误",
				},
			)
		}
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: true,
				Note:   "请注册",
				Data:   "",
			})
	}
}

//随机生成长度为20的用户名
func RandomUserName() string {
	kinds := [][]int{[]int{10, 48}, []int{26, 97}, []int{26, 65}}
	result := make([]byte, 20) //20位
	for i := 0; i < 20; i++ {
		randKind := math_rand.Intn(3)
		scope, base := kinds[randKind][0], kinds[randKind][1]
		result[i] = uint8(base + math_rand.Intn(scope))
	}
	return string(result)
}

//用于短信验证码登录后,下次自动登录
func AutoLoginByCode(c echo.Context) error {
	m := new(models.RequestUser)
	if err := c.Bind(m); err != nil {
		return err
	}
	fmt.Printf("手机号为: %d 验证码为: %s \n", m.PhoneNumber, m.Code)

	if gorm_mysql.IsHasPhoneAndCode(m.PhoneNumber, m.Code) {
		userInfo := gorm_mysql.GetUserSecretByPhoneNumber(&m.PhoneNumber)
		// CreateUser token
		token := jwt.New(jwt.SigningMethodHS256)
		// Set claims
		claims := token.Claims.(jwt.MapClaims)
		claims["uid"] = strconv.FormatUint(userInfo.Uid, 10)
		claims["userName"] = userInfo.UserName
		claims["userPhone"] = userInfo.PhoneNumber
		claims["exp"] = time.Now().Add(time.Hour * 72).Unix() //有效期三天
		// Generate encoded token and send it as response.
		t, err := token.SignedString([]byte("secret"))
		debug.PanicErr(err)
		return c.JSON(http.StatusOK,
			models.Token{
				Result:         true,
				Token:          t,
				UserName:       userInfo.UserName,
				Uid:            userInfo.Uid,
				UserPhone:      userInfo.PhoneNumber,
				Points:         userInfo.Points,
				HeadView:       userInfo.HeadView,
				BackgroundWall: userInfo.BackgroundWall,
			},
		)

	}
	return c.JSON(http.StatusOK,
		models.Token{
			Result: false,
			Token:  "验证码错误",
		},
	)
}

//注册---(之前版本)
/*func Register(c echo.Context) error {
	m := new(models.RequestUser)
	if err := c.Bind(m); err != nil {
		return err
	}
	fmt.Printf("手机号为: %d 密码为: %s 验证码为 %s, 用户名为 %s \n",
		m.PhoneNumber, m.Password, m.Code, m.UserName)
	if !util.CheckNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "手机号有误，请检查后重试"})
	}

	msg := gorm_mysql.GetRegisterCodeMsg(&m.PhoneNumber)
	fmt.Printf("时间差为： %d 分钟 \n", (time.Now().Unix()-msg.ExpireTime)/60)
	if msg == nil || msg.Code != m.Code || msg.ExpireTime+5*60 < time.Now().Unix() {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "验证码失效或者不合法",
			},
		)
	} // 查看验证码是否过期，过期直接返回 默认过期时间5分钟

	if gorm_mysql.HasPhoneNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{Result: false,
				Note: "该用户已存在",
			},
		)
	} else if grpc.CreateUserClient(m.PhoneNumber) {
		//pbkdf2加密
		salt := make([]byte, 32)
		_, err := rand.Read(salt)
		if err != nil {
			return err
		}
		key := pbkdf2.Key([]byte(m.Password), salt, 1323, 32, sha256.New)
		User := models.User{
			PhoneNumber: m.PhoneNumber,
			UserName:    m.UserName,
			Salt:        hex.EncodeToString(salt),
			Key:         hex.EncodeToString(key),
			Points:      500,
		}
		if err = gorm_mysql.CreateUser(&User); err != nil {
			return err
		}
		return c.JSON(http.StatusOK,
			models.ResultLogin{
				Result: true,
				Note:   "注册成功",
			},
		)
	} else {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "与区块连节点通信失败",
			},
		)
	}
}*/

//获取注册验证码(之前版本)
/*func GetRegisterCode(c echo.Context) error {
	m := new(models.PhoneNumber)
	if err := c.Bind(m); err != nil {
		return err
	}
	if !util.CheckNumber(m.PhoneNumber) {
		fmt.Printf("手机号为： %d", m.PhoneNumber)
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "手机号有误，请检查后重试"})
	}
	if gorm_mysql.HasPhone(m.PhoneNumber) {
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "该手机号已存在"})
	} else {
		code := strconv.Itoa(math_rand.Intn(8999) + 1000)
		res := util.RegisterOrLoginOrForget(m.PhoneNumber, code)
		if res {
			t := time.Now().Unix() // 秒
			msg := models.RegisterCode{
				PhoneNumber: m.PhoneNumber,
				Code:        code,
				ExpireTime:  t,
			}
			if gorm_mysql.IsHasRegisterCodeMsg(m.PhoneNumber) {
				if err := gorm_mysql.UpdateRegisterCodeMsg(&m.PhoneNumber, code); err != nil {
					return err
				}
			} else {
				if err := gorm_mysql.CreateRegisterCodeMsg(&msg); err != nil {
					return err
				}
			}
		} else {
			return c.JSON(http.StatusOK,
				models.ResultWithNote{
					Result: false,
					Note:   "验证码发送失败",
				},
			)
		}
	}
	return c.JSON(http.StatusOK,
		models.ResultWithNote{
			Result: true,
			Note:   "验证码已发送",
		},
	)
}*/

// 发送验证码， 有效时间为5分钟， 用于重置密码,登录等操作(之前版本)
/*func GetOthersCode(c echo.Context) error {
	m := new(models.PhoneNumber)
	if err := c.Bind(m); err != nil {
		return err
	}
	if !util.CheckNumber(m.PhoneNumber) {
		fmt.Printf("手机号为： %d", m.PhoneNumber)
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "手机号有误，请检查输入"})
	}
	if !gorm_mysql.HasPhone(m.PhoneNumber) {
		return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "该手机号未注册"})
	} else {
		code := strconv.Itoa(math_rand.Intn(8999) + 1000)
		res := util.RegisterOrLoginOrForget(m.PhoneNumber, code)
		if res {
			t := time.Now().Unix() // 秒
			msg := models.OthersCode{
				PhoneNumber: m.PhoneNumber,
				Code:        code,
				ExpireTime:  t,
			}
			if gorm_mysql.IsHasOthersCodeMsg(m.PhoneNumber) {
				if err := gorm_mysql.UpdateOthersCodeMsg(&m.PhoneNumber, code); err != nil {
					return err
				}
			} else {
				if err := gorm_mysql.CreateOthersCodeMsg(&msg); err != nil {
					return err
				}
			}
		} else {
			return c.JSON(http.StatusOK, models.ResultWithNote{Result: false, Note: "验证码发送失败"})
		}
	}
	return c.JSON(http.StatusOK, models.ResultWithNote{Result: true, Note: "验证码已发送"})
}*/

// 使用短信验证码登录(之前版本)
/*func LoginByVCode(c echo.Context) error {
	m := new(models.RequestLoginByVCode)
	if err := c.Bind(m); err != nil {
		return err
	}
	if !util.CheckNumber(m.PhoneNumber) {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "手机号不合法，请检查后重试",
			},
		)
	}

	msg := gorm_mysql.GetOthersCodeMsg(&m.PhoneNumber)
	if msg == nil || msg.Code != m.Vcode || msg.ExpireTime+5*60 < time.Now().Unix() {
		return c.JSON(http.StatusOK,
			models.ResultWithNote{
				Result: false,
				Note:   "验证码错误",
			},
		)
	}

	userInfo := gorm_mysql.GetUserSecretByPhoneNumber(&m.PhoneNumber)
	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	claims["uid"] = strconv.FormatUint(userInfo.Uid, 10)
	claims["userName"] = userInfo.UserName
	claims["exp"] = time.Now().Add(time.Hour * 72).Unix() //有效期三天
	t, err := token.SignedString([]byte("secret"))
	debug.PanicErr(err)
	return c.JSON(http.StatusOK,
		models.Token{
			Result:    true,
			Token:     t,
			UserName:  userInfo.UserName,
			Uid:       userInfo.Uid,
			UserPhone: userInfo.PhoneNumber,
			Points:    userInfo.Points,
		},
	)
}*/

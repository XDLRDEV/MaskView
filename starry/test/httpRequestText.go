package main

import (
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
)

func main() {
	//httpPost()
	/*qq := action.GetMyPoints(1)
	fmt.Println(qq)*/
	http1()
}

type Uid struct {
	Uid uint64 `json:"uid"` // 用户uid
}

func http1()  {
	url := "https://qqcloud1.xdlianrong.cn:1718/fisco-bcos/normalTrade"
	method := "POST"

	payload := strings.NewReader("purchaserUid=1&sellerUid=2&imgPrice=2")
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
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")

	res, err := client.Do(req)
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)

	fmt.Println(string(body))
}


/**
http传递单个参数
*/
func httpPost() {

	url := "https://localhost:1718/test/getUserInfo"
	method := "POST"

	uid := "1"
	payload := strings.NewReader("uid=" + uid)
	//payload := strings.NewReader("uid=" + uid + "&password=123321")
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
		return
	}
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")

	res, err := client.Do(req)
	defer res.Body.Close()
	body, err := ioutil.ReadAll(res.Body)

	//fmt.Println(string(body))
	//解析返回的JSON数据
	var message result
	err = json.Unmarshal(body, &message)

	fmt.Printf("%+v\n", message)
	fmt.Printf("%v\t%v\n", message.Code, message.Data)
}

type result struct {
	Code    uint64 `json:"code"`
	Message string `json:"message"`
	Data    user   `json:"data"`
}

type user struct {
	Uid            uint64 `json:"uid"`
	PhoneNumber    string `json:"phone_number"`
	UserName       string `json:"username"`
	Salt           string `json:"salt"`
	Points         string `json:"points"`
	HeadView       string `json:"head_view"`
	BackgroundWall string `json:"background_wall"`
	Birth          string `json:"birth"`
	Sex            string `json:"sex"`
}

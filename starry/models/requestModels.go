package models

type PhoneNumber struct {
	PhoneNumber uint64 `json:"phoneNumber"`
}

type RequestUser struct {
	PhoneNumber uint64 `json:"phoneNumber"`
	UserName    string `json:"username"`
	Password    string `json:"password"`
	Code        string `json:"code"`
}

type RequestRegister struct {
	PhoneNumber uint64 `json:"phoneNumber"`
	Password    string `json:"password"`
}

type RequestReset struct {
	PhoneNumber uint64 `json:"phoneNumber"`
	UserName    string `json:"userName"`
}

type RequestForgetPwdCheckCode struct {
	PhoneNumber uint64 `json:"phoneNumber"`
	VCode       string `json:"vcode"`
}

type RequestForgetPassword struct {
	PhoneNumber uint64 `json:"phoneNumber"`
	Password    string `json:"password"`
	VCode       string `json:"vcode"`
}

type RequestLoginByVCode struct {
	PhoneNumber uint64 `json:"phoneNumber"`
	Vcode       string `json:"vcode"`
}

type RequestData struct {
	DataHash   string `json:"dataHash"`
	SearchCode int    `json:"searchCode"`
}

type CrawlerData struct {
	RobustInfo string `json:"robustInfo"`
	ScreenShot string `json:"screenShot"`
	Url        string `json:"url"`
}




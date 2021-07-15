package models

type WeChat struct {
	Openid      string `json:"Openid" `
	Session_key string `json:"Session_key"`
	Unionid     string `json:"unionid"`
	Errcode     string `json:"Errcode"`
	Errmsg      string `json:"Errmsg"`
}

type UnifyOrderRequest struct {
	Appid            string `xml:"appid"`
	Mch_id           string `xml:"mch_id"`
	Nonce_str        string `xml:"nonce_str"`
	Sign             string `xml:"sign"`
	Body             string `xml:"body"`
	Out_trade_no     string `xml:"out_trade_no"`
	Total_fee        int    `xml:"total_fee"`
	Spbill_create_ip string `xml:"spbill_create_ip"`
	Notify_url       string `xml:"notify_url"`
	Trade_type       string `xml:"trade_type"`
	Openid           string `xml:"openid"`
}

type UnifyOrderResponse struct {
	ReturnCode  string `xml:"return_code"`
	ReturnMsg   string `xml:"return_msg"`
	Appid       string `xml:"appid"`
	MchId       string `xml:"mch_id"`
	NonceStr    string `xml:"nonce_str"`
	Sign        string `xml:"sign"`
	Result_code string `xml:"result_code"`
	Prepay_id   string `xml:"prepay_id"`
	Trade_type  string `xml:"trade_type"`
}

type WeChatRequestPayment struct {
	TimeStamp string `xml:"timeStamp"`
	Package   string `xml:"package"`
	PaySign   string `xml:"paySign"`
	NonceStr  string `xml:"nonceStr"`
	SignType  string `xml:"signType"`
}

type WXPayNotifyReq struct {
	Return_code    string `xml:"return_code"`
	Return_msg     string `xml:"return_msg"`
	Appid          string `xml:"appid"`
	Mch_id         string `xml:"mch_id"`
	Nonce_str      string `xml:"nonce_str"`
	Sign           string `xml:"sign"`
	Result_code    string `xml:"result_code"`
	Openid         string `xml:"openid"`
	Is_subscribe   string `xml:"is_subscribe"`
	Trade_type     string `xml:"trade_type"`
	Bank_type      string `xml:"bank_type"`
	Total_fee      int    `xml:"total_fee"`
	Fee_type       string `xml:"fee_type"`
	Cash_fee       int    `xml:"cash_fee"`
	Cash_fee_Type  string `xml:"cash_fee_type"`
	Transaction_id string `xml:"transaction_id"`
	Out_trade_no   string `xml:"out_trade_no"`
	Attach         string `xml:"attach"`
	Time_end       string `xml:"time_end"`
}
type FSICOBCOSResponse struct {
	Code            string
	NewValue        string
	TransactionHash string
}

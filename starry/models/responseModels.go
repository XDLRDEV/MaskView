package models

type MyInfoResult struct {
	Result         bool   `json:"result" xml:"result"`
	UserName       string `json:"userName"`
	UserPhone      uint64 `json:"userPhone"`
	Points         uint64 `json:"points"`
	HeadView       string `json:"headViewPath"`
	BackgroundWall string `json:"backgroundWall"`
	Sex            string `json:"sex"`
	Birth          string `json:"birth"`
	FocusCount     uint64 `json:"focusCount"`
	FansCount      uint64 `json:"fansCount"`
}

type Result struct {
	Result bool `json:"result" xml:"result"`
}

type ResultLogin struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

type ResultWithNote struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

type Token struct {
	Result         bool   `json:"result" xml:"result"`
	Token          string `json:"token" xml:"token"`
	UserName       string `json:"userName"`
	Uid            uint64 `json:"uid"`
	UserPhone      uint64 `json:"userPhone"`
	Points         uint64 `json:"points"`
	HeadView       string `json:"headViewPath"`
	BackgroundWall string `json:"backgroundWall"`
	Note           string `json:"note" xml:"note"`
}

type ResultWeChat struct {
	Return_code string `json:"return_code" xml:"return_code"`
	Return_msg  string `json:"return_msg" xml:"return_msg"`
}

type ResultData struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
	Data   *Data  `json:"data" xml:"data"`
}

type ResultCopyright struct {
	Result    bool       `json:"result" xml:"result"`
	Note      string     `json:"note" xml:"note"`
	Copyright *Copyright `json:"copyright" xml:"copyright"`
}

type ResultAuthRight struct {
	Result    bool       `json:"result" xml:"result"`
	Note      string     `json:"note" xml:"note"`
	AuthRight *AuthRight `json:"authRight" xml:"authRight"`
}

type ResultConfirm struct {
	Result  bool     `json:"result" xml:"result"`
	Note    string   `json:"note" xml:"note"`
	Confirm *Confirm `json:"confirm" xml:"confirm"`
}

type SimpleResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
	Data   string `json:"data" xml:"data"`
}

type FileResult struct {
	Result bool          `json:"result" xml:"result"`
	Note   string        `json:"note" xml:"note"`
	Data   []interface{} `json:"data" xml:"data"`
}

type TradeListResult struct {
	Result bool    `json:"result" xml:"result"`
	Note   string  `json:"note" xml:"note"`
	Data   []Trade `json:"data" xml:"data"`
}

type ImgKeyListResult struct {
	Result bool       `json:"result" xml:"result"`
	Note   string     `json:"note" xml:"note"`
	Data   []ImageKey `json:"data" xml:"data"`
}

//上架图片结果
type SellResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//获取上架信息结果
type SellInfoListResult struct {
	Result bool      `json:"result" xml:"result"`
	Note   string    `json:"note" xml:"note"`
	Data   []SellMsg `json:"data" xml:"data"`
}

//获取某个用户上架图片集合结果
type GetSellImgListResult struct {
	Result bool     `json:"result" xml:"result"`
	Note   string   `json:"note" xml:"note"`
	Data   []string `json:"data" xml:"data"`
}

//确权图片结果
type ConfirmImgResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//获取展厅信息的json返回结果
type MainViewInfoResult struct {
	Result bool           `json:"result" xml:"result"`
	Note   string         `json:"note" xml:"note"`
	Data   []MainViewInfo `json:"data" xml:"data"`
}

//获取展厅某个图片的信息
type OneSellImgInfoByImgNameResult struct {
	Result   bool   `json:"result" xml:"result"`
	Note     string `json:"note" xml:"note"`
	UserName string `json:"userName"`
	ImgTopic string `json:"imgTopic"`
	ImgPrice uint64 `json:"imgPrice"`
	SellDate string `json:"sellDate"`
}

//购买返回结果
type PurchaserImgResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//加入购物车返回结果
type AddOneToShoppingCarResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//删除购物车返回结果
type DeleteOneToShoppingCarResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//获取购物车信息结果
type GetShoppingCarResult struct {
	Result bool                            `json:"result" xml:"result"`
	Note   string                          `json:"note" xml:"note"`
	Data   []ShoppingCarOneSellerGoodsInfo `json:"data" xml:"data"`
}

type ShoppingCarOneSellerGoodsInfo struct {
	SellerName string       `json:"sellerName"`
	AllGoods   *[]GoodsInfo `json:"goodsInfo"`
}

type GoodsInfo struct {
	ImgTopic string `json:"imgTopic"`
	ImgPath  string `json:"imgPath"`
	ImgPrice string `json:"imgPrice"`
}

//删除上架图片
type DeleteSellImgResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//更新上架图片价格
type UpdateSellImgPriceResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

//我的购买记录结果
type MyPurchaseRecordsResult struct {
	Result bool                     `json:"result" xml:"result"`
	Note   string                   `json:"note" xml:"note"`
	Data   *[]MyPurchaseRecordsInfo `json:"data" xml:"data"`
}

//我的出售记录结果
type MySaleRecordsResult struct {
	Result bool                 `json:"result" xml:"result"`
	Note   string               `json:"note" xml:"note"`
	Data   *[]MySaleRecordsData `json:"data" xml:"data"`
}

//清空购物车返回结果
type ClearMyShoppingCartResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

// 关注结果
type FocusUserResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

// 取消关注结果
type CancelFocusResult struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
}

// 查看某个用户基本信息及所有上架照片返回结果
type GetUserInfoAndSellListResult struct {
	Result             bool     `json:"result" xml:"result"`
	Note               string   `json:"note" xml:"note"`
	Uid                uint64   `json:"uid"`
	PhoneNumber        uint64   `json:"phoneNumber"`
	UserName           string   `json:"userName"`
	HeadViewPath       string   `json:"headViewPath"`
	BackgroundWallPath string   `json:"backgroundWallPath"`
	FocusCount         uint64   `json:"focusCount"`
	FansCount          uint64   `json:"fansCount"`
	Relation           uint64   `json:"relation"`
	SellImgPathList    []string `json:"sellImgPathList"`
	SellImgPriceList   []uint   `json:"sellImgPriceList"`
}

// 展厅---用户基本信息与最近上架的一张图片
type GetDisplayInfoResult struct {
	Result bool          `json:"result" xml:"result"`
	Note   string        `json:"note" xml:"note"`
	Data   []DisplayInfo `json:"data"`
}

// 用户所有的关注人的信息
type GetUserAllFocusInfoResult struct {
	Result bool                     `json:"result" xml:"result"`
	Note   string                   `json:"note" xml:"note"`
	Data   []UserAllFocusOrFansInfo `json:"data"`
}

// 用户所有的粉丝信息
type GetUserAllFansInfoResult struct {
	Result bool                     `json:"result" xml:"result"`
	Note   string                   `json:"note" xml:"note"`
	Data   []UserAllFocusOrFansInfo `json:"data"`
}

// 安全交易---返回图片基本信息结果(txt...)
type PurchaseForSecureTraResult struct {
	Result      bool             `json:"result" xml:"result"`
	Note        string           `json:"note" xml:"note"`
	PurchaseUid uint64           `json:"purchaseUid"`
	Data        SecureTraImgInfo `json:"data"`
}

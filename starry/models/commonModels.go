package models

//用户uid集合
type Uid struct {
	Uid uint64 `json:"uid"`
}

//对应用户的所有上架照片名称集合
type SellImgNameByUid struct {
	SellImgName string `json:"sellImgName"`
}

//对应用户的所有上架照片价格集合
type SellImgPriceByUid struct {
	SellImgPrice uint `json:"sellImgPrice"`
}

//卖家用户名集合
type SellerName struct {
	SellerName string `json:"sellerName"`
}

//获取所有用户的uid
type AllUid struct {
	Result bool   `json:"result" xml:"result"`
	Note   string `json:"note" xml:"note"`
	Data   []Uid  `json:"data" xml:"data"`
}

//展厅信息结构体
type MainViewInfo struct {
	Uid          uint64
	PhoneNumber  uint64
	UserName     string
	HeadView     string
	SellImgPrice []uint
	SellImgName  []string
}

// 购买记录
type MyPurchaseRecordsInfo struct {
	ImgName    string `json:"imgPath"`
	TradePrice string `json:"imgPrice"`
	CreatedAt  string `json:"purchaseDate"`
}

// 出售记录
type MySaleRecordsData struct {
	ImgName    string `json:"imgPath"`
	TradePrice string `json:"imgPrice"`
	CreatedAt  string `json:"saleDate"`
}

// 用户基本信息,及登录者是否关注该用户---用于展厅界面点击其它用户
type UserInfoAndFocusRelation struct {
	Uid                uint64 `json:"uid"`
	PhoneNumber        uint64 `json:"phoneNumber"`
	UserName           string `json:"userName"`
	HeadViewPath       string `json:"headViewPath"`
	BackgroundWallPath string `json:"backgroundWallPath"`
	FocusCount         uint64 `json:"focusCount"`
	FansCount          uint64 `json:"fansCount"`
	Relation           uint64 `json:"relation"`
}

// 展厅信息,包括关注数,只显示一张最新的上架图片
type DisplayInfo struct {
	Uid          uint64 `json:"uid"`
	UserName     string `json:"userName"`
	HeadViewPath string `json:"headViewPath"`
	FocusCount   uint64 `json:"focusCount"`
	FansCount    uint64 `json:"fansCount"`
	ImgPath      string `json:"imgPath"`
	Relation     uint64 `json:"relation"`
}

// 个人信息,包括粉丝数等
type MyInfo struct {
	UserName           string `json:"userName"`
	UserPhone          uint64 `json:"userPhone"`
	Points             uint64 `json:"points"`
	HeadViewPath       string `json:"headViewPath"`
	BackgroundWallPath string `json:"backgroundWallPath"`
	Sex                string `json:"sex"`
	Birth              string `json:"birth"`
	FocusCount         uint64 `json:"focusCount"`
	FansCount          uint64 `json:"fansCount"`
}

// 用户关注的所有人,或者其所有粉丝的信息
type UserAllFocusOrFansInfo struct {
	Uid          uint64 `json:"uid"`
	HeadViewPath string `json:"headViewPath"`
	UserName     string `json:"userName"`
	Relation     uint64 `json:"relation"`
}

// 某张上架图片的txt路径
type SecureTraImgInfo struct {
	Uid      uint64 `json:"sellerUid"`
	ImgPrice uint64 `json:"imgPrice"`
	TxtPath  string `json:"txtPath"`
}

type Chengdu struct {
	Uid            uint64 `json:"uid"  `
	PhoneNumber    uint64 `json:"phoneNumber" `
	UserName       string `json:"username" `
	Salt           string `json:"salt" `
	Key            string `json:"key"  `
	Points         uint64 `json:"points" `
	HeadView       string `json:"headView" `
	BackgroundWall string `json:"backgroundWall" `
	Sex            string `json:"sex" `
	Birth          string `json:"birth" `
}

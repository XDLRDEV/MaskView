package models

import (
	"time"
)

// 用户信息
type User struct {
	Uid            uint64 `json:"uid"  gorm:"column:uid; primary_key; AUTO_INCREMENT"`
	PhoneNumber    uint64 `json:"phoneNumber" gorm:"column:phone_number; index"`
	UserName       string `json:"username" gorm:"column:username; type:varchar(25); not null"`
	Salt           string `json:"salt" gorm:"type:char(64); not null"`
	Key            string `json:"key"  gorm:"type:char(64); not null"`
	Points         uint64 `json:"points" gorm:"type:int(16); default:0"` // 用户积分
	HeadView       string `json:"headView" gorm:"column:head_view; type:varchar(255)"`
	BackgroundWall string `json:"backgroundWall" gorm:"column:background_wall; type:varchar(255)"`
	Sex            string `json:"sex" gorm:"column:sex; type:varchar(10)"`
	Birth          string `json:"birth" gorm:"column:birth; type:varchar(64)"`
}

type Data struct {
	Uid        uint64 `json:"uid"  gorm:"index"`
	DataNumber uint64 `json:"dataNumber" gorm:"column:data_number; primary_key"`
	DataType   string `json:"dataType" gorm:"column:data_type; type:varchar(16); not null; DEFAULT:'picture'"`
	DataHash   string `json:"dataHash" gorm:"column:data_hash; type:char(2550)"`
	ZeroInfo   string `json:"zeroInfo" gorm:"column:zero_info; type:varchar(1024); not null"`
	RobustInfo string `json:"robustInfo" gorm:"column:robust_info;not null"`
	TxId       string `json:"txId" gorm:"column:tx_id; type:char(66); not null"`
}

type Copyright struct {
	ID         uint64 `json:"ID" gorm:"column:id; AUTO_INCREMENT; primary_key"`
	RobustInfo string `json:"robustInfo" gorm:"column:robust_info; type:varchar(64); not null"`
	ScreenShot string `json:"screenShot" gorm:"column:screen_shot; type:varchar(64); not null;DEFAULT:'picture'"`
	Url        string `json:"url" gorm:"column:url; type:char(128); not null"`
	TxId       string `json:"txId" gorm:"column:tx_id; type:char(66); not null"`
	Code       string `json:"code" gorm:"column:code; not null; DEFAULT:0"`
}

type AuthRight struct {
	Uid            uint64 `json:"uid"  gorm:"primary_key"`
	DataNumber     uint64 `json:"dataNumber" gorm:"column:data_number; not null; AUTO_INCREMENT"`
	DataHash       string `json:"dataHash" gorm:"column:data_hash; type:char(64); not null; index:data_hash"`
	ZeroInfo       string `json:"zeroInfo" gorm:"column:zero_info; type:varchar(64); not null"`
	RobustInfo     string `json:"robustInfo" gorm:"column:robust_info; type:varchar(64); not null"`
	ConfirmMessage string `json:"confirmMessage" gorm:"column:confirm_message; type:varchar(64); not null;DEFAULT:'侵权数据已确权公证'"`
	ConfirmTime    string `json:"confirmTime" gorm:"column:confirm_time; type:varchar(64); not null"`
	TxId           string `json:"txId" gorm:"column:tx_id; type:char(66); not null"`
}

type Confirm struct {
	Uid            uint64 `json:"uid"  gorm:"primary_key"`
	DataNumber     uint64 `json:"dataNumber" gorm:"column:data_number; not null; AUTO_INCREMENT"`
	DataType       string `json:"dataType" gorm:"column:data_type; type:varchar(16); not null;DEFAULT:'picture'"`
	DataHash       string `json:"dataHash" gorm:"column:data_hash; type:char(64); not null; index:data_hash"`
	ZeroInfo       string `json:"zeroInfo" gorm:"column:zero_info; type:varchar(64); not null"`
	RobustInfo     string `json:"robustInfo" gorm:"column:robust_info; type:varchar(64); not null"`
	ScreenShot     string `json:"screenShot" gorm:"column:screen_shot; type:varchar(64); not null"`
	Url            string `json:"url" gorm:"column:url; type:char(128); not null"`
	ConfirmMessage string `json:"confirmMessage" gorm:"column:confirm_message; type:varchar(64); not null"`
	ConfirmTime    string `json:"confirmTime" gorm:"column:confirm_time; type:varchar(64); not null"`
	TxId           string `json:"txId" gorm:"column:tx_id; type:char(66); not null"`
}

type Trade struct {
	From      uint64    `json:"from" gorm:"not null; index"`
	To        uint64    `json:"to" gorm:"not null; index"`
	Value     uint64    `json:"value" gorm:"not null"`
	Mold      uint64    `json:"mold" gorm:"not null"`
	DataHash  string    `json:"dataHash" gorm:"column:data_hash; type:char(64); not null; index:data_hash"`
	TxId      string    `json:"txId" gorm:"column:tx_id; type:char(66); not null"`
	CreatedAt time.Time `json:"created_at" gorm:"column:created_at; type:datetime; oncreated: CURRENT_TIMESTAMP"`
}

// 注册验证码
type RegisterCode struct {
	PhoneNumber uint64 `json:"phoneNumber" gorm:"column:phone_number; not null; index"`
	Code        string `json:"code" gorm:"column:code; not null"`
	ExpireTime  int64  `json:"expireTime" gorm:"column:expire_time; not null"`
}

// 登录,修改密码等非注册操作验证码
type OthersCode struct {
	PhoneNumber uint64 `json:"phoneNumber" gorm:"column:phone_number; not null; index"`
	Code        string `json:"code" gorm:"column:code; not null"`
	ExpireTime  int64  `json:"expireTime" gorm:"column:expire_time; not null"`
}

type TradeMsg struct {
	Id         uint64 `json:"id" gorm:"column:id; primary_key;AUTO_INCREMENT"`
	From       string `json:"from" gorm:"not null; index:idx_phone"`
	To         string `json:"to" gorm:"not null; index:idx_phone"`
	Value      uint64 `json:"value" gorm:"not null"`
	Code       string `json:"vcode" gorm:"not null"`
	ExpireTime int64  `json:"expireTime" gorm:"column:expire_time; not null"`
}

// 确权信息
type ImageKey struct {
	Id          uint64    `json:"id" gorm:"column:id; primary_key;AUTO_INCREMENT"`
	Uid         uint64    `json:"uid" gorm:"column:uid; not null; index"`
	PhoneNumber uint64    `json:"phoneNumber" gorm:"column:phone_number; not null; index"`
	ImageName   string    `json:"image_name" gorm:"image_name; not null"`
	ImgKey      string    `json:"img_key" gorm:"column:img_key; type:varchar(2550); not null"`
	CreatedAt   time.Time `json:"created_at" gorm:"column:created_at; type:datetime; oncreated: CURRENT_TIMESTAMP"`
}

// 上架信息
type SellMsg struct {
	ID            uint64    `json:"id" gorm:"column:id; primary_key; AUTO_INCREMENT"`
	Uid           uint64    `json:"uid" gorm:"column:uid; not null; "`
	ImgName       string    `json:"imgName" gorm:"column:img_name; type:varchar(255); not null"`
	ThumbnailPath string    `json:"thumbnailPath" gorm:"column:thumbnail_path; type:varchar(255)"`
	ImageTopic    string    `json:"imgTopic" gorm:"column:img_topic; type:varchar(20); "`
	ImgPrice      uint64    `json:"imgPrice" gorm:"column:img_price; not null"`
	FiscoKey      string    `json:"fiscoKey" gorm:"column:fisco_key; type:varchar(255)"`
	TxtPath       string    `json:"txtPath" gorm:"column:txt_path; type:varchar(255)"`
	CreatedAt     time.Time `json:"sellDate" gorm:"column:created_at; type:datetime; oncreated:CURRENT_TIMESTAMP"`
}

// 交易记录
type TradeHistory struct {
	Id          uint64    `json:"id" gorm:"column:id; primary_key;AUTO_INCREMENT"`
	PurchaserId uint64    `json:"purchaserId" gorm:"column:purchaser_id; not null"`
	SellerId    uint64    `json:"sellerId" gorm:"column:seller_id; not null"`
	ImageName   string    `json:"imgName" gorm:"column:img_name; type:varchar(255); not null"`
	TradePrice  uint64    `json:"tradePrice" gorm:"column:trade_price; not null"`
	DataHash    string    `json:"dataHash" gorm:"column:data_hash;type:char(2550) not null"`
	CreatedAt   time.Time `json:"tradeDate" gorm:"column:created_at; type:datetime; oncreated:CURRENT_TIMESTAMP"`
}

// 购物车
type ShoppingCar struct {
	Id          uint64 `json:"id" gorm:"column:id; primary_key;AUTO_INCREMENT"`
	PurchaserId uint64 `json:"purchaserId" gorm:"column:purchaser_id; not null"`
	SellerId    uint64 `json:"sellerId" gorm:"column:seller_id; not null"`
	ImgPath     string `json:"imgPath" gorm:"column:img_path; type:varchar(255); not null"`
}

// 关注
type Follower struct {
	Id        uint64    `json:"id" gorm:"column:id; primary_key;AUTO_INCREMENT"`
	UserId    uint64    `json:"userId" gorm:"column:user_id; not null"`
	FocusUid  uint64    `json:"focusUid" gorm:"column:focus_uid; not null"`
	CreatedAt time.Time `json:"date" gorm:"column:created_at; type:datetime; oncreated:CURRENT_TIMESTAMP"`
}

// 粉丝
type Fans struct {
	Id      uint64 `json:"id" gorm:"column:id; primary_key;AUTO_INCREMENT"`
	UserId  uint64 `json:"userId" gorm:"column:user_id; not null"`
	FansUid uint64 `json:"focusUid" gorm:"column:fans_uid; not null"`
}

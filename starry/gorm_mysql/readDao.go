package gorm_mysql

import (
	"Starry/api/debug"
	. "Starry/models"
	"fmt"
	"github.com/jinzhu/gorm"
)

func GetUserSecret(uid *uint64) *User {
	m := new(User)
	err := db.Where("uid = ?", uid).First(m).Error
	debug.PrintErr(err)
	return m
}

func GetUserSecretByPhoneNumber(phoneNumber *uint64) *User {
	m := new(User)
	err := db.Where("phone_number = ?", phoneNumber).First(m).Error
	debug.PrintErr(err)
	return m
}

func GetUserIdByPhoneNumber(phoneNumber *uint64) uint64 {
	m := new(User)
	err := db.Where("phone_number = ?", phoneNumber).First(m).Error
	debug.PrintErr(err)
	return m.Uid
}

func GetUserIdByUserName(userName string) uint64 {
	m := new(User)
	err := db.Where("username = ?", userName).First(m).Error
	debug.PrintErr(err)
	return m.Uid
}

func GetUserPhoneByUserId(uid uint64) uint64 {
	m := new(User)
	err := db.Where("uid = ?", uid).First(m).Error
	debug.PrintErr(err)
	return m.PhoneNumber
}

func GetUserInfoByUserName(userName string) *User {
	m := new(User)
	err := db.Where("username = ?", userName).First(m).Error
	debug.PrintErr(err)
	return m
}

func GetUserDataByRobustInfo(uid *uint64, robustInfo string) (*Data, uint) {
	var m = Data{}
	if err := db.Where("uid = ? AND robust_info = ? ", uid, robustInfo).First(&m).Error; gorm.IsRecordNotFoundError(err) {
		fmt.Println("IsRecordNotFoundError")
		return nil, 0
	} else if err != nil {
		fmt.Println(err)
		return nil, 0
	} else {
		fmt.Println("打印m")
		fmt.Println(m)
		return &m, 1
	}
}

func GetUserList(uid *uint64) (*Data, uint) {
	var m = Data{}
	var count uint
	if err := db.Where("uid = ?", uid).Find(&m).Count(&count).Error; gorm.IsRecordNotFoundError(err) {
		return nil, 0
	} else if err != nil {
		fmt.Println(err)
		return nil, 0
	} else {
		return &m, count
	}
}

func GetUserAuthRight(uid *uint64) (*AuthRight, uint) {
	var m = AuthRight{}
	var count uint
	if err := db.Where("uid = ?", uid).Find(&m).Count(&count).Error; gorm.IsRecordNotFoundError(err) {
		return nil, 0
	} else if err != nil {
		fmt.Println(err)
		return nil, 0
	} else {
		return &m, count
	}
}

func GetUserConfirm(uid *uint64) (*Confirm, uint) {
	var m = Confirm{}
	var count uint
	if err := db.Where("uid = ?", uid).Find(&m).Count(&count).Error; gorm.IsRecordNotFoundError(err) {
		return nil, 0
	} else if err != nil {
		fmt.Println(err)
		return nil, 0
	} else {
		return &m, count
	}
}

func GetData(dataHash *string) (*Data, uint) {
	var m = Data{}
	var count uint
	if err := db.Where("data_hash = ?", dataHash).Count(&count).First(&m).Error; gorm.IsRecordNotFoundError(err) {
		return nil, 0
	} else if err != nil {
		panic(err)
	} else {
		return &m, 0
	}
}

func GetDataAuthRight(dataHash *string) (*AuthRight, uint) {
	var m = AuthRight{}
	var count uint
	if err := db.Where("data_hash = ?", dataHash).Count(&count).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		return nil, 0
	} else if err != nil {
		fmt.Println(err)
		return nil, 0
	} else {
		return &m, 0
	}
}

func GetDataConfirm(dataHash *string) (*Confirm, uint) {
	var m = Confirm{}
	var count uint
	if err := db.Where("data_hash = ?", dataHash).Count(&count).First(&m).Error; gorm.IsRecordNotFoundError(err) {
		return nil, 0
	} else if err != nil {
		fmt.Println(err)
		return nil, 0
	} else {
		return &m, 0
	}
}

func GetRegisterCodeMsg(phoneNumber *uint64) *RegisterCode {
	m := new(RegisterCode)
	err := db.Where("phone_number = ?", phoneNumber).First(&m).Error
	debug.PrintErr(err)
	return m
}

func GetOthersCodeMsg(phoneNumber *uint64) *OthersCode {
	m := new(OthersCode)
	err := db.Where("phone_number = ?", phoneNumber).First(&m).Error
	debug.PrintErr(err)
	return m
}

func GetTradeMsgByPhoneNumber(from, to string) *TradeMsg {
	m := new(TradeMsg)
	err := db.Model(&TradeMsg{}).Where("`from` = ? and `to` = ?", from, to).Last(&m).Error
	debug.PrintErr(err)
	return m
}

func GetTradeHistory(uid *uint64) *[]Trade {
	var res []Trade
	db.Model(&Trade{}).Where("`from` = ?", *uid).Find(&res)
	return &res
}

func GetImageKeyList(uid *uint64) *[]ImageKey {
	var res []ImageKey
	db.Model(&Trade{}).Where("uid = ?", *uid).Find(&res)
	return &res
}

/*func FindOthersCodeMsg(phoneNumber uint64) bool {
	return !db.Where("phone_number = ?", phoneNumber).First(&OthersCode{}).RecordNotFound()
}*/

// 获取上架信息
func GetSellInfoList(uid *uint64) *[]SellMsg {
	var res []SellMsg
	db.Model(&SellMsg{}).Where("uid = ?", *uid).Find(&res)
	return &res
}

// 获取一个用户的某张图片上架信息
func GetOneSellInfo(uid uint64, imgPath string) *SellMsg {
	var res SellMsg
	db.Model(&SellMsg{}).Where("uid = ? and img_name = ?", uid, imgPath).Find(&res)
	return &res
}

// 获取展厅信息---用户所有uid
func GetAllUid() []uint64 {
	var users []User
	var uid []Uid
	rows, err := db.Model(&users).Select("uid").Scan(&uid).Rows()
	if err != nil {
		fmt.Println(err)
	}
	var intSlice []uint64
	for rows.Next() {
		var singleUid Uid
		err = rows.Scan(&singleUid.Uid)
		if err != nil {
			fmt.Println(err)
		}
		intSlice = append(intSlice, singleUid.Uid)
	}
	return intSlice
}

// 获取展厅信息---对应uid的所有上架照片名称
func GetSellImgNameByUid(uid uint64) []string {
	var sellMsg []SellMsg
	var sellImgName []SellImgNameByUid
	rows, err := db.Model(&sellMsg).Where("uid = ?", uid).Select("thumbnail_path").Order("id desc").Scan(&sellImgName).Rows()
	if err != nil {
		fmt.Println(err)
	}
	var stringSlice []string
	for rows.Next() {
		var singleSellImgName SellImgNameByUid
		err = rows.Scan(&singleSellImgName.SellImgName)
		//打印数据库语句，先省略
		if err != nil {
			fmt.Println(err)
		}
		stringSlice = append(stringSlice, singleSellImgName.SellImgName)
	}
	return stringSlice
}

// 获取展厅信息---对应uid的所有价格
func GetSellImgPriceByUid(uid uint64) []uint {
	var sellMsg []SellMsg
	var sellImgPrice []SellImgPriceByUid
	rows, err := db.Model(&sellMsg).Where("uid = ?", uid).Select("img_price").Order("id desc").Scan(&sellImgPrice).Rows()
	if err != nil {
		fmt.Println(err)
	}
	var uintSlice []uint
	for rows.Next() {
		var singleSellImgPrice SellImgPriceByUid
		err = rows.Scan(&singleSellImgPrice.SellImgPrice)
		uintSlice = append(uintSlice, singleSellImgPrice.SellImgPrice)
	}
	return uintSlice
}

// 展厅/购物车---根据图片名称获取图片信息
func GetOneSellImgInfoByImgName(thumbnailPath string) (*SellMsg, bool) {
	m := new(SellMsg)
	if db.Where("thumbnail_path = ?", thumbnailPath).First(m).RecordNotFound() {
		return nil, false
	} else {
		db.Where("thumbnail_path = ?", thumbnailPath).First(m)
		return m, true
	}
}

// 购物车---登陆ID购物车里所有的卖家用户名
func GetAllSellerNameByPurchaserId(uid uint64) []string {
	var res []SellerName
	rows, err := db.Raw("select distinct b.username as seller_name from shopping_car a "+
		"left join user b on a.seller_id = b.uid "+
		"where a.purchaser_id = ?", uid).Scan(&res).Rows()
	//rows, err := db.Model(&ShoppingCar{}).Where("purchaser_id = ?", uid).Select("distinct seller_name").Scan(&res).Rows()
	if err != nil {
		fmt.Println(err)
	}
	var stringSlice []string
	for rows.Next() {
		var singleSellerName SellerName
		err = rows.Scan(&singleSellerName.SellerName)
		if err != nil {
			fmt.Println(err)
		}
		stringSlice = append(stringSlice, singleSellerName.SellerName)
	}
	return stringSlice
}

// 购物车--根据卖方用户名检索用户加入的所有商品
func GetGoodsInfoBySellerName(purchaseId uint64, sellName string) *[]GoodsInfo {
	var res []GoodsInfo
	db.Raw("select a.img_path as img_path, b.img_topic as img_topic, b.img_price as img_price "+
		"from shopping_car a "+
		"left join sell_msg b on a.seller_id = b.uid and a.img_path = b.thumbnail_path "+
		"left join user c on a.seller_id = c.uid "+
		"where a.purchaser_id = ? and c.username = ? ", purchaseId, sellName).Scan(&res)
	return &res
}

// 我的购买记录
func GetPurchaseRecords(uid uint64) (*[]MyPurchaseRecordsInfo, bool) {
	var res []MyPurchaseRecordsInfo
	if db.Where("purchaser_id = ?", uid).First(&TradeHistory{}).RecordNotFound() {
		return nil, false
	} else {
		db.Model(&TradeHistory{}).Where("purchaser_id = ?", uid).
			Select("img_name, trade_price, created_at").Order("created_at desc").Scan(&res)
		return &res, true
	}
}

// 我的出售记录
func GetSaleRecords(uid uint64) (*[]MySaleRecordsData, bool) {
	var res []MySaleRecordsData
	if db.Where("seller_id = ?", uid).First(&TradeHistory{}).RecordNotFound() {
		return nil, false
	} else {
		db.Model(&TradeHistory{}).Where("seller_id = ?", uid).
			Select("img_name, trade_price, created_at").Order("created_at desc").Scan(&res)
		return &res, true
	}
}

// 用户基本信息及关注人数,粉丝人数---用于展厅界面查看他人信息(未登录)
/*func GetUserInfoAndFansNotLogin(herUid uint64) *UserInfoAndFocusRelation {
	m := new(UserInfoAndFocusRelation)
	db.Raw("select a.uid as uid, a.phone_number as phone_number, a.username as user_name, "+
		"a.head_view as head_view_path, a.background_wall as background_wall_path, "+
		" count(b.focus_uid) as focus_count, "+
		"(select count(user_id) from follower where focus_uid = ?) as fans_count "+
		"from user a left join follower b on a.uid = b.user_id where a.uid = ?", herUid, herUid).Scan(&m)
	return m
}*/

// TODO:川大---注入
func GetUserInfoAndFansNotLogin(herUid string) *UserInfoAndFocusRelation {
	m := new(UserInfoAndFocusRelation)
	db.Raw("select a.uid as uid, a.phone_number as phone_number, a.username as user_name, "+
		"a.head_view as head_view_path, a.background_wall as background_wall_path, "+
		" count(b.focus_uid) as focus_count, "+
		"(select count(user_id) from follower where focus_uid = ?) as fans_count "+
		"from user a left join follower b on a.uid = b.user_id where a.uid = ?", herUid, herUid).Scan(&m)
	return m
}

// 用户基本信息及关注人数,粉丝人数及关注关系---用于展厅界面查看他人信息(登录后)
func GetUserInfoAndFansAfterLogin(myUid uint64, herUid uint64) *UserInfoAndFocusRelation {
	m := new(UserInfoAndFocusRelation)
	db.Raw("select a.uid as uid, a.phone_number as phone_number, a.username as user_name, "+
		"a.head_view as head_view_path, a.background_wall as background_wall_path, "+
		"count(b.focus_uid) as focus_count, "+
		"(select count(user_id) from follower where focus_uid = ?) as fans_count, "+
		"(select count(*) from follower where user_id = ? and focus_uid = ?) as relation "+
		"from user a left join follower b on a.uid = b.user_id where a.uid = ?", herUid, myUid, herUid, herUid).Scan(&m)
	return m
}

// 展厅界面获取展厅信息---Android端(未登录)
func GetDisplayHallNotLogin() *[]DisplayInfo {
	var res []DisplayInfo
	db.Raw("select a.uid as uid, " +
		"a.username as user_name, " +
		"a.head_view as head_view_path, " +
		"count(distinct(b.focus_uid)) as focus_count, " +
		"count(distinct(d.fans_uid)) as fans_count, " +
		"(select thumbnail_path from sell_msg where uid = a.uid order by created_at desc limit 1) as img_path " +
		"from (sell_msg c left join user a on c.uid = a.uid) " +
		"left join follower b on c.uid = b.user_id " +
		"left join fans d on c.uid = d.user_id group by c.uid").Scan(&res)
	return &res
}

// 展厅界面获取展厅信息---(登录后)要返回登陆者与其他用户关注关系且展厅中不显示自己
func GetDisplayHallInfoAfterLogin(myUid uint64) *[]DisplayInfo {
	var res []DisplayInfo
	db.Raw("select a.uid as uid, a.username as user_name, a.head_view as head_view_path, "+
		"count(distinct(b.focus_uid)) as focus_count, "+
		"count(distinct(d.fans_uid)) as fans_count, "+
		"(select count(*) from follower where user_id = ? and focus_uid = a.uid) as relation, "+
		"(select thumbnail_path from sell_msg where uid = a.uid order by created_at desc limit 1) as img_path "+
		"from (sell_msg c left join user a on c.uid = a.uid) "+
		"left join follower b on c.uid = b.user_id "+
		"left join fans d on c.uid = d.user_id "+
		"where a.uid != ? group by c.uid", myUid, myUid).Scan(&res)
	return &res
}

// 获取个人信息,包括粉丝数
func GetMyInfo(uid *uint64) *MyInfo {
	m := new(MyInfo)
	db.Raw("select username as user_name, phone_number as user_phone, points as points, "+
		"head_view as head_view_path, background_wall as background_wall_path, "+
		"sex as sex, birth as birth, "+
		"(select count(focus_uid) from follower where user_id = ?) as focus_count, "+
		"(select count(fans_uid) from fans where user_id = ?) as fans_count "+
		"from user where uid = ?", uid, uid, uid).Scan(&m)
	return m
}

// 用户关注的所有人的信息---头像,名称,及登陆者与每个用户的关注关系
func GetUserAllFocusInfo(myUid uint64, herUid uint64) *[]UserAllFocusOrFansInfo {
	var res []UserAllFocusOrFansInfo
	db.Raw("select a.focus_uid as uid, b.head_view as head_view_path, b.username as user_name, "+
		"(select count(*) from follower where user_id = ? and focus_uid = a.focus_uid) as relation "+
		"from follower a left join user b on a.focus_uid = b.uid "+
		"where a.user_id = ? order by convert(b.username using gbk) ASC", myUid, herUid).Scan(&res)
	return &res
}

// 用户粉丝的所有人信息---头像,名称,及登陆者与每个用户的关注关系
func GetUserAllFansInfo(myUid uint64, herUid uint64) *[]UserAllFocusOrFansInfo {
	var res []UserAllFocusOrFansInfo
	db.Raw("select a.user_id as uid, b.head_view as head_view_path, b.username as user_name, "+
		"(select count(*) from follower where user_id = ? and focus_uid = a.user_id) as relation "+
		"from follower a left join user b on a.user_id = b.uid "+
		"where a.focus_uid = ? order by convert(b.username using gbk) ASC", myUid, herUid).Scan(&res)
	return &res
}

// 根据imgPath获取某张上架图片的信息
func GetOneSellImgInfoByImgPath(imgPath string) (*SecureTraImgInfo, bool) {
	var res SecureTraImgInfo
	if db.Where("img_name = ?", imgPath).First(&SellMsg{}).RecordNotFound() {
		return nil, false
	} else {
		db.Model(&SellMsg{}).Where("img_name = ?", imgPath).
			Select("uid, img_price, txt_path").Scan(&res)
		return &res, true
	}
}

// 川大---根据userName查信息
func ChengduGetUserInfo(username string) *Chengdu {
	var res Chengdu
	db.Raw("select * from user where uid = " + username).Scan(&res)
	return &res
}

package gorm_mysql

import (
	. "Starry/models"
)

func HasPhoneNumber(uid uint64) bool {
	return !db.Where("uid = ?", uid).First(&User{}).RecordNotFound()
}

func HasPhone(phone uint64) bool {
	return !db.Where("phone_number = ?", phone).First(&User{}).RecordNotFound()
}

func HasSellImgName(imgName string) bool {
	return !db.Where("img_name = ?", imgName).First(&SellMsg{}).RecordNotFound()
}

func CreateUser(user *User) error {
	return db.Create(&user).Error
}
func CreateSellInfo(sellMsg *SellMsg) error {
	return db.Create(&sellMsg).Error
}

func CreateData(data *Data) error {
	return db.Create(&data).Error
}

func CreateCopyright(Copyright *Copyright) (uint64, error) {
	if err := db.Create(&Copyright).Error; err != nil {
		return 0, err
	}
	return Copyright.ID, nil
}

func CreateAuthRight(AuthRight *AuthRight) error {
	return db.Create(&AuthRight).Error
}

func CreateConfirm(Confirm *Confirm) error {
	return db.Create(&Confirm).Error
}

func CreateTrade(Trade *Trade) error {
	return db.Create(&Trade).Error
}

func CreateRegisterCodeMsg(msg *RegisterCode) error {
	return db.Create(&msg).Error
}

func CreateOthersCodeMsg(msg *OthersCode) error {
	return db.Create(&msg).Error
}

func CreateImgKey(imgKey *ImageKey) error {
	return db.Create(&imgKey).Error
}

func CreateTradeMsg(tradeMsg *TradeMsg) error {
	return db.Create(&tradeMsg).Error
}

func CreateTradeHistory(tradeHistory *TradeHistory) error {
	return db.Create(&tradeHistory).Error
}

func CreateShoppingCar(shoppingCar *ShoppingCar) error  {
	return db.Create(&shoppingCar).Error
}

// 关注功能
func CreateFollowerAndFans(follower *Follower, fans *Fans) error{
	return db.Create(&follower).Create(&fans).Error
}

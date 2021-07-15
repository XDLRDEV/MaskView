package gorm_mysql

import (
	"Starry/models"
	"github.com/jinzhu/gorm"
)

func IsHasUserName(userName string) bool {
	m := new(models.User)
	if err := db.Where("username = ?", userName).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		return false
	}
	return true
}

//判断是否有该手机号---用于验证码登陆,先判断该手机号是否注册
func IsHasPhoneNumber(phoneNumber uint64) bool {
	m := new(models.User)
	if err := db.Where("phone_number = ?", phoneNumber).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		return false
	}
	return true
}

func IsHasPhoneAndCode(phoneNumber uint64, code string) bool {
	return !db.Where("phone_number = ? and code = ?", phoneNumber, code).First(&models.OthersCode{}).RecordNotFound()
}

func IsHasOthersCodeMsg(phoneNumber uint64) bool {
	return !db.Where("phone_number = ?", phoneNumber).First(&models.OthersCode{}).RecordNotFound()
}

func IsHasRegisterCodeMsg(phoneNumber uint64) bool {
	return !db.Where("phone_number = ?", phoneNumber).First(&models.RegisterCode{}).RecordNotFound()
}

func IsHasDataShoppingCar(uid uint64, imgPath string) bool {
	m := new(models.ShoppingCar)
	if err := db.Where("purchaser_id = ? and img_path = ?", uid, imgPath).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		return false
	}
	return true
}

//删除上架图片---判断数据库是否有该数据
func IsHasSellImgName(uid uint64, imgPath string) bool {
	m := new(models.SellMsg)
	if err := db.Where("uid = ? and img_name = ?", uid, imgPath).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		return false
	}
	return true
}

// 判断是否已经关注某人
func IsHasFollower(myUid uint64, herUid uint64) bool {
	m := new(models.Follower)
	if err := db.Where("user_id = ? and focus_uid = ?", myUid, herUid).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		return false
	}
	return true
}

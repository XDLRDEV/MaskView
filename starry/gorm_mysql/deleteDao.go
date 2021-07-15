package gorm_mysql

import (
	"Starry/models"
)

// 删除用户信息
func DeleteUserInfoByUid(uid uint64) error {
	return db.Where("uid = ?", uid).Delete(models.User{}).Error
}

//移除购物车---删除购物车的某条信息
func DeleteOneDataToShoppingCar(uid uint64, imgPath string) error {
	return db.Where("purchaser_id = ? and img_path = ?", uid, imgPath).Delete(models.ShoppingCar{}).Error
}

//下架---删除信息
func DeleteSell(uid uint64, imgPath string) error {
	return db.Where("uid = ? and img_name = ?", uid, imgPath).Delete(models.SellMsg{}).Error
}

//下架某张图片后,对应购物车该图片信息全部删除
func DeleteShoppingCartInfoByImgPath(imgPath string) error {
	return db.Where("img_path = ?", imgPath).Delete(models.ShoppingCar{}).Error
}

//购买后,删除购物车相应数据
func DeleteOneShoppingCartInfoAfterPurchase(uid uint64, imgPath string) {
	m := new(models.ShoppingCar)
	//如果购物车有这条信息,直接删除
	if !db.Where("purchaser_id = ? and img_path = ?", uid, imgPath).First(&m).RecordNotFound() {
		db.Where("purchaser_id = ? and img_path = ?", uid, imgPath).Delete(&m)
	}
}

//清空某用户的购物车
func ClearShoppingCart(uid uint64) bool {
	m := new(models.ShoppingCar)
	if !db.Where("purchaser_id = ?", uid).First(&m).RecordNotFound() {
		db.Where("purchaser_id = ?", uid).Delete(models.ShoppingCar{})
		return true
	}
	return false
}

// 取消关注
func CancelFocus(uid uint64, cancelId uint64) bool {
	m := new(models.Follower)
	if !db.Where("user_id = ? and focus_uid = ?", uid, cancelId).First(&m).RecordNotFound() {
		db.Where("user_id = ? and focus_uid = ?", uid, cancelId).Delete(models.Follower{})
		db.Where("user_id = ? and fans_uid = ?", cancelId, uid).Delete(models.Fans{})
		return true
	}
	return false
}

// 安全交易---上架重复图片时,先删除原数据
func DeleteSecureTraSell(uid uint64, imgPath string) error {
	return db.Where("uid = ? and img_name = ?", uid, imgPath).Delete(models.SellMsg{}).Error
}

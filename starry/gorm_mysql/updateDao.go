package gorm_mysql

import (
	"Starry/models"
	"github.com/jinzhu/gorm"
	"time"
)

func UpdateUserInfo(user models.User) error {
	if err := db.Model(&models.User{}).Updates(user).Error; err != nil {
		return err
	}
	return nil
}

func UpdateCopyrightCode(id *uint64) error {
	var copyright models.Copyright
	//db.Model(&copyright).Where("id = ?", id).Update("code", 1)
	if err := db.Model(&copyright).Where("id = ?", id).Update("code", 1).Error; err != nil {
		return err
	}
	return nil
}

func UpdateRegisterCodeMsg(phoneNumber *uint64, code string) error {
	if err := db.Model(&models.RegisterCode{}).Where("phone_number = ?", phoneNumber).Update("code", code).Update("ExpireTime", time.Now().Unix()).Error; err != nil {
		return err
	}
	return nil
}

func UpdateOthersCodeMsg(phoneNumber *uint64, code string) error {
	if err := db.Model(&models.OthersCode{}).Where("phone_number = ?", phoneNumber).Update("code", code).Update("ExpireTime", time.Now().Unix()).Error; err != nil {
		return err
	}
	return nil
}

func UpdateUser(user *models.User) error {
	if err := db.Model(&models.User{}).Where("uid = ?", user.Uid).Update("points", user.Points).Error; err != nil {
		return err
	}
	return nil
}

//更改上架图片价格
func UpdateSellMsgImgPrice(uid uint64, imgPath string, newImgPrice int) error {
	if err := db.Model(&models.SellMsg{}).Where("uid = ? and img_name = ?", uid, imgPath).Update("img_price", newImgPrice).Error;
		err != nil {
		return err
	}
	return nil
}

//更新上架图片信息(针对已经上架过的图片,进行信息更新)
func UpdateSellMsg(uid uint64, imgPath string, imgPrice uint64, imgTopic string) error {
	if err := db.Model(&models.SellMsg{}).Where("uid = ? and img_name = ?", uid, imgPath).
		UpdateColumns(models.SellMsg{
			ImageTopic: imgTopic,
			ImgPrice:   imgPrice,
			CreatedAt:  time.Now(),
		}).Error; err != nil {
		return err
	}
	return nil
}

//更改头像
func UpdateUserHeadView(uid uint64, headViewPath string) error {
	if err := db.Model(&models.User{}).Where("uid = ?", uid).
		Update("head_view", headViewPath).Error;
		err != nil {
		return err
	}
	return nil
}

// 更改背景图
func UpdateUserBackgroundWall(uid uint64, backgroundWallPath string) error {
	if err := db.Model(&models.User{}).Where("uid = ?", uid).
		Update("background_wall", backgroundWallPath).Error;
		err != nil {
		return err
	}
	return nil
}

//更改昵称
func UpdateUserName(uid uint64, userName string) bool {
	m := new(models.User)
	if err := db.Where("username = ?", userName).First(&m).Error;
		gorm.IsRecordNotFoundError(err) {
		db.Model(&models.User{}).Where("uid = ?", uid).Update("username", userName)
		return true
	}
	return false

}

//更改性别
func UpdateUserSex(uid uint64, sex string) error {
	if err := db.Model(&models.User{}).Where("uid = ?", uid).Update("sex", sex).Error;
		err != nil {
		return err
	}
	return nil
}

//更改出生日期
func UpdateUserBirth(uid uint64, birth string) error {
	if err := db.Model(&models.User{}).Where("uid = ?", uid).Update("birth", birth).Error;
		err != nil {
		return err
	}
	return nil
}

func Update(uid uint64, originPath string, thumbPath string) error {
	if err := db.Model(&models.SellMsg{}).Where("uid = ? and img_name = ?", uid, originPath).
		Update("thumbnail_path", thumbPath).Error; err != nil {
		return err
	}
	return nil
}

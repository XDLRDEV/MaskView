package gorm_mysql

import "fmt"

func CountTable(tableName string, ch chan uint) {
	var count uint
	if err := db.Table(tableName).Count(&count).Error; err != nil {
		fmt.Println(err)
	} else {
		ch <- count
	}
}

func CountData() (uint64, error) {
	var count uint64
	if err := db.Table("data").Count(&count).Error; err != nil {
		return 0, err
	} else {
		return count, nil
	}
}

func CountUserConfirm(uid uint64, ch chan uint) {
	var count uint
	if err := db.Table("confirm").Where("uid = ?", uid).Count(&count).Error; err != nil {
		fmt.Println(err)
	} else {
		ch <- count
	}
}

func CountDataConfirm(uid uint64, ch chan uint) {
	var count uint
	if err := db.Table("confirm").Where("data_hash = ?", uid).Count(&count).Error; err != nil {
		fmt.Println(err)
	} else {
		ch <- count
	}
}

func CountMsg()(phoneNumber uint64){
	var count uint64
	if err := db.Table("message").Where("phone_number = ?", phoneNumber).Count(&count).Error; err != nil {
		println(err)
	}
	return count
}

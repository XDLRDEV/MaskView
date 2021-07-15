package main

import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jinzhu/gorm"
)

var db *gorm.DB
var err error

func check(err error){     //因为要多次检查错误，所以干脆自己建立一个函数。
	if err!=nil{
		fmt.Println(err)
	}

}


func main() {

	db, err = gorm.Open("mysql", "test:aA!123456@tcp(49.234.37.254:3306)/starry?charset=utf8&parseTime=True&loc=Local")
	check(err)

	db.SingularTable(true)

	type User struct {
		Uid         uint64 `json:"uid"  gorm:"column:uid; primary_key; AUTO_INCREMENT"`
		PhoneNumber uint64 `json:"phoneNumber" gorm:"column:phone_number; index"`
		UserName    string `json:"username" gorm:"column:username; type:varchar(25); not null"`
		Salt        string `json:"salt" gorm:"type:char(64); not null"`
		Key         string `json:"key"  gorm:"type:char(64); not null"`
		Points      int64  `json:"points" gorm:"type:int(16); default:0"` // 用户积分
	}

	type Uid struct {
		Uid         uint64 `json:"uid"  gorm:"column:uid; primary_key; AUTO_INCREMENT"`
	}

	var users []User
	var res []Uid

	//rows, err := db.Find(&users).Rows()
	rows, err := db.Model(&users).Select("uid").Scan(&res).Rows()

	check(err)
	var intSlice []uint64
	for rows.Next() {
		var qwe Uid
		err = rows.Scan(&qwe.Uid)
		check(err)
		var intq uint64
		intq = qwe.Uid
		intSlice = append(intSlice,intq)

	}
	fmt.Println(intSlice)
	fmt.Println(intSlice[1])

}

package gorm_mysql

import (
	"Starry/api/debug"
	"Starry/models"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jinzhu/gorm"
)

var db *gorm.DB // 全局变量用 =

func Init() {
	var err error
	db, err = gorm.Open("mysql", "用户名:密码@tcp(IP:3306)/starry?charset=utf8&parseTime=True&loc=Local")
	debug.PanicErr(err)
	db.DB().SetMaxIdleConns(100)
	db.DB().SetMaxOpenConns(1000)
	db.LogMode(true)       // 启用Logger，显示详细日志
	db.SingularTable(true) // 全局禁用表名复数
	fmt.Println("mysql数据库已连接，检查表结构中...")
	if !db.HasTable(&models.User{}) {
		fmt.Println("表:user不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.User{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.Data{}) {
		fmt.Println("表:data不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.Data{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.Copyright{}) {
		fmt.Println("表:Copyright不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.Copyright{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.AuthRight{}) {
		fmt.Println("表:AuthRight不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.AuthRight{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.Confirm{}) {
		fmt.Println("表:Confirm不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.Confirm{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.Trade{}) {
		fmt.Println("表:Trade不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.Trade{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.RegisterCode{}) {
		fmt.Println("表:Msg，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.RegisterCode{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.OthersCode{}) {
		fmt.Println("表:ResetMsg不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.OthersCode{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.TradeMsg{}) {
		fmt.Println("表:TradeMsg，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.TradeMsg{}).Error; err != nil {
			panic(err)
		}
	}
	if !db.HasTable(&models.ImageKey{}) {
		fmt.Println("表:ImageKey不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.ImageKey{}).Error; err != nil {
			panic(err)
		}
	}

	/*********************/
	if !db.HasTable(&models.Follower{}) {
		fmt.Println("表:Follower不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.Follower{}).Error; err != nil {
			panic(err)
		}
	}

	if !db.HasTable(&models.Fans{}) {
		fmt.Println("表:Fans不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.Fans{}).Error; err != nil {
			panic(err)
		}
	}

	if !db.HasTable(&models.TradeHistory{}) {
		fmt.Println("表:TradeHistory不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.TradeHistory{}).Error; err != nil {
			panic(err)
		}
	}

	if !db.HasTable(&models.ShoppingCar{}) {
		fmt.Println("表:ShoppingCar不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.ShoppingCar{}).Error; err != nil {
			panic(err)
		}
	}

	if !db.HasTable(&models.SellMsg{}) {
		fmt.Println("表:SellMsg不存在，正在创建中")
		if err := db.Set("gorm:table_options", "ENGINE=InnoDB DEFAULT CHARSET=utf8").CreateTable(&models.SellMsg{}).Error; err != nil {
			panic(err)
		}
	}

	//defer db.Close()
}

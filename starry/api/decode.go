package api

import (
	"Starry/gorm_mysql"
	"Starry/models"
	"fmt"
	"net"
	"strconv"
	"strings"
	"time"
)

func DecodeByRobustInfo(RobustInfo string) (*models.AuthRight, error) {
	uidString, feed, err := net.SplitHostPort(RobustInfo)
	if err != nil {
		return nil, err
	}
	fmt.Println(uidString, feed)
	s := strings.Split(feed, "?")
	message := s[0]
	fmt.Println(message)
	uid, err := strconv.ParseUint(uidString, 10, 64)
	if err != nil {
		return nil, err
	}
	d, count := gorm_mysql.GetUserDataByRobustInfo(&uid, message)
	fmt.Println("打印d")
	fmt.Println(d)
	if count == 0 {
		fmt.Println("没有")
		return nil, nil
	}
	var a = models.AuthRight{
		Uid:            d.Uid,
		DataNumber:     d.DataNumber,
		DataHash:       d.DataHash,
		ZeroInfo:       d.ZeroInfo,
		RobustInfo:     RobustInfo,
		ConfirmMessage: "侵权数据已确权公证",
		ConfirmTime:    time.Now().Format("2006-01-02 15:04:05"),
	}
	fmt.Println(a)
	return &a, nil
}

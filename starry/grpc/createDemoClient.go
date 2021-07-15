package grpc

import (
	"Starry/api/debug"
	"Starry/gorm_mysql"
	"Starry/grpc/proto"
	. "Starry/models"
	"context"
	"fmt"
	"strings"
)

//CreateUserClient 注册
func CreateUserClient(userPhone uint64, date string) bool {
	client := proto.NewCreateUserDemoClient(conn)
	Value, err := client.CreateUserDemo(context.Background(), &proto.CreateUserRequest{UserPhone: userPhone, Date: date})
	if err != nil {
		fmt.Println(err)
		return false
	}
	return Value.Result
}

//ConfirmClient 确权
func ConfirmClient(c *ImageKey) bool {
	client := proto.NewConfirmDemoClient(conn)
	Value, err := client.ConfirmDemo(context.Background(), &proto.ConfirmRequest{
		UserPhone: c.PhoneNumber,
		Tag:       1,
		ImgName:   c.ImageName,
		Key:       c.ImgKey,
	})
	debug.PrintErr(err)
	return Value.Result
}

//TradeClient 购买
func TradeClient(trade *TradeHistory) (*proto.TradeResponse, error) {
	client := proto.NewTradeDemoClient(conn)

	purchasePhone := gorm_mysql.GetUserPhoneByUserId(trade.PurchaserId)
	sellerPhone := gorm_mysql.GetUserPhoneByUserId(trade.SellerId)
	index := strings.Index(trade.ImageName, "con-")

	return client.TradeDemo(context.Background(), &proto.TradeRequest{
		PurchasePhone: purchasePhone,
		SellerPhone:   sellerPhone,
		Price:         trade.TradePrice,
		Tag:           2,
		ImgName:       trade.ImageName[index:],
		Key:           trade.DataHash})
}

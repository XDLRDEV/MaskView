package main

import (
	"encoding/json"
	"fmt"
)

type Text struct {
	Data []P
}

type P struct {
	SellerUid  string
	SellerName string
	GoodsInfo  Q
}

type Q struct {
	FileName     string
	IdentityHash string
	FilePrice    string
	FileTopic    string
	FileType     string
}

func main() {

	jsonString := `{"data": [
        {
            "sellerUid": "4",
            "sellerName": "于兆兴",
            "goodsInfo":{
                    "fileName": "IMG_0812.JPG",
                    "identityHash": "zxzx1",
                    "filePrice": "1",
                    "fileTopic": "xxx",
                    "fileType": ".JPG"
                }
        },
        {
            "sellerUid": "2",
            "sellerName": "aabbcc",
            "goodsInfo":{
                    "fileName": "timg6.jpg",
                    "identityHash": "qweasdzxc6",
                    "filePrice": "10",
                    "fileTopic": "qq",
                    "fileType": ".jpg"
                }
        }
    ]}`

	var res Text
	err := json.Unmarshal([]byte(jsonString), &res)
	if err != nil {
		fmt.Println("err = ", err)
	}
	fmt.Printf("tmp = %+v\n", res)


	qq := res.Data[0].SellerName
	fmt.Println(qq)

}

package main

import (
	"bytes"
	"fmt"
	"github.com/disintegration/imaging"
	"io/ioutil"
)

// 对原图进行缩略
func main() {
	imgData, _ := ioutil.ReadFile("img/4/con-15209294392-t16.jpg")
	buf := bytes.NewBuffer(imgData)
	image, err := imaging.Decode(buf)
	if err != nil {
		fmt.Println(err)
		return
	}
	//生成缩略图，尺寸宽x,高传0表示等比例放缩
	image = imaging.Resize(image, 600, 0, imaging.Lanczos)
	err = imaging.Save(image, "save.jpg")
	if err != nil {
		fmt.Println(err)
	}
}

package main

import (
	"fmt"
	"os"
)

func main() {
	deleteFileByPath()
}

func deleteFileByPath() {
	path := "base64File/1/qqq.txt"
	err := os.Remove(path)
	if err != nil {
		fmt.Println("删除失败")
	} else {
		fmt.Println("删除成功")
	}
}
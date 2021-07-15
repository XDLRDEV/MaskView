package main

import (
	"bufio"
	"encoding/base64"
	"fmt"
	"os"
)

func main() {
	encode("t16.jpg", "D:\\go_project\\starry\\text.txt")
}

func encode(infile string, outfile string) {

	filex, err := os.Open(infile)
	if err != nil {
		fmt.Println("未找到该文件，请确定文件是否存在。")
		return
	}
	aaa := make([]byte, 1024*99999)
	n, err := filex.Read(aaa)
	if err != nil {
		fmt.Println("代码读入失败，请检查权限。")
	}
	get := base64.StdEncoding.EncodeToString(aaa[:n])
	file, err := os.OpenFile(outfile, os.O_WRONLY|os.O_CREATE, 0666)
	if err != nil {
		fmt.Println("写出失败。")
		fmt.Println(err)
		return
	}
	buf := bufio.NewWriter(file)
	_, _ = buf.WriteString(get)
	_ = buf.Flush()
	//fmt.Println("加密成功，导出文件:", os.Args[3])
	defer file.Close()
}

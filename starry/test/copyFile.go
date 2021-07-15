package main

import (
	"io"
	"os"
)

func main() {
	copyFile("test/w1.txt", "w2.txt")
}

func copyFile(srcFile, destFile string) {
	file1, err := os.Open(srcFile)
	if err != nil {
		//return 0, err
		return
	}
	file2, err := os.OpenFile(destFile, os.O_WRONLY|os.O_CREATE, os.ModePerm)
	if err != nil {
		//return 0, err
		return
	}
	defer file1.Close()
	defer file2.Close()

	//return io.Copy(file2,file1)
	_, _ = io.Copy(file2, file1)
}

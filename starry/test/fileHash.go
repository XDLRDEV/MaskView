package main

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"io"
	"os"
)

/**
文件哈希
*/
func getHash(path string) (hash string) {
	file, err := os.Open(path)
	if err == nil {
		h_ob := sha256.New()
		_, err := io.Copy(h_ob, file)
		if err == nil {
			hash := h_ob.Sum(nil)
			hashValue := hex.EncodeToString(hash)
			return hashValue
		} else {
			return "something wrong when use sha256 interface..."
		}
	} else {
		fmt.Printf("failed to open %s\n", path)
	}
	defer file.Close()
	return
}
func main() {
	path := "text.txt"
	hash := getHash(path)
	fmt.Printf("%s hash: %s", path, hash)
}

package main

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"golang.org/x/crypto/pbkdf2"
)

func main() {
	salt,_ := hex.DecodeString("b94e71d68b3bd062f5127d3a43b1d268b2b487d8f9e00fe3298b9d86bb798b78")
	key := pbkdf2.Key([]byte("123321"), salt, 1323, 32, sha256.New)
	fmt.Println(hex.EncodeToString(key))
}






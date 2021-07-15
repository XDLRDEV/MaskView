package main

/**
加密解密
*/

import (
	"fmt"
	math_rand "math/rand"
)

func main() {
	//随机生成长度为30
	kinds := [][]int{[]int{10, 48}, []int{26, 97}, []int{26, 65}}
	result := make([]byte, 30) //30位
	for i := 0; i < 30; i++ {
		randKind := math_rand.Intn(3)
		scope, base := kinds[randKind][0], kinds[randKind][1]
		result[i] = uint8(base + math_rand.Intn(scope))
	}
	fmt.Println(string(result))
}

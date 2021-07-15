package util

import (
	"regexp"
	"strconv"
)

func CheckNumber(phone uint64) bool {
	num := strconv.FormatUint(phone, 10)
	reg := `^1([38][0-9]|14[579]|5[^4]|16[6]|7[1-35-8]|9[189])\d{8}$`
	rgx := regexp.MustCompile(reg)
	return rgx.MatchString(num)
}

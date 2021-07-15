package util

import (
	"bytes"
	"fmt"
	"os/exec"
	"strings"
)

var matlabEnvPath = "/usr/local/MATLAB/MATLAB_Runtime/v94"

func Add(from, to, imageInputPath, imageOutputPath string) string {
	command := "sudo ./lib/add/run_markemd.sh " + matlabEnvPath + " " + from + " " + to + " " + imageInputPath + " " + imageOutputPath
	//command := "./lib/add/run_markemd.sh " + matlabEnvPath + " " + from + " " + to + " " + imageInputPath + " " + imageOutputPath
	println(command)
	cmd := exec.Command("/bin/bash", "-c", command)
	var out bytes.Buffer
	var stderr bytes.Buffer
	//cmd.Stdout = &out
	cmd.Stderr = &stderr
	key, err := cmd.Output()
	if err != nil {
		fmt.Printf("cmd run exception, %s , %s, %s", err.Error(), stderr.String(), out.String())
		return ""
	}
	arr := strings.Split(string(key), "lib/glnxa64\n")
	return arr[1]
}

func Trade(from, to, imageInputPath, imageOutputPath string) bool {
	command := "sudo lib/trade/run_robustemd.sh " + matlabEnvPath + " " + from + " " + to + " " + imageInputPath + " " + imageOutputPath
	//command := "lib/trade/run_robustemd.sh " + matlabEnvPath + " " + from + " " + to + " " + imageInputPath + " " + imageOutputPath
	println(command)
	cmd := exec.Command("sh", "-c", command)
	var out bytes.Buffer
	var stderr bytes.Buffer
	//cmd.Stdout = &out
	cmd.Stderr = &stderr
	_, err := cmd.Output()
	if err != nil {
		println("exec trade failed! " + err.Error())
		fmt.Printf("cmd run exception, %s , %s, %s", err.Error(), stderr.String(), out.String())
		return false
	}
	return true
}

func Check(inputImagePath, fromAndTo, key string) bool {
	command := "lib/qinquan/run_markextract.sh " + matlabEnvPath + " " + inputImagePath + " " + fromAndTo + " " + key
	cmd := exec.Command("sh", "-c", command)
	var out bytes.Buffer
	var stderr bytes.Buffer
	//cmd.Stdout = &out
	cmd.Stderr = &stderr
	res, err := cmd.Output()
	if err != nil {
		fmt.Printf("cmd run exception, %s , %s, %s", err.Error(), stderr.String(), out.String())
		return false
	}
	if string(res) == "0" {
		return false
	}
	return true
}

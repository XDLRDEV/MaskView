package action



import (
	"Starry/gorm_mysql"
	"Starry/models"
	"Starry/util"
	"bytes"
	"fmt"
	"github.com/disintegration/imaging"
	"github.com/labstack/echo/v4"
	"io/ioutil"
	"net/http"
	"os"
	"strconv"
)

/**
	基本练习
 */

func Test(c echo.Context) error {
	uid := c.FormValue("uid")
	uidUint, _ := strconv.ParseUint(uid, 10, 64)
	imgName := c.FormValue("imgName")
	// 原图路径
	originPath := "img/" + uid + "/" + imgName
	// 缩略原图
	imgData, _ := ioutil.ReadFile(originPath)
	buf := bytes.NewBuffer(imgData)
	image, err := imaging.Decode(buf)
	if err != nil {
		fmt.Println(err)
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "失败1",
			Data:   "",
		})
	}
	// 保存缩略图
	thumbnailDir := "thumbnailImg/" + uid
	exists, err := util.PathExists(thumbnailDir)
	if !exists {
		err = os.MkdirAll(thumbnailDir, os.ModePerm)
		if err != nil {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "失败2",
				Data:   "",
			})
		}
	}
	//生成缩略图，尺寸宽x,高传0表示等比例放缩
	image = imaging.Resize(image, 800, 0, imaging.Lanczos)
	err = imaging.Save(image, thumbnailDir+"/"+imgName)
	if err != nil {
		fmt.Println(err)
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: false,
			Note:   "失败3",
			Data:   "",
		})
	} else {
		// 更新数据库
		if err := gorm_mysql.Update(uidUint, originPath, thumbnailDir+"/"+imgName);
			err != nil {
			return c.JSON(http.StatusOK, models.SimpleResult{
				Result: false,
				Note:   "失败4",
				Data:   "",
			})
		}
		return c.JSON(http.StatusOK, models.SimpleResult{
			Result: true,
			Note:   "成功",
			Data:   "",
		})
	}
}

// 传递一个参数
func T1 (c echo.Context) error {
	value := c.FormValue("key")

	if value != "" {
		return c.JSON(http.StatusOK,
			models.SimpleResult{
				Result: true,
				Note:   "拿到数据",
				Data:   value,
			})
	}
	return c.JSON(http.StatusOK,
		models.SimpleResult{
			Result: false,
			Note:   "空数据",
			Data:   "nil",
		})
}

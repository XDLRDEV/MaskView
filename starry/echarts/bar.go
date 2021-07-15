package echarts

import (
	"Starry/gorm_mysql"
	"fmt"
	"github.com/chenjiandongx/go-echarts/charts"
	"github.com/labstack/echo/v4"
	"math/rand"
	"os"
)

func barShowLabel() *charts.Bar {
	bar := charts.NewBar()
	bar.SetGlobalOptions(
		charts.TitleOpts{Title: "示例图"},
		charts.ToolboxOpts{Show: true},
		charts.InitOpts{
			AssetsHost: AssetsHost,
			//Theme:      charts.ThemeType.Chalk,
		})
	bar.AddXAxis(nameItems).
		AddYAxis("商家A", randInt()).
		AddYAxis("商家B", randInt())
	bar.SetSeriesOptions(charts.LabelTextOpts{Show: true})
	return bar
}

var totalItems = []string{"全部上传数据", "疑似侵权数据", "已公证数据"}

func BarTotal() *charts.Bar {
	dataNum := make(chan uint)
	copyrightNum := make(chan uint)
	AuthRightNum := make(chan uint)
	go gorm_mysql.CountTable("data", dataNum)
	go gorm_mysql.CountTable("copyright", copyrightNum)
	go gorm_mysql.CountTable("auth_right", AuthRightNum)
	bar := charts.NewBar()
	bar.SetGlobalOptions(
		charts.TitleOpts{Title: "总量柱状图"},
		charts.ToolboxOpts{Show: true},
		charts.InitOpts{
			AssetsHost: AssetsHost,
		})
	num := []uint{<-dataNum, <-copyrightNum, <-AuthRightNum}
	nums := []uint{11, 12, 13}
	bar.AddXAxis(totalItems).
		AddYAxis("真实数据", num).
		AddYAxis("随机样例数", nums)
	return bar
}

func liquid() *charts.Liquid {
	copyrightNum := make(chan uint)
	AuthRightNum := make(chan uint)
	go gorm_mysql.CountTable("copyright", copyrightNum)
	go gorm_mysql.CountTable("auth_right", AuthRightNum)
	liquid := charts.NewLiquid()
	liquid.SetGlobalOptions(
		charts.TitleOpts{Title: "已公正数据占比"},
		charts.ToolboxOpts{Show: true},
		charts.InitOpts{
			AssetsHost: AssetsHost,
		})
	fmt.Println(float32(<-copyrightNum) / float32(<-AuthRightNum))
	liquid.Add("liquid", []float32{float32(23333) / float32(55555)},
		charts.LiquidOpts{IsWaveAnimation: true},
		charts.LabelTextOpts{Show: true},
	)
	return liquid
}

var mapData = map[string]float32{
	"北京":   float32(rand.Intn(150)),
	"上海":   float32(rand.Intn(150)),
	"深圳":   float32(rand.Intn(150)),
	"辽宁":   float32(rand.Intn(150)),
	"青岛":   float32(rand.Intn(150)),
	"山西":   float32(rand.Intn(150)),
	"陕西":   float32(rand.Intn(150)),
	"乌鲁木齐": float32(rand.Intn(150)),
	"齐齐哈尔": float32(rand.Intn(150)),
}

func geoShowLabel() *charts.Geo {
	geo := charts.NewGeo("china")
	geo.SetGlobalOptions(
		charts.TitleOpts{Title: "用户地区分布——假数据"},
		charts.ToolboxOpts{Show: true},
		charts.InitOpts{
			AssetsHost: AssetsHost,
		})

	fn := `function (params) {
		return params.name + ' : ' + params.value[2];
}`
	geo.Add("geo", charts.ChartType.EffectScatter, mapData,
		charts.LabelTextOpts{Show: true, Formatter: charts.FuncOpts(fn), Color: "black", Position: "right"},
		charts.RippleEffectOpts{Period: 4, Scale: 6, BrushType: "stroke"},
	)
	return geo
}

func pieRadius() *charts.Pie {
	pie := charts.NewPie()
	pie.SetGlobalOptions(charts.TitleOpts{Title: "Pie-测试图表"})
	pie.Add("pie", genKvData(),
		charts.LabelTextOpts{Show: true, Formatter: "{b}: {c}"},
		charts.PieOpts{Radius: []string{"40%", "75%"}},
	)
	return pie
}

func genKvData() map[string]interface{} {
	m := make(map[string]interface{})
	for i := 0; i < len(nameItems); i++ {
		m[nameItems[i]] = rand.Intn(maxNum)
	}
	return m
}

func BarHandler(c echo.Context) error {
	page := charts.NewPage(orderRouters("bar")...)
	page.Add(
		/*		barBase(),
				barTitle(),
				barShowLabel(),
				barXYName(),
				barColor(),
				barSplitLine(),
				barGap(),
				barYAxis(),
				barMultiYAxis(),
				barMultiXAxis(),
				barDataZoom(),
				barReverse(),
				barStack(),
				barMark(),
				barMarkCustom(),*/
		//barShowLabel(),
		BarTotal(),
		liquid(),
		geoShowLabel(),
		//pieRadius(),
	)

	page.PageTitle = "媒体素材公证链"
	f, err := os.Create(getRenderPath("test.html"))
	if err != nil {
		return err
	}
	if err = page.Render(f); err != nil {
		return err
	}
	return c.Inline("./echarts/assets/html/test.html", "temp.html")
}

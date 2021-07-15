# MaskView操作手册

## 环境准备

- FISCO-BCOS 区块链
- go 、 Java 和 android studio 的开发环境
- MySQL数据库，配置可远程访问
- 懂得如何部署智能合约，以及 `Nginx` 的安装

## 下载代码

克隆 MaskView 项目的 git 仓库，执行：  

```shell
git clone https://github.com/xdlianrong/MaskView.git
```

目录有：

```shell
img/  LICENSE  MaskView/  MaskView操作手册.md  other/  README.md  server-java/  starry/
```

其中，目录含义如下：

- MaskView ： 安卓客户端代码
- server-java：Java服务器代码
- starry：go服务器代码
- other：智能合约代码，以及`Nginx` 的配置文件

## 部署服务

在部署服务之前，需要修改各个代码中的配置。

### 修改配置

#### 1. 智能合约

首先，需要在区块链系统上部署我们写好的智能合约：`PointControllerClient.sol`，切换到控制台，部署合约：

```shell
部署，并修改智能合约的地址：
[group:1]> deploy PointController 
transaction hash: 0x2827cf823dce3cb3fc6543d89b042370f2be5d99fb2809a37f778d873888a7ae
contract address: 0x162da7a679bbe9d8015e6d4d32598ecb4f0966a8
```

之后，切入到Java代码中，找到对应目录，修改智能合约地址：

```shell
在目录下，com.media.bcos.client.PointControllerClient
修改部署合约的对应的地址
String contractAddress = "0x162da7a679bbe9d8015e6d4d32598ecb4f0966a8";
```

#### 2. go端

由于go服务器使用到了MySQL数据库，且需要连接到Java服务器，所以需要修改IP地址。

1. 更改数据库配置

```shell
找到目录：gorm_mysql/init.go

修改MySQL数据库的配置
db, err = gorm.Open("mysql", "test:密码@tcp(IP:3306)/starry?charset=utf8&parseTime=True&loc=Local")
```

2. `go` 端到`Java`端的通信`IP`

```shell
找到目录：action/javaIp.go

修改到Java端的IP地址
return "http://localhost:1718"
```

3. 数据库创表

```shell
这两个数据库，得改：
alter table image_key modify column img_key varchar(2550);
alter table data modify column data_hash varchar(2550);

Mysql删除数据库中所有表：
SELECT CONCAT('DROP TABLE ', table_name,';') FROM information_schema.`TABLES` WHERE table_schema='starry';   
```

4. 修改短信服务

```shell
找到目录：util/message.go

var (
	regionId        = "ID"
	accessKeyId     = "ID"
	accessKeySecret = "密码"
	outId = "1"
	scheme = "https"

	verificationCodeTemplateCode = "ID"
	verificationCodeSignName = "机构名称"

	messageNotificationTemplateCodeSeller = "ID"
	messageNotificationTemplateCodeBuyer = "ID"
	messageNotificationTemplateCodeTradeNotify = "ID"
	messageNotificationSignName = "机构名称"
)
配置相关服务
```

首次运行，需要拉取依赖：

```shell
go mod tidy
```

如果失败的话，多数情况是由于不能翻墙，可以配置mod 加速：

```shell
加速源 https://goproxy.cn
```

#### 3. Java端

由于Java服务器使用到了MySQL数据库，且需要连接到区块链，所以需要修改IP地址。

1. 修改MySQL数据库配置

```shell
找到目录文件：server-java\src\main\resources\application.properties

修改MySQL配置
# mysql8数据源配置
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://IP:3306/starry
spring.datasource.username=用户名
spring.datasource.password=密码
```

2. 连接到区块链网络

```shell
找到目录文件：server-java\src\main\resources\applicationContext.xml

修改区块链配置
 <value>IP:20200</value>
导入对于区块链节点的证书
```

3. 短信服务

```shell
找到目录文件：com.media.utils.Constants

    // 短信服务
    public static final String SMS_REGION_ID = "ID0";
    public static final String SMS_ACCESS_KEY_ID = "密钥1";
    public static final String SMS_ACCESS_KEY_SECRET = "密钥2";
    public static final String SMS_REQUEST_DOMAIN = "URL";
    public static final String SMS_REQUEST_VERSION = "time";
    public static final String SMS_REQUEST_ACTION = "服务";
    public static final String SMS_SIGN_NAME = "机构";
    public static final String SMS_LOGIN = "ID1";
    public static final String SMS_REGISTER = "ID2";
```



3. 启动命令：

```shell
java -jar xxx.jar
```



#### 4. 安卓端

由于安卓客户端需要和go服务器、Java服务器通信，需要修改对应的IP地址：

1. 和GO、Java服务器通信

```shell
找到目录文件：MaskView\app\src\main\java\com\xdlr\maskview\util\GetIP.java

修改和go端、Java端通信的IP地址：
 	public static String myIP() {
        return "https://IP:718";  // 云服务器
        //return "https://10.0.2.2:718";  // 本机
    }

    public static String javaIp() {
        return "https://IP:1718";  // 云服务器
        //return "https://10.0.2.2:1718";  // 本机
    }
```

2. 和 `Nginx` 通信

```shell
找到目录文件：com.xdlr.maskview.util.UtilParameter

修改显示照片url的ip：
public static final String IMAGES_IP = "http://IP/";  // 云服务器
```

#### 5. Nginx

对图片的访问，使用`nginx`进行加速访问。

```shell
nginx -s stop  关闭
nginx -c /etc/nginx/nginx.conf 启动
```

其配置主要是：

```nginx
		location  /img/ {
            root /root/maskview/starry/;
            autoindex on;
        }
        
        location  /thumbnailImg/ {
            root /root/maskview/starry/;
            autoindex on;
        }

        location /base64File/ {
            root /root/maskview/starry/;
            autoindex on;
        }	
```

### 部署启动

切换到go端，执行：

```shell
go run main.go
```

切换到Java端，执行：

```shell
java -jar XXX.jar
```

切换到android studio ，执行：

```shell
build 
找到对应的APP，安装到手机
```

备注：可云端部署

```shell
##根据端口port查进程：
lsof  -i:port     
##查找特定的进程
ps -ef|grep ssh
##不挂断运行命令,当账户退出或终端关闭时,程序仍然运行，所有输出被重定向到Log.log的文件中
nohup 执行命令 >> Log.log &
```

## 进入系统

### 1. 注册和登陆

登陆：

<div  align="center">
<img src="./img/登陆.jpg" width = 30% height = 30% />
</div>





### 2. 确权和上架

点击作品；

点击“我的确权”，可开始确权：

<div  align="center">
<img src="./img/上架-.jpg" width = 30% height = 30% />
</div>



点击“+”，选择需要确权的图片：

<div  align="center">
<img src="./img/确权.jpg" width = 30% height = 30% />
</div>

等待，嵌入水印：

<div  align="center">
<img src="./img/确权-上链.jpg" width = 30% height = 30% />
</div>

确权成功后：

<div  align="center">
<img src="./img/确权-成功.jpg" width = 30% height = 30% />
</div>



确权后，需要上架，只要上架后的商品才能被看到：

<div  align="center">
<img src="./img/上架.jpg" width = 30% height = 30% />
</div>

点击“上架”

### 3. 交易

点击商品，加入购物车：

<div  align="center">
<img src="./img/交易-购物车.jpg" width = 30% height = 30% />
</div>

点击购买，需要等待2分钟左右

在“我的购买”中可以看到信息：

<div  align="center">
<img src="./img/交易-成功.jpg" width = 30% height = 30% />
</div>
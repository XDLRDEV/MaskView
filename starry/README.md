# Starry
一个使用Golang Echo 编写的后端程序

##Starry有哪些功能？

* 用户功能
    *  使用pbkdf2加密
    *  采用sha256算法
    
    
## Starry接口详情

#### 用户功能
*  注册
    * Description : get message code
    * Router : /preRegister
    * Method : POST
    * Header : 
        * Content-Type:application/form-data
    * Param : 
        * phoneNumber
    * Result
        * {result: true/false, note: "message has sent""}

    * 描述 : 注册账户
    * 路由 : /register
    * 方式 : POST
    * Header : 
        * Content-Type:application/form-data 
    * 参数 ：
        * phoneNumber
        * username
        * password
        * code
    * 返回值：
        * {result: ture/false, note: "login success""}
    
*  登录
    * 描述 : 登录账户
    * 路由 : /login
    * 方式 : POST
    * Header : 
        * Content-Type:application/form-data 
    * 参数 ：
        * phoneNumber
        * password
    * 返回值：
        * {result: ture/false,token: t //token值保存}   // token need to be added in the http head for authentication
        
*  更改密码
    * 描述 : 更改账户密码
    * 路由 : /reset
    * 方式 : POST
    * Header : 
        * Content-Type:application/form-data 
    * 参数 ：
        * phoneNumber
        * oldPassword
        * newPassword
    * 返回值：
        * {result: true/false, note: "密码更新成功"})
   
* watermark
    * zero watermark
        * Description : upload a picture
        * Router : /restricted/uploadImg
        * Param
            * file: a image
        * Result : {result: true/false, note: "upload image success"}
        
        * Description : get zero watermark key
        * Router : /restricted/getImageKey
        * Param : 
            * fileName: the image name 
        * Result : {result: true/false, note : "watermark add success", data: "aabbcc11122344556666""} 
    
        * Description : check zero watermark key, precondition upload the image
        * Router : /restricted/checkImageKey
        * Param:
            * fileName : the image fileName
            * zeroKey : the zeroKey generate last step
         * Result : {result: true/false, note: "zero watermark check success", data: "1"/"0""}
         
    * robust watermark
        * Description : upload a picture just like zero watermark
        
        * Description : trade
        * Router : /restricted/trade
        * Param : 
            * phoneNumber
            * fileName
            * value
            * mold
        Result : {result: true/false, note: "trade success", data "outputImagePath"}
        
        * Description : get uidFrom and uidTo from robust image, first you need to upload the robust image generated last step
        * Router : /restricted/tradeCheck
        * Param : 
            * fileName : the robust image file name
        Result : {result: true/false, note: "robust watermark check success", Data: "111222"}  // it means uidFrom is 111 and uidTo is 222
          
            
* server port
   * main 718
   * grpc 1330 
   * block chain 30303 - 30306, 20200 - 20203, 8545 - 8548


### 注意事项
1. 需安装matlab runtime 2018b
2. 安装nginx， 用来做图片服务器
3. db需要迁移，同时至少需要一个备份，防止数据丢失.  gorm_mysql/init.go 中数据库账号密码需要切换成公司自己的
4. 部署的时候直接把 main 文件上传至 root@qqcloud1.xdlianrong.cn:/root/go/src/Starry/ 目录下即可
5. 接口直接看 媒体素材接口文档
6. 图片侵权部分暂时没做，目前只能根据图片生成的 imgKey 查找到 txId, 再到区块链浏览器去查找对应的记录（记录无法解码）
7. 关于交易，可以做以验证码的方式告知对方，索要到短信验证码后进行交易，避免单一责任方导致的交易不可控因素


## 关于作者
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮件:beihai@wingsxdu.com
* QQ: 1844848686
* blog: [@beihai](https://www.wingsxdu.com)

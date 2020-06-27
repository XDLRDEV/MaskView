package com.xdlr.maskview.util;


public class UtilParameter {

    // 我的积分,登录后赋值
    public static String myPoints;
    // 我的手机号,登录后赋值
    public static String myPhoneNumber;
    // token值
    public static String myToken;
    // 我的用户名
    public static String myNickName;

    // 显示照片url的ip
    //public static final String IMAGES_IP = "http://10.0.2.2:8080/";
    public static final String IMAGES_IP = "http://qqcloud1.xdlianrong.cn/";

    // 上传图片url
    public static final String uploadImgUrl = GetIP.myIP() + "/restricted/uploadImg";

    // 确权标志位
    public static final int CONFIRM_FLAG = 1;
    // 购买标志位
    public static final int DEAL_FLAG = 2;


}

package com.xdlr.maskview.dao;

import android.util.Log;

import com.xdlr.maskview.util.GetIP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
<<<<<<< HEAD
import java.util.HashMap;
=======
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
>>>>>>> '测试'
import java.util.Map;
import java.util.UUID;


/**
 * 请求服务器模块方法
 */

public class UserRequest {

    private static String CHARSET = "UTF-8"; //编码格式

    /**
     * POST方法请求服务器 并 得到返回结果
     *
     * @param url    : go服务器地址
     * @param params : 传递的字段key值
     * @return : 返回请求服务器的结果
     */
    private String requestResult(String url, Map<String, String> params, String token) {

        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
<<<<<<< HEAD
            conn.setConnectTimeout(1000);  //响应时间超时设置
=======
            conn.setConnectTimeout(2000);  //响应时间超时设置
>>>>>>> '测试'
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.addRequestProperty("Authorization", "Bearer " + token);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), CHARSET);
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    // System.out.println(entry.getKey()+":"+entry.getValue());
                }
                System.out.println("param:" + param.toString());
                out.write(param.toString());
            }

            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            in = new BufferedReader(inputStreamReader);
            //in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 上传图片给服务器
     *
     * @param file       : 图片以文件形式上传
     * @param RequestURL : 水印服务器地址
     * @return : 返回请求服务器的结果
     */
    public String uploadImage(File file, String RequestURL, String token) {
        String result = "error";
        String BOUNDARY = UUID.randomUUID().toString();//边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";//内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);  //超时时间
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);//允许输入流
            conn.setDoOutput(true);//允许输出流
            conn.setUseCaches(false);//不允许使用缓存
            conn.setRequestMethod("POST");//请求方式
            conn.setRequestProperty("Charset", CHARSET);//设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.addRequestProperty("Authorization", "Bearer " + token);
            conn.connect();

            if (file != null) {
                //当文件不为空，把文件包装并且上传
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                /*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key,只有这个key才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的。比如:abc.png
                 */
                dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + file.getName() + "\"" + LINE_END);
                dos.writeBytes(LINE_END);

                FileInputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());

                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /*
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流  
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuilder sbs = new StringBuilder();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sbs.append((char) ss);
                    }
                    result = sbs.toString();
                    Log.i("---", "result------------------>>" + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


<<<<<<< HEAD
=======
    public String uploadImageSetName(File file, String imgName, String RequestURL, String token) {
        String result = "error";
        String BOUNDARY = UUID.randomUUID().toString();//边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";//内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);  //超时时间
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);//允许输入流
            conn.setDoOutput(true);//允许输出流
            conn.setUseCaches(false);//不允许使用缓存
            conn.setRequestMethod("POST");//请求方式
            conn.setRequestProperty("Charset", CHARSET);//设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.addRequestProperty("Authorization", "Bearer " + token);
            conn.connect();

            if (file != null) {
                //当文件不为空，把文件包装并且上传
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                /*
                 * 这里重点注意：
                 * name里面的值为服务器端需要key,只有这个key才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的。比如:abc.png
                 */
                dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
                dos.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + imgName + "\"" + LINE_END);
                dos.writeBytes(LINE_END);

                FileInputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());

                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /*
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流  
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuilder sbs = new StringBuilder();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sbs.append((char) ss);
                    }
                    result = sbs.toString();
                    Log.i("---", "result------------------>>" + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


>>>>>>> '测试'
    /**
     * 登录请求---密码登录
     *
     * @param phoneNumber : 手机号
     * @param password    : 密码
     * @param token       : null
     * @return : 请求服务器的结果
     */
    public String loginByPwd(String phoneNumber, String password, String token) {

        //对所有证书添加信任
        FakeX509TrustManager.allowAllSSL();
        //获取登录url
        String ip = GetIP.myIP();
        String url = ip + "/loginByPwd";
        //传递的字段uid和password
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", password);
        //获取服务器的返回结果
        return requestResult(url, params, token);

    }

    /**
     * 发送验证码
     *
     * @param phoneNumber : 手机号
     * @param token       : null
     * @return 服务器返回值
     */
    public String sendPhoneCode(String phoneNumber, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/getPhoneCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);

        return requestResult(url, params, token);
    }


    /**
     * 注册
     *
     * @param phoneNumber : 手机号
     * @param password    : 密码
     * @param token       : null
     * @return 服务器返回值
     */
    public String register(String phoneNumber, String password, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/register";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", password);

        return requestResult(url, params, token);
    }


    /**
     * 登录请求---手机验证码登录
     *
     * @param phoneNumber : 手机号
     * @param phoneCode   : 手机验证码
     * @param token       : null
     * @return 服务器返回值
     */
    public String loginByCode(String phoneNumber, String phoneCode, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/loginByVCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("vcode", phoneCode);

        return requestResult(url, params, token);
    }


    /**
     * 自动登录---验证码
     *
     * @param phoneNumber : 手机号
     * @param code        : 最后一次验证码
     * @param token       : token
     * @return 服务器返回结果
     */
    public String autoLoginByCode(String phoneNumber, String code, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/autoLoginByCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("code", code);

        return requestResult(url, params, token);
    }


    /**
     * 忘记密码---检查验证码是否正确
     *
     * @param phoneNumber : 手机号
     * @param phoneCode   : 手机验证码
     * @param token       : null
     * @return 服务器结果
     */
    public String forgetPwdCheckCode(String phoneNumber, String phoneCode, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/forgetPwdCheckCode";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("vcode", phoneCode);

        return requestResult(url, params, token);
    }


    /**
     * 忘记密码---重置密码
     *
     * @param phoneNumber : 手机号码
     * @param phoneCode   : 手机验证码
     * @param newPwd      : 新密码
     * @param token       : null
     * @return 服务器返回值
     */
    public String resetPwd(String phoneNumber, String phoneCode, String newPwd, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/forgetResetPwd";
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", newPwd);
        params.put("vcode", phoneCode);

        return requestResult(url, params, token);
    }

    /**
     * 获取我的购物车信息(需要登录)
     *
     * @param token : token
     * @return 服务器返回值
     */
    public String getShoppingCartInfo(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/getMyShoppingCarInfo";

        return requestResult(url, null, token);
    }


    /**
     * 删除购物车中的某条信息(需要登录)
     *
     * @param imgPath : 要删除的照片路径---img/3/xxxx.jpg
     * @param token   : token
     * @return 服务器返回值
     */
    public String deleteOneShoppingCartItem(String imgPath, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/deleteOneToShoppingCar";
        Map<String, String> params = new HashMap<>();
        params.put("imgPath", imgPath);

        return requestResult(url, params, token);
    }


    /**
<<<<<<< HEAD
=======
     * 清空购物车
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String clearShoppingCart(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/clearMyShoppingCart";

        return requestResult(url, null, token);
    }


    /**
>>>>>>> '测试'
     * 获取展厅信息,不需要登录
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getDisplayHallInfo(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/getDisplayHall";

        return requestResult(url, null, token);
    }


    /**
     * 展厅中获取选中图片的信息
     *
     * @param imgName : 图片名称 : img/1/xxxxxxx.jpg为例
     * @param token   : token
     * @return 服务器返回结果
     */
    public String getOneSellImgInfo(String imgName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/getOneSellImgInfoByImgName";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);

        return requestResult(url, params, token);
    }


    /**
     * 添加某个图片到购物车(需要登录)
     *
     * @param sellerName : 卖家用户名
     * @param imgPath    : 图片路径 : img/1/xxxxxxx.jpg为例
     * @param token      : token
     * @return 服务器返回结果
     */
    public String addOneToMyShoppingCart(String sellerName, String imgPath, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/addOneToShoppingCar";
        Map<String, String> params = new HashMap<>();
        params.put("sellerName", sellerName);
        params.put("imgPath", imgPath);

        return requestResult(url, params, token);
    }


    /**
     * 获取我的上架图片集合
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getMySellImgList(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/getSellImgList";

        return requestResult(url, null, token);
    }


    /**
     * 上架图片,需要登录
     *
     * @param imgName:  图片名称
     * @param imgPrice: 图片价格
     * @param token     : token
     * @return 服务器返回的结果
     */
    public String sellImage(String imgName, String imgPrice, String imgTopic, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/sellImg";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        params.put("imgPrice", imgPrice);
        params.put("imgTopic", imgTopic);

        return requestResult(url, params, token);
    }


    /**
     * 修改上架图片的价格,需要登录
     *
     * @param imgName     : 图片名称 (con-xxxx.jpg)
     * @param newImgPrice : 新的价格
     * @param token       : token
     * @return 服务器返回结果
     */
    public String updateSellImgPrice(String imgName, String newImgPrice, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/updateSellImgPrice";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        params.put("newImgPrice", newImgPrice);

        return requestResult(url, params, token);
    }


    /**
     * 下架某张图片,需要登录
     *
     * @param imgName : 图片名称 (con-xxx.jpg)
     * @param token   : token
     * @return 服务器返回结果
     */
    public String underSellImg(String imgName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/deleteSellImg";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);

        return requestResult(url, params, token);
    }

    /**
     * 上传确权信息,需要登录
     *
     * @param imgName: 图片名称
     * @param imgKey:  零水印序列
     * @param token    : token
     * @return : 服务器返回结果
     */
    public String sendConfirmInfo(String imgName, String imgKey, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/confirmImg";
        Map<String, String> params = new HashMap<>();
        params.put("imgName", imgName);
        params.put("imgKey", imgKey);

        return requestResult(url, params, token);
    }


    /**
     * 检验照片是否是自己的,是否可以购买,需要登录
     *
     * @param sellerName : 卖家昵称
     * @param token      : token
     * @return 服务器返回结果
     */
    public String isMyImg(String sellerName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/isMyImage";
        Map<String, String> params = new HashMap<>();
        params.put("sellerName", sellerName);

        return requestResult(url, params, token);
    }


    /**
     * 购买图片,需要登录
     *
     * @param sellerName : 卖家用户名
     * @param imgName    : 本次购买的图片名称(卖家的图片名称), 添加购买水印的图片在本机,服务器不存储
     * @param imgPrice   : 购买价格
     * @param dataHash   : 购买水印序列
     * @return 服务器返回结果
     */
    public String purchaseImg(String sellerName, String imgName, String imgPrice, String dataHash, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/purchaseImg";
        Map<String, String> params = new HashMap<>();
        params.put("sellerName", sellerName);
        params.put("imgName", imgName);
        params.put("imgPrice", imgPrice);
        params.put("dataHash", dataHash);

        return requestResult(url, params, token);
    }


    /**
     * 获取购买记录
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getPurchaseRecords(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/getMyPurchaseRecords";

        return requestResult(url, null, token);
    }


    /**
     * 获取出售记录
     *
     * @param token : token
     * @return 服务器返回结果
     */
    public String getSaleRecords(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/getMySaleRecords";

        return requestResult(url, null, token);
    }


    /**
     * 获取用户信息,需要登录
     *
     * @param token : token
     */
    public String getMyInfo(String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/getMyInfo";

        return requestResult(url, null, token);
    }


    /**
     * 修改昵称,需要登录
     *
     * @param newNickName : 新昵称
     * @param token       : token
     * @return 服务器返回结果
     */
    public String updateMyNickName(String newNickName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/updateMyNickName";
        Map<String, String> params = new HashMap<>();
        params.put("newNickName", newNickName);

        return requestResult(url, params, token);
    }

    /**
     * 修改性别,需要登录
     *
     * @param sex   : 性别
     * @param token : token
     * @return 服务器返回结果
     */
    public String updateMySex(String sex, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/updateMySex";
        Map<String, String> params = new HashMap<>();
        params.put("sex", sex);

        return requestResult(url, params, token);
    }


    /**
     * 修改出生日期,需要登录
     *
     * @param birth : 出生日期
     * @param token : token
     * @return 服务器返回结果
     */
    public String updateMyBirth(String birth, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/updateMyBirth";
        Map<String, String> params = new HashMap<>();
        params.put("birth", birth);

        return requestResult(url, params, token);
    }


    /**
     * 设置头像,需要登录
     *
     * @param headViewName : 头像图片名称
     * @param token        : token
     * @return 服务器返回结果
     */
    public String updateMyHeadView(String headViewName, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/updateMyHeadView";
        Map<String, String> params = new HashMap<>();
        params.put("headViewName", headViewName);

        return requestResult(url, params, token);
    }


    /**
     * 修改密码,需要登录
     *
     * @param newPassword : 新密码
     * @param token       : token
     * @return 服务器返回结果
     */
    public String updatePwd(String newPassword, String token) {
        FakeX509TrustManager.allowAllSSL();
        String ip = GetIP.myIP();
        String url = ip + "/restricted/updatePwd";
        Map<String, String> params = new HashMap<>();
        params.put("newPassword", newPassword);

        return requestResult(url, params, token);
    }
}

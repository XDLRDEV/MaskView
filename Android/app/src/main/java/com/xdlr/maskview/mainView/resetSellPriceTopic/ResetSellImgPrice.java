package com.xdlr.maskview.mainView.resetSellPriceTopic;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
=======
import android.content.Context;
import android.content.DialogInterface;
>>>>>>> '测试'
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

>>>>>>> '测试'
import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.UrlTransBitmap;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

<<<<<<< HEAD
import java.util.concurrent.ExecutionException;
=======
>>>>>>> '测试'
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ResetSellImgPrice extends AppCompatActivity implements View.OnClickListener {

    private String resetPriceImgUrl;
    private PhotoView iv_mySellImg;
    private EditText et_newPrice;
    private TextView tv_resetSellPrice_imgTopic;
    private TextView tv_resetSellPrice_sellerName;
    private TextView tv_resetSellPrice_sellDate;
    private View headerView, footerView;
    private RelativeLayout relativeLayout;
<<<<<<< HEAD

=======
>>>>>>> '测试'
    private Bitmap bgBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private int screenWidth;
    private int screenHeight;
    private boolean isHidden = false;
    private final static int RESET_PRICE_SUCCESS = 0;
    private final static int RESET_PRICE_FAIL = 1;
    private final static int RESET_NO_RESPONSE = 2;
    private AlertDialog alertDialog;
<<<<<<< HEAD

=======
>>>>>>> '测试'
    private Context myContext;
    private UserRequest ur;
    private String imgOwner;
    private String sellDate;
    private String imgPrice;
    private String imgTopic;

<<<<<<< HEAD

=======
>>>>>>> '测试'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_sell_img_price);

        initView();
        getImgUrl();
    }

    private void initView() {
        myContext = this;
        ur = new UserRequest();
        relativeLayout = findViewById(R.id.layout_resetSellImgPrice);
        ImageView iv_finishThiActivity = findViewById(R.id.iv_finishResetSellPrice);
        iv_finishThiActivity.setOnClickListener(this);
        iv_mySellImg = findViewById(R.id.iv_mySellOneImg);
        et_newPrice = findViewById(R.id.et_resetSellPrice_newPrice);
        tv_resetSellPrice_imgTopic = findViewById(R.id.tv_resetSellPrice_imgTopic);
        tv_resetSellPrice_sellerName = findViewById(R.id.tv_resetSellPrice_sellerName);
        tv_resetSellPrice_sellDate = findViewById(R.id.tv_resetSellPrice_sellDate);
        headerView = findViewById(R.id.resetSellPrice_header_view);
        footerView = findViewById(R.id.resetSellPrice_footer_view);
        Button bt_resetSellPrice_quit = findViewById(R.id.bt_resetSellPrice_quit);
        bt_resetSellPrice_quit.setOnClickListener(this);
        Button bt_resetSellPrice_ok = findViewById(R.id.bt_resetSellPrice_ok);
        bt_resetSellPrice_ok.setOnClickListener(this);

        //获取屏幕宽高
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;

<<<<<<< HEAD
        //点击图片显隐图片信息
=======
        // 点击图片显隐图片信息
>>>>>>> '测试'
        iv_mySellImg.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                updateHeaderAndFooter();
            }
        });
    }

    private void showView() {
        headerView.getBackground().setAlpha(10);
        footerView.getBackground().setAlpha(10);
        showSoftColor();
        et_newPrice.setHint(imgPrice);
        tv_resetSellPrice_imgTopic.setText(imgTopic);
        tv_resetSellPrice_sellerName.setText(imgOwner);
        tv_resetSellPrice_sellDate.setText(sellDate);
<<<<<<< HEAD
        //网络加载图片
        Glide.with(this).load(resetPriceImgUrl).into(iv_mySellImg);
    }

    //接收传回过来的图片地址
=======
        // 网络加载图片
        Glide.with(this).load(resetPriceImgUrl).dontAnimate().into(iv_mySellImg);
    }

    // 接收传回过来的图片地址
>>>>>>> '测试'
    private void getImgUrl() {
        resetPriceImgUrl = getIntent().getStringExtra("selectedResetPriceImgUrl");
        if (resetPriceImgUrl != null && !resetPriceImgUrl.equals("")) {
            Log.e("----------", "传过来的路径:  " + resetPriceImgUrl);
            getImgInfo();
        }
    }

<<<<<<< HEAD
    //请求服务器获取图片信息
    private void getImgInfo() {
        //将图片地址截取为图片名称
=======
    // 请求服务器获取图片信息
    private void getImgInfo() {
        // 将图片地址截取为图片名称
>>>>>>> '测试'
        final String imgName = resetPriceImgUrl.substring(resetPriceImgUrl.indexOf("img"));
        ur = new UserRequest();
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getOneSellImgInfo(imgName, null);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        imgOwner = jsonObject.get("userName") + "";
                        imgPrice = jsonObject.get("imgPrice") + "";
                        imgTopic = jsonObject.get("imgTopic") + "";
                        sellDate = jsonObject.get("sellDate") + "";
                        Log.e("-----------------", "run: " + result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                showView();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finishResetSellPrice:
                ResetSellImgPrice.this.finish();
                break;
            case R.id.bt_resetSellPrice_ok:
                resetImgPrice();
                break;
            case R.id.bt_resetSellPrice_quit:
<<<<<<< HEAD
                //点击取消改价,恢复原价
=======
                // 点击取消改价,恢复原价
>>>>>>> '测试'
                et_newPrice.setText("");
                break;
        }
    }

<<<<<<< HEAD
    //修改价格,传给服务器
=======
    // 修改价格,传给服务器
>>>>>>> '测试'
    private void resetImgPrice() {
        final int newPrice;
        if (et_newPrice.getText().toString().trim().equals("")) {
            Toast.makeText(myContext, "请输入修改的价格", Toast.LENGTH_SHORT).show();
            return;
        } else {
            newPrice = Integer.parseInt(et_newPrice.getText().toString());
            if (newPrice == 0) {
                Toast.makeText(myContext, "价格不能为0", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String imgName = resetPriceImgUrl.substring(resetPriceImgUrl.indexOf("con-" + UtilParameter.myPhoneNumber));
                String result = ur.updateSellImgPrice(imgName, newPrice + "", UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
<<<<<<< HEAD
                            //修改成功提示框,返回我的上架
=======
                            // 修改成功提示框,返回我的上架
>>>>>>> '测试'
                            Message msg = new Message();
                            msg.what = RESET_PRICE_SUCCESS;
                            resetWindowHandler.sendMessage(msg);
                        } else {
<<<<<<< HEAD
                            //修改失败,请稍后重试
=======
                            // 修改失败,请稍后重试
>>>>>>> '测试'
                            Message msg = new Message();
                            msg.what = RESET_PRICE_FAIL;
                            resetWindowHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
<<<<<<< HEAD
                    //服务器未响应
=======
                    // 服务器未响应
>>>>>>> '测试'
                    Message msg = new Message();
                    msg.what = RESET_NO_RESPONSE;
                    resetWindowHandler.sendMessage(msg);
                }
            }
        };

        es.submit(task);
        es.shutdown();

    }


<<<<<<< HEAD
    //改价成功失败提示
=======
    // 改价成功失败提示
>>>>>>> '测试'
    private Handler resetWindowHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == RESET_PRICE_SUCCESS) {
                alertDialog = new AlertDialog.Builder(myContext).setMessage("修改成功")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ResetSellImgPrice.this.finish();
                            }
                        }).create();
            } else if (msg.what == RESET_PRICE_FAIL) {
                alertDialog = new AlertDialog.Builder(myContext).setMessage("改价失败,求稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
            } else if (msg.what == RESET_NO_RESPONSE) {
                alertDialog = new AlertDialog.Builder(myContext).setMessage("服务器未响应,请稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
            }
            alertDialog.show();
            return false;
        }
    });


    private void showSoftColor() {
        UrlTransBitmap trans = new UrlTransBitmap();
        Bitmap bitmap = trans.returnBitMap(resetPriceImgUrl, myContext);
<<<<<<< HEAD
        Log.e("------", "转成bitmap: ");
=======
>>>>>>> '测试'
        if (bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
<<<<<<< HEAD
                    //记得判空
                    if (palette == null) return;
                    //palette取色不一定取得到某些特定的颜色，这里通过取多种颜色来避免取不到颜色的情况
                    if (palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkVibrantColor(Color.TRANSPARENT),
                                palette.getDarkVibrantColor(Color.TRANSPARENT));
                        //Log.e("---", "第一个: ");
                    } else if (palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                        //Log.e("---", "第二个: ");
                    } else {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                        //Log.e("---", "第三个: ");
=======
                    if (palette == null) return;
                    if (palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkVibrantColor(Color.TRANSPARENT),
                                palette.getDarkVibrantColor(Color.TRANSPARENT));
                    } else if (palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
                    } else {
                        createLinearGradientBitmap(palette.getDarkMutedColor(Color.TRANSPARENT),
                                palette.getDarkMutedColor(Color.TRANSPARENT));
>>>>>>> '测试'
                    }
                }
            });
        }
    }

<<<<<<< HEAD
    //创建线性渐变背景色
=======
    // 创建线性渐变背景色
>>>>>>> '测试'
    private void createLinearGradientBitmap(int darkColor, int color) {
        int bgColors[] = new int[2];
        bgColors[0] = darkColor;
        bgColors[1] = color;
        if (bgBitmap == null) {
<<<<<<< HEAD
            //bgBitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_4444);
            bgBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
        }

        if (mCanvas == null) {
            mCanvas = new Canvas();
        }

=======
            bgBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas();
        }
>>>>>>> '测试'
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mCanvas.setBitmap(bgBitmap);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        LinearGradient gradient = new LinearGradient(0, 0, 0, bgBitmap.getHeight(), bgColors[0], bgColors[1], Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        RectF rectF = new RectF(0, 0, bgBitmap.getWidth(), bgBitmap.getHeight());
<<<<<<< HEAD
        // mCanvas.drawRoundRect(rectF,16,16,mPaint); 这个用来绘制圆角的哈
        mCanvas.drawRect(rectF, mPaint);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            relativeLayout.setBackground(new BitmapDrawable(getResources(), bgBitmap));
            Log.e("-----------", "渐变色设置完成 ");
        }

    }


    //控制头部尾部的显隐
=======
        mCanvas.drawRect(rectF, mPaint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            relativeLayout.setBackground(new BitmapDrawable(getResources(), bgBitmap));
        }
    }


    // 控制头部尾部的显隐
>>>>>>> '测试'
    private void updateHeaderAndFooter() {
        if (isHidden) {
            headerView.animate().translationY(0);
            footerView.animate().translationY(0);
        } else {
            headerView.animate().translationY(-headerView.getMeasuredHeight());
            footerView.animate().translationY(footerView.getMeasuredHeight());
<<<<<<< HEAD
            //隐藏输入法
=======
            // 隐藏输入法
>>>>>>> '测试'
            hiddenInputMethod(relativeLayout, this);
        }
        isHidden = !isHidden;
    }


    //隐藏输入法
    public static void hiddenInputMethod(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

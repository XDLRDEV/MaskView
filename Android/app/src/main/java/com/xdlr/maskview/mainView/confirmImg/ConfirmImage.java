package com.xdlr.maskview.mainView.confirmImg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goyourfly.multi_picture.ImageLoader;
import com.goyourfly.multi_picture.MultiPictureView;
import com.goyourfly.vincent.Vincent;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.ImageUriUtil;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.MaskView;
import com.xdlr.maskview.util.GetSDPath;
import com.xdlr.maskview.util.GlideImageEngine;
import com.xdlr.maskview.util.UtilParameter;
import com.xdlr.maskview.watermark.Robustwatermark;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfirmImage extends AppCompatActivity implements View.OnClickListener {

    private List<Uri> selectedConfirmImageUri;
    private ArrayList<String> selectedConfirmImageName;
    private ArrayList<String> selectedConfirmImagePath;
    private MultiPictureView multiPictureView;
    private static final int REQUEST_CODE_CHOOSE = 0x13;
    private final static int CONFIRM_WAITING = 0;
    private final static int CONFIRM_FAIL = 1;

    private Context myContext;

    private AlertDialog alertDialog;
    private AlertDialog waterMarkWaitingDialog;
    private int confirmCount;

    private Robustwatermark robust;
    private UserRequest ur;
    private String markPhone;

    private String SDCardPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);

        initView();
        getData();
    }

    private void getData() {
        SDCardPath = GetSDPath.getSDPath(myContext);
        //水印手机号
        markPhone = UtilParameter.myPhoneNumber;
        //图片Uri
        selectedConfirmImageUri = (List<Uri>) getIntent().getSerializableExtra("selectedConfirmImgUri");
        //图片Path
        selectedConfirmImagePath = getIntent().getStringArrayListExtra("selectedConfirmImgPath");
        Log.e("----------", "getData: " + selectedConfirmImagePath.size());
        //图片名称
        selectedConfirmImageName = getIntent().getStringArrayListExtra("selectedConfirmImgName");
        if (selectedConfirmImageUri != null) {
            multiPictureView.setList(selectedConfirmImageUri);
            MultiPictureView.setImageLoader(new ImageLoader() {
                @Override
                public void loadImage(@NotNull ImageView imageView, @NotNull Uri uri) {
                    Vincent.with(imageView.getContext()).load(uri).placeholder(R.mipmap.loading)
                            .error(R.mipmap.loading).into(imageView);
                }
            });
        }
    }

    private void initView() {
        myContext = this;
        Button bt_choseAgain = findViewById(R.id.bt_confirmImage_choseAgain);
        Button bt_startConfirm = findViewById(R.id.bt_confirmImage_confirmNow);
        bt_choseAgain.setOnClickListener(this);
        bt_startConfirm.setOnClickListener(this);
        multiPictureView = findViewById(R.id.confirmImage_MultiPictureView);
        ImageView iv_finishThisActivity = findViewById(R.id.confirmImage_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);

        robust = new Robustwatermark();
        ur = new UserRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirmImage_choseAgain:
                choseConfirmImgAgain();
                break;
            case R.id.bt_confirmImage_confirmNow:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    confirmNowHighAPI();
                } else {
                    confirmNowLowAPI();
                }

                break;
            case R.id.confirmImage_finishThisActivity:
                ConfirmImage.this.finish();
                break;
        }
    }


    //跳转选取照片
    private void choseConfirmImgAgain() {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)  //true:选中后显示数字;false:选中后显示对号
                .maxSelectable(6) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideImageEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }


    // 确权图片(Android10以上,包括10)
    private void confirmNowHighAPI() {
        confirmCount = 1;
        if (selectedConfirmImageUri.size() > 0) {
            // 根据图片名称判断确权图片是否符合要求
            for (int i = 0; i < selectedConfirmImageName.size(); i++) {
                String imgName = selectedConfirmImageName.get(i);
                if (imgName.startsWith("con-") || imgName.startsWith("tra-")) {
                    imageNonCompliance();
                    return;
                }
            }
            // 等待框
            loadingAlert();
            // 开始水印
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < selectedConfirmImageUri.size(); i++) {
                        String privatePath = GetSDPath.getPrivatePath(myContext, selectedConfirmImageUri.get(i));
                        String type = privatePath.substring(privatePath.lastIndexOf("."));
                        FileInputStream fis = null;
                        FileOutputStream fos = null;
                        try {
                            fis = new FileInputStream(privatePath);
                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
                            Bitmap dstbmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            //加水印, 1是确权,2是交易
                            Bitmap myBitmap = robust.robustWatermark(dstbmp, markPhone, UtilParameter.CONFIRM_FLAG);
                            String key = robust.getKey();
                            String initialName = selectedConfirmImageName.get(i).substring(0, selectedConfirmImageName.get(i).lastIndexOf("."));
                            String mark_imgName = "con-" + markPhone + "-" + initialName + type;
                            File file = new File(SDCardPath, mark_imgName);
                            //上传数据到服务器
                            String result = ur.sendConfirmInfo(mark_imgName, key, UtilParameter.myToken);
                            if (result.contains("true")) {
                                fos = new FileOutputStream(file);
                                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.close();
                                GetSDPath.scanFile(file, myContext);
                                confirmCount++;
                            } else {
                                //确权失败,请稍后重试
                                confirmWaitingHandler.sendEmptyMessage(CONFIRM_FAIL);
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            noImage();
        }
    }

    // 确权图片(Android10以下)
    private void confirmNowLowAPI() {
        confirmCount = 1;
        if (selectedConfirmImageUri.size() > 0) {
            // 根据图片名称判断确权图片是否符合要求

            for (int i = 0; i < selectedConfirmImageName.size(); i++) {
                String imgName = selectedConfirmImageName.get(i);
                if (imgName.startsWith("con-") || imgName.startsWith("tra-")) {
                    imageNonCompliance();
                    return;
                }
            }

            // 符合要求,进行水印
            File confirmImgFile = new File(SDCardPath + "/MaskView确权");
            if (!confirmImgFile.exists()) {
                boolean makeFile = confirmImgFile.mkdirs();
                if (!makeFile) {
                    Toast.makeText(myContext, "文件夹创建失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //等待框
            loadingAlert();
            //水印
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < selectedConfirmImagePath.size(); i++) {
                        FileInputStream fis = null;
                        FileOutputStream fos = null;
                        try {
                            fis = new FileInputStream(selectedConfirmImagePath.get(i));
                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
                            Bitmap dstbmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            //加水印, 1是确权,2是交易
                            Bitmap myBitmap = robust.robustWatermark(dstbmp, markPhone, UtilParameter.CONFIRM_FLAG);
                            String key = robust.getKey();
                            String mark_imgName = "con-" + markPhone + "-" + selectedConfirmImageName.get(i);
                            //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MaskView确权/" + mark_imgName);
                            File file = new File(SDCardPath + "/MaskView确权/" + mark_imgName);
                            //上传数据到服务器
                            String result = ur.sendConfirmInfo(mark_imgName, key, UtilParameter.myToken);
                            if (result.contains("true")) {
                                fos = new FileOutputStream(file);
                                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.close();
                                confirmCount++;
                                //发送广播,刷新图库(API<29)
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri uri = Uri.fromFile(file);
                                intent.setData(uri);
                                sendBroadcast(intent);
                            } else {
                                //确权失败,请稍后重试
                                confirmWaitingHandler.sendEmptyMessage(CONFIRM_FAIL);
                                return;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            //请选择图片
            noImage();
        }
    }

    //选取完照片显示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE) {// 图库选择图片
            if (data != null) {
                selectedConfirmImagePath = new ArrayList<>();
                selectedConfirmImageName = new ArrayList<>();
                selectedConfirmImageUri.clear();
                selectedConfirmImagePath.clear();
                selectedConfirmImageName.clear();
                selectedConfirmImageUri = Matisse.obtainResult(data);
                for (Uri uri : selectedConfirmImageUri) {
                    selectedConfirmImagePath.add(ImageUriUtil.getPhotoPathFromContentUri(myContext, uri));
                }
                DocumentFile documentFile;
                for (int i = 0; i < selectedConfirmImageUri.size(); i++) {
                    documentFile = DocumentFile.fromSingleUri(myContext, selectedConfirmImageUri.get(i));
                    if (documentFile != null) {
                        selectedConfirmImageName.add(documentFile.getName());
                        Log.e("-----", "图片名称 : " + documentFile.getName());
                    }
                }
                multiPictureView.clearItem();
                multiPictureView.setList(selectedConfirmImageUri);
                MultiPictureView.setImageLoader(new ImageLoader() {
                    @Override
                    public void loadImage(@NotNull ImageView imageView, @NotNull Uri uri) {
                        Vincent.with(imageView.getContext()).load(uri).placeholder(R.mipmap.loading)
                                .error(R.mipmap.loading).into(imageView);
                    }
                });
            }
        }
    }


    //显示确权进度提示
    private void loadingAlert() {
        View view = View.inflate(myContext, R.layout.sell_waiting_window, null);
        waterMarkWaitingDialog = new AlertDialog.Builder(myContext).setView(view).create();
        waterMarkWaitingDialog.setTitle("正在确权处理,请耐心等待......");
        waterMarkWaitingDialog.show();
        confirmWaitingHandler.sendEmptyMessage(CONFIRM_WAITING);
    }

    //添加水印等待提示框
    Handler confirmWaitingHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == CONFIRM_WAITING) {
                TextView tv_now = waterMarkWaitingDialog.findViewById(R.id.loading_nowCount);
                TextView tv_all = waterMarkWaitingDialog.findViewById(R.id.loading_allCount);
                if (tv_now != null && tv_all != null) {
                    tv_now.setText(confirmCount + "");
                    tv_all.setText("/" + selectedConfirmImageName.size());
                }
                if (waterMarkWaitingDialog.isShowing()) {
                    confirmWaitingHandler.sendEmptyMessageDelayed(CONFIRM_WAITING, 1000);
                    if (confirmCount > selectedConfirmImageName.size()) {
                        waterMarkWaitingDialog.cancel();
                        finishAlert();
                    }
                }
            } else if (msg.what == CONFIRM_FAIL) {
                waterMarkWaitingDialog.cancel();
                alertDialog = new AlertDialog.Builder(myContext).setMessage("服务器未响应,请稍后重试")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                alertDialog.show();
            }
            return false;
        }
    });

    //图片不符合要求提示
    private void imageNonCompliance() {
        alertDialog = new AlertDialog.Builder(myContext)
                .setTitle("请重新选择")
                .setMessage("确权和购买的图片不能再次确权")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    //没有选择图片提示
    private void noImage() {
        alertDialog = new AlertDialog.Builder(myContext)
                .setMessage("请选择图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    //确权完成后提示
    private void finishAlert() {
        alertDialog = new AlertDialog.Builder(myContext)
                .setMessage("确权完成,共确权" + selectedConfirmImageName.size() + "张图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //跳转我的确权界面
                        Intent intent = new Intent(ConfirmImage.this, MaskView.class);
                        intent.putExtra("fragmentID", 1);
                        startActivity(intent);
                    }
                }).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

}

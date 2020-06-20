package com.xdlr.maskview.mainView.myInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.ImageUriUtil;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.MaskView;
import com.xdlr.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyInfo extends AppCompatActivity implements View.OnClickListener {

    private Context myContext;
    private AlertDialog alertDialog;
    private TextView tv_myNickName;
    private TextView tv_sex;
    private TextView tv_birth;
    private TextView tv_phoneNumber;
    private ImageView iv_headView;
    private DatePickerDialog datePickerDialog;

    private static int RESULT_LOAD_IMAGE = 1;  //成功选取照片后的返回值

    private UserRequest ur;

    private String nickName;
    private String headViewPath;
    private String sex;
    private String birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        initView();
        getMyInfo();
    }

    private void initView() {
        myContext = this;
        ur = new UserRequest();
        ImageView iv_finishThisActivity = findViewById(R.id.myInfo_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(this);
        LinearLayout layout_setNickName = findViewById(R.id.layout_myInfo_nickName);
        layout_setNickName.setOnClickListener(this);
        LinearLayout layout_setSex = findViewById(R.id.layout_myInfo_sex);
        layout_setSex.setOnClickListener(this);
        LinearLayout layout_setBirth = findViewById(R.id.layout_myInfo_birth);
        layout_setBirth.setOnClickListener(this);
        LinearLayout layout_setHeadView = findViewById(R.id.layout_myInfo_headView);
        layout_setHeadView.setOnClickListener(this);
        tv_myNickName = findViewById(R.id.tv_myInfo_nickName);
        tv_sex = findViewById(R.id.tv_myInfo_sex);
        tv_birth = findViewById(R.id.tv_myInfo_birth);
        tv_phoneNumber = findViewById(R.id.tv_myInfo_phoneNumber);
        iv_headView = findViewById(R.id.iv_myInfo_headView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myInfo_finishThisActivity:
                Intent intent = new Intent(myContext, MaskView.class);
                intent.putExtra("fragmentID", 3);
                startActivity(intent);
                break;
            case R.id.layout_myInfo_nickName:
                setNickName();
                break;
            case R.id.layout_myInfo_sex:
                setSex();
                break;
            case R.id.layout_myInfo_birth:
                setBirth();
                break;
            case R.id.layout_myInfo_headView:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    private void getMyInfo() {
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getMyInfo(UtilParameter.myToken);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        nickName = jsonObject.get("userName") + "";
                        headViewPath = jsonObject.get("headViewPath") + "";
                        sex = jsonObject.get("sex") + "";
                        birth = jsonObject.get("birth") + "";
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
                tv_myNickName.setText(nickName);
                String phoneNum = UtilParameter.myPhoneNumber;
                String hiddenPhone = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7);
                tv_phoneNumber.setText(hiddenPhone);
                if (!sex.equals("")) {
                    tv_sex.setText(sex);
                }
                if (!birth.equals("")) {
                    tv_birth.setText(birth);
                }
                if (!headViewPath.equals("")) {
                    String headUrl = UtilParameter.IMAGES_IP + headViewPath;
                    Glide.with(myContext).load(headUrl).into(iv_headView);
                }
                break;
            }
        }
    }

    private void setNickName() {
        View view = View.inflate(myContext, R.layout.alert_nick_name, null);
        alertDialog = new AlertDialog.Builder(myContext).setView(view).setTitle("昵称")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        EditText et_nickName = alertDialog.findViewById(R.id.alert_et__nickName);
                        if (et_nickName != null) {
                            final String nickName = et_nickName.getText().toString().trim();
                            if (nickName.equals(tv_myNickName.getText().toString().trim())) {
                                dialog.cancel();
                                return;
                            }
                            if (!nickName.equals("")) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String result = ur.updateMyNickName(nickName, UtilParameter.myToken);
                                        Log.e("--------", "result: " + result);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!result.equals("")) {
                                                    if (result.contains("true")) {
                                                        dialog.cancel();
                                                        tv_myNickName.setText(nickName + "");
                                                        UtilParameter.myNickName = nickName;
                                                    } else {
                                                        dialog.cancel();
                                                        Toast.makeText(myContext, "昵称不可用", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(myContext, "服务器未响应,请稍后重试", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).start();

                            } else {
                                tv_myNickName.setText("未设置");
                            }
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void setSex() {
        final String[] sex_list = {"男", "女", "保密"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别");
        builder.setSingleChoiceItems(sex_list, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final String sex = sex_list[which];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = ur.updateMySex(sex, UtilParameter.myToken);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.contains("true")) {
                                    dialog.cancel();
                                    tv_sex.setText(sex);
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setBirth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);    //获取年月日
        int month = cal.get(Calendar.MONTH);  //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                //将选择的日期,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                final String birth = year + "-" + (++month) + "-" + day;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = ur.updateMyBirth(birth, UtilParameter.myToken);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.contains("true")) {
                                    tv_birth.setText(birth);
                                }
                            }
                        });
                    }
                }).start();
            }
        };
        //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        DatePickerDialog dialog = new
                DatePickerDialog(MyInfo.this, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
        dialog.show();
    }


    //选取完照片后设置头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            DocumentFile documentFile = DocumentFile.fromSingleUri(myContext, uri);
            if (documentFile != null) {
                final String myHeadViewName = documentFile.getName(); //文件名称
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //上传图片
                        String uploadImageResult = "";
                        String imgPath = ImageUriUtil.getPhotoPathFromContentUri(myContext, uri);
                        File file = new File(imgPath);
                        uploadImageResult = ur.uploadImage(file, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                        if (uploadImageResult.contains("true")) {
                            //图片上传成功,上传数据
                            final String result = ur.updateMyHeadView(myHeadViewName, UtilParameter.myToken);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.contains("true")) {
                                        try {
                                            ContentResolver cr = myContext.getContentResolver();
                                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                                            iv_headView.setImageBitmap(bitmap);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }).start();
            }
        }
    }


    //不允许用自带的返回键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}

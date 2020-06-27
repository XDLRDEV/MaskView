package com.xdlr.maskview.mainView.setSellImgInfo;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

=======
>>>>>>> '测试'
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.net.Uri;
import android.os.Build;
>>>>>>> '测试'
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
<<<<<<< HEAD
=======
import android.view.KeyEvent;
>>>>>>> '测试'
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

>>>>>>> '测试'
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.MaskView;
import com.xdlr.maskview.mainView.setSellImgInfo.adapter.SellImgInfoAdapter;
<<<<<<< HEAD
import com.xdlr.maskview.util.UtilParameter;


import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetSellImgInfo extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView_sellImgList;
    private ImageView iv_finishSetSellImgInfo;
    private ArrayList<String> selectedSellImgList;


    private SellImgInfoAdapter adapter;
    private Context myContext;
    private AlertDialog waitingAlertDialog;
    private int waitingTag;
    private UserRequest ur;
=======
import com.xdlr.maskview.util.API29ImgBean;
import com.xdlr.maskview.util.GetSDPath;
import com.xdlr.maskview.util.UtilParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SetSellImgInfo extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView_sellImgList;
    private SellImgInfoAdapter adapter;
    private Context mContext;
    private AlertDialog waitingAlertDialog;
    private int waitingTag;
    private UserRequest ur;
    private List<API29ImgBean> mBeanList;
    private List<Uri> mSelectedSellUriList;
    private List<String> mImgPathList;
>>>>>>> '测试'

    private final static int SELL_WAITING = 0;
    private final static int SELL_NO_RESPONSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sell_img_info);
<<<<<<< HEAD
        myContext = this;
        initView();

        getImgPathList();
=======
        mContext = this;
        initView();

        getSelectedImgDatas();
>>>>>>> '测试'
    }

    private void initView() {
        recyclerView_sellImgList = findViewById(R.id.recycler_setSellImgInfo);
<<<<<<< HEAD
        iv_finishSetSellImgInfo = findViewById(R.id.setSellImgInfo_finishThisActivity);
=======
        ImageView iv_finishSetSellImgInfo = findViewById(R.id.setSellImgInfo_finishThisActivity);
>>>>>>> '测试'
        iv_finishSetSellImgInfo.setOnClickListener(this);
        Button bt_confirmSell = findViewById(R.id.bt_setSellImgInfo_confirmSell);
        bt_confirmSell.setOnClickListener(this);
        bt_confirmSell.getBackground().setAlpha(160);
        ur = new UserRequest();
    }

<<<<<<< HEAD
    private void getImgPathList() {
        selectedSellImgList = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("selectedSellImg");
        if (selectedSellImgList != null && selectedSellImgList.size() > 0) {
            Log.e("-------------", "getImgPathList: " + selectedSellImgList.size());
            showRecyclerView();
        }
=======
    private void getSelectedImgDatas() {
        mSelectedSellUriList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 29) {
            mBeanList = getIntent().getParcelableArrayListExtra("selectedImgBeanList");
            if (mBeanList != null) {
                for (int i = 0; i < mBeanList.size(); i++) {
                    mSelectedSellUriList.add(mBeanList.get(i).getImgUri());
                }
            }
        } else {
            mImgPathList = getIntent().getStringArrayListExtra("selectedSellImgPathList");
            if (mImgPathList != null) {
                for (int i = 0; i < mImgPathList.size(); i++) {
                    mSelectedSellUriList.add(Uri.fromFile(new File(mImgPathList.get(i))));
                }
            }
        }
        showRecyclerView();
>>>>>>> '测试'
    }

    private void showRecyclerView() {
        recyclerView_sellImgList.setHasFixedSize(true);
        recyclerView_sellImgList.setItemAnimator(null);

        //垂直方向的2列
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止Item切换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView_sellImgList.setLayoutManager(layoutManager);
        final int spanCount = 2;

        //解决底部滚动到顶部时，顶部item上方偶尔会出现一大片间隔的问题
        recyclerView_sellImgList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[spanCount];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });

<<<<<<< HEAD
        adapter = new SellImgInfoAdapter(selectedSellImgList, myContext);
=======
        adapter = new SellImgInfoAdapter(mContext, mSelectedSellUriList);
>>>>>>> '测试'
        recyclerView_sellImgList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setSellImgInfo_confirmSell:
<<<<<<< HEAD
                sellImg();
=======
                if (Build.VERSION.SDK_INT >= 29) {
                    sellImgHighAPI();
                } else {
                    sellImgLowAPI();
                }
>>>>>>> '测试'
                break;
            case R.id.setSellImgInfo_finishThisActivity:
                SetSellImgInfo.this.finish();
                break;
        }
    }

<<<<<<< HEAD
    private void sellImg() {
        waitingTag = 0;
        final View[] view = new View[1];

        //首先对价格进行判空
        for (int i = 0; i < selectedSellImgList.size(); i++) {
=======
    // 上架图片(Android10以下)
    private void sellImgLowAPI() {
        waitingTag = 0;
        final View[] view = {null};
        // 对价格进行判空处理
        for (int i = 0; i < mSelectedSellUriList.size(); i++) {
>>>>>>> '测试'
            view[0] = recyclerView_sellImgList.getChildAt(i);
            EditText et_price = view[0].findViewById(R.id.item_setSellImgInfo_price);
            String imgPrice = et_price.getText().toString();
            if (imgPrice.equals("") || Integer.parseInt(imgPrice) == 0) {
                et_price.setError("格式有误");
                return;
            }
        }
<<<<<<< HEAD

        loadingAlert();
        //上传图片并上架图片字段
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < selectedSellImgList.size(); i++) {
                    //上传图片到服务器
                    String uploadImageResult = "";
                    try {
                        File file = new File(selectedSellImgList.get(i));
                        //上传图片
                        uploadImageResult = ur.uploadImage(file, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                        //上传图片成功后,上传图片信息
=======
        // 等待提示
        loadingAlert();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mImgPathList.size(); i++) {
                    //上传图片到服务器
                    String uploadImageResult;
                    try {
                        File file = new File(mImgPathList.get(i));
                        // 上传图片
                        uploadImageResult = ur.uploadImage(file, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                        // 上传图片成功后,上传图片信息
>>>>>>> '测试'
                        if (uploadImageResult.contains("true")) {
                            view[0] = recyclerView_sellImgList.getChildAt(i);
                            EditText et_topic = view[0].findViewById(R.id.item_setSellImgInfo_topic);
                            EditText et_price = view[0].findViewById(R.id.item_setSellImgInfo_price);
                            String imgTopic = et_topic.getText().toString().trim();
<<<<<<< HEAD
                            String imgPrice = Integer.parseInt(et_price.getText().toString()) + ""; //消除数字前边的0 (0016->16)
                            String imageName = selectedSellImgList.get(i).substring(selectedSellImgList.get(i).indexOf("con-"));
                            String result = ur.sellImage(imageName, imgPrice, imgTopic, UtilParameter.myToken);
                            //上架成功,tag++
=======
                            // 消除数字前边的0 (0016->16)
                            String imgPrice = Integer.parseInt(et_price.getText().toString()) + "";
                            String imageName = mImgPathList.get(i).substring(mImgPathList.get(i).indexOf("con-"));
                            String result = ur.sellImage(imageName, imgPrice, imgTopic, UtilParameter.myToken);
                            // 上架成功,tag++
>>>>>>> '测试'
                            if (result.contains("true")) {
                                waitingTag++;
                            } else {
                                waitingOrNoResponseHandler.sendEmptyMessage(SELL_NO_RESPONSE);
                                return;
                            }
                        } else {
                            waitingOrNoResponseHandler.sendEmptyMessage(SELL_NO_RESPONSE);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
<<<<<<< HEAD

    }

=======
    }

    // 上架图片(Android10及以上)
    private void sellImgHighAPI() {
        waitingTag = 0;
        final View[] view = {null};
        // 对价格进行判空处理
        for (int i = 0; i < mSelectedSellUriList.size(); i++) {
            view[0] = recyclerView_sellImgList.getChildAt(i);
            EditText et_price = view[0].findViewById(R.id.item_setSellImgInfo_price);
            String imgPrice = et_price.getText().toString();
            if (imgPrice.equals("") || Integer.parseInt(imgPrice) == 0) {
                et_price.setError("格式有误");
                return;
            }
        }
        // 等待提示
        loadingAlert();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mBeanList.size(); i++) {
                    API29ImgBean bean = mBeanList.get(i);
                    String privatePath = GetSDPath.getPrivatePath(mContext, bean.getImgUri());
                    String type = privatePath.substring(privatePath.lastIndexOf("."));
                    File file = new File(privatePath);
                    // 上传图片
                    String uploadImgResult = ur.uploadImageSetName(file, bean.getImgName() + type, UtilParameter.uploadImgUrl, UtilParameter.myToken);
                    if (uploadImgResult.contains("true")) {
                        // 图片上传成功,上传上架数据
                        view[0] = recyclerView_sellImgList.getChildAt(i);
                        EditText et_topic = view[0].findViewById(R.id.item_setSellImgInfo_topic);
                        EditText et_price = view[0].findViewById(R.id.item_setSellImgInfo_price);
                        String imgTopic = et_topic.getText().toString().trim();
                        // 消除数字前边的0 (0016->16)
                        String imgPrice = Integer.parseInt(et_price.getText().toString()) + "";
                        String imageName = bean.getImgName();
                        String result = ur.sellImage(imageName + type, imgPrice, imgTopic, UtilParameter.myToken);
                        if (result.contains("true")) {
                            waitingTag++;
                        } else {
                            // 上传数据失败
                            waitingOrNoResponseHandler.sendEmptyMessage(SELL_NO_RESPONSE);
                            return;
                        }
                    } else {
                        // 上传图片失败
                        waitingOrNoResponseHandler.sendEmptyMessage(SELL_NO_RESPONSE);
                        return;
                    }
                }
            }
        }).start();
    }
>>>>>>> '测试'

    //上架等待提示框
    private void loadingAlert() {
        View view = View.inflate(getApplicationContext(), R.layout.sell_waiting_window, null);
        waitingAlertDialog = new AlertDialog.Builder(this).setView(view).create();
        waitingAlertDialog.setTitle("正在上传,请耐心等待......");
        waitingAlertDialog.show();
        waitingOrNoResponseHandler.sendEmptyMessage(SELL_WAITING);
    }


    //显示上架进度,未响应提示
    private Handler waitingOrNoResponseHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SELL_WAITING) {
                TextView tv_now = waitingAlertDialog.findViewById(R.id.loading_nowCount);
                TextView tv_all = waitingAlertDialog.findViewById(R.id.loading_allCount);
                if (tv_now != null && tv_all != null) {
                    tv_now.setText(waitingTag + "");
<<<<<<< HEAD
                    tv_all.setText("/" + selectedSellImgList.size());
                }
                if (waitingAlertDialog.isShowing()) {
                    waitingOrNoResponseHandler.sendEmptyMessageDelayed(0, 1000);
                    if (waitingTag == selectedSellImgList.size()) {
=======
                    tv_all.setText("/" + mSelectedSellUriList.size());
                }
                if (waitingAlertDialog.isShowing()) {
                    waitingOrNoResponseHandler.sendEmptyMessageDelayed(0, 1000);
                    if (waitingTag == mSelectedSellUriList.size()) {
>>>>>>> '测试'
                        waitingAlertDialog.cancel();
                        finishAlert();
                    }
                }
            } else if (msg.what == SELL_NO_RESPONSE) {
                waitingAlertDialog.cancel();
<<<<<<< HEAD
                AlertDialog noResponseAlertDialog = new AlertDialog.Builder(myContext).setMessage("服务器未响应,请稍后重试")
=======
                AlertDialog noResponseAlertDialog = new AlertDialog.Builder(mContext).setMessage("服务器未响应,请稍后重试")
>>>>>>> '测试'
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                noResponseAlertDialog.show();
            }
            return false;
        }
    });

    //上架完成提示
    private void finishAlert() {
        AlertDialog finishAlertDialog = new AlertDialog.Builder(this).setMessage("上架成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SetSellImgInfo.this, MaskView.class);
                        intent.putExtra("fragmentID", 1);
                        startActivity(intent);
<<<<<<< HEAD
=======
                        finish();
>>>>>>> '测试'
                    }
                }).create();
        finishAlertDialog.show();
    }

<<<<<<< HEAD
=======
    //自带的返回键finish当前页面
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            SetSellImgInfo.this.finish();
        }
        return super.dispatchKeyEvent(event);
    }
>>>>>>> '测试'
}

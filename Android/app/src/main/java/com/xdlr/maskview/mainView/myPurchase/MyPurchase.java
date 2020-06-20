package com.xdlr.maskview.mainView.myPurchase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xdlr.maskview.R;
import com.xdlr.maskview.mainView.myPurchase.adapter.MyPurchaseAdapter;
import com.xdlr.maskview.util.GetSDPath;
import com.xdlr.maskview.util.UtilParameter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Objects;

public class MyPurchase extends AppCompatActivity {

    private RefreshLayout refreshLayout_myPurchase;
    private RecyclerView recyclerView_myPurchase;
    private LinearLayout layout_noImg;
    private String SDCardPath;
    private Context myContext;
    private MyPurchaseAdapter myPurchaseAdapter;
    private int purchaseRefreshTag = 0;

    private File[] imgFiles;
    private ArrayList<String> imgPath;
    private ArrayList<String> myPurchaseImgPath;
    private int screenHeight;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchase);

        myContext = this;

        initView();
        myPurchaseRecyclerRefresh();
        refreshLayout_myPurchase.autoRefresh();
    }

    private void initView() {
        refreshLayout_myPurchase = findViewById(R.id.smartRefresh_myPurchase);
        recyclerView_myPurchase = findViewById(R.id.recycler_myPurchase);
        layout_noImg = findViewById(R.id.layout_myPurchase_noImg);
        layout_noImg.setVisibility(View.INVISIBLE);
        ImageView iv_finishThisActivity = findViewById(R.id.myPurchase_finishThisActivity);
        iv_finishThisActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPurchase.this.finish();
            }
        });
    }

    //本机获取我购买的照片
    private boolean initData() {
        //指定路径
        SDCardPath = GetSDPath.getSDPath(myContext);
        String imgPath = SDCardPath + "/MaskView购买";
        //判断路径是否存在
        File file = new File(imgPath);
        if (file.exists()) {
            //路径存在,获取该路径下所有图片
            imgFiles = getConfirmImages(imgPath);
            if (imgFiles == null || imgFiles.length == 0) {
                //没有图片提示
                layout_noImg.setVisibility(View.VISIBLE);
                refreshLayout_myPurchase.finishRefresh();
                return false;
            } else {
                //文件夹内有照片,判断是否是登陆者购买的照片
                reversToPath();
                if (myPurchaseImgPath.size() > 0) {
                    layout_noImg.setVisibility(View.INVISIBLE);
                    getScreenSize();
                    showMyPurchaseImg();
                    refreshLayout_myPurchase.finishRefresh();
                    return true;
                } else {
                    layout_noImg.setVisibility(View.VISIBLE);
                    refreshLayout_myPurchase.finishRefresh();
                    return false;
                }
            }
        } else {
            //没有图片提示
            layout_noImg.setVisibility(View.VISIBLE);
            refreshLayout_myPurchase.finishRefresh();
            return false;
        }
    }

    //显示图片,绑定适配器
    private void showMyPurchaseImg() {
        recyclerView_myPurchase.setVisibility(View.VISIBLE);
        recyclerView_myPurchase.setHasFixedSize(true);
        recyclerView_myPurchase.setItemAnimator(null);

        GridLayoutManager layoutManager = new GridLayoutManager(myContext, 2);
        recyclerView_myPurchase.setLayoutManager(layoutManager);
        refreshMyPurchaseUI();
    }

    private void refreshMyPurchaseUI() {
        if (myPurchaseAdapter == null) {
            myPurchaseAdapter = new MyPurchaseAdapter(myContext, myPurchaseImgPath, screenWidth, screenHeight);
            recyclerView_myPurchase.setAdapter(myPurchaseAdapter);
        } else {
            myPurchaseAdapter.notifyDataSetChanged();
        }
    }


    //我的购买图片浏览上拉刷新
    private void myPurchaseRecyclerRefresh() {
        refreshLayout_myPurchase.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (purchaseRefreshTag == 0) {
                            recyclerView_myPurchase.setVisibility(View.INVISIBLE);
                            if (initData()) {
                                purchaseRefreshTag++;
                            }
                        } else {
                            if (initData()) {
                                myPurchaseAdapter.replaceAll(myPurchaseImgPath);
                                refreshLayout.finishRefresh();
                            }
                        }
                    }
                }, 1000);
            }
        });
    }

    //获取屏幕的宽高
    private void getScreenSize() {
        //获取手机屏幕的长宽
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
    }

    //获取指定路径下的所有图片
    private File[] getConfirmImages(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            //过滤器
            return folder.listFiles(imageFilter);
        }
        return null;
    }


    //筛选图片的过滤器
    private FileFilter imageFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
        }
    };


    //转换为path
    private void reversToPath() {
        imgPath = new ArrayList<>();
        myPurchaseImgPath = new ArrayList<>();
        for (File imgFile : imgFiles) {
            imgPath.add(imgFile.toString());
        }
        //遍历筛选自己的购买图片
        String imgName;
        for (int i = 0; i < imgPath.size(); i++) {
            imgName = imgPath.get(i).substring(imgPath.get(i).lastIndexOf("/") + 1);
            if (imgName.startsWith("tra-" + UtilParameter.myPhoneNumber)) {
                myPurchaseImgPath.add(imgPath.get(i));
            }
        }
    }
}

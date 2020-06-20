package com.xdlr.maskview.mainView.mgGoods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.MaskView;
import com.xdlr.maskview.mainView.mgGoods.adapter.MyConfirmAdapter;
import com.xdlr.maskview.mainView.mgGoods.adapter.MySellAdapter;
import com.xdlr.maskview.mainView.resetSellPriceTopic.ResetSellImgPrice;
import com.xdlr.maskview.mainView.setSellImgInfo.SetSellImgInfo;
import com.xdlr.maskview.util.GetSDPath;
import com.xdlr.maskview.util.UtilParameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyGoods extends Fragment implements View.OnClickListener {

    private RelativeLayout layout_myConfirm;
    private RelativeLayout layout_mySell;
    private LinearLayout layout_noConfirmImg;
    private LinearLayout layout_noSellImg;
    private LinearLayout layout_noResponse;
    private RefreshLayout refreshLayout_myConfirm;
    private RefreshLayout refreshLayout_mySell;
    private int confirmRefreshTag = 0;
    private int sellRefreshTag = 0;
    private RecyclerView recyclerView_myConfirm;
    private RecyclerView recyclerView_mySell;
    private Button bt_setImgInfo;
    private Button bt_underImg;
    private TextView text1;
    private TextView text2;
    private View view1;
    private View view2;
    private LinearLayout layout_myGoods_text_myConfirm;
    private LinearLayout layout_myGoods_text_mySell;

    private Context myContext;
    private AlertDialog underImgWaitingDialog;  //下架图片等待提示框
    private final static int UNDER_WAITING = 0;  //下架等待标志
    private final static int UNDER_NO_RESPONSE = 1;  //下架时服务器未响应标志
    private int underCount = 0;
    private final static int NO_SELL_IMAGE = 2; //我的上架没有图片标志
    private final static int SHOW_SELL_NO_RESPONSE = 3; //显示上架图片时,服务器未响应标志


    private File[] imgFiles;
    private ArrayList<String> imgPath;
    private ArrayList<String> myConfirmImgPath;
    private ArrayList<String> selectedSellImg;
    private MyConfirmAdapter myConfirmAdapter;
    private MySellAdapter mySellAdapter;
    private boolean isShowChecked;
    private UserRequest ur;
    private ArrayList<String> mySellImgList;
    private ArrayList<String> selectedUnderImg;

    private AlertDialog noConfirmImg;
    private AlertDialog noSellImg;

    private int screenWidth;
    private int screenHeight;

    private String SDCardPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_goods, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        confirmRecyclerRefresh();
        refreshLayout_myConfirm.autoRefresh();
    }


    private void initView() {
        myContext = getActivity();
        layout_myConfirm = Objects.requireNonNull(getActivity()).findViewById(R.id.layout_myGoods_myConfirm);
        layout_mySell = getActivity().findViewById(R.id.layout_myGoods_mySell);
        layout_noConfirmImg = getActivity().findViewById(R.id.layout_myGoods_noConfirmImg);
        layout_noSellImg = getActivity().findViewById(R.id.layout_myGoods_noSellImg);
        layout_noResponse = getActivity().findViewById(R.id.layout_myGoods_showSellImg_noResponse);
        layout_noConfirmImg.setVisibility(View.INVISIBLE);
        layout_noSellImg.setVisibility(View.INVISIBLE);
        layout_noResponse.setVisibility(View.INVISIBLE);
        refreshLayout_myConfirm = getActivity().findViewById(R.id.smartRefresh_myGoods_myConfirm);
        refreshLayout_mySell = getActivity().findViewById(R.id.smartRefresh_myGoods_mySell);
        recyclerView_myConfirm = getActivity().findViewById(R.id.recycler_myGoods_myConfirm);
        recyclerView_mySell = getActivity().findViewById(R.id.recycler_myGoods_mySell);
        bt_setImgInfo = getActivity().findViewById(R.id.bt_myGoods_setInfo);
        bt_underImg = getActivity().findViewById(R.id.bt_myGoods_under);
        bt_setImgInfo.setOnClickListener(this);
        bt_underImg.setOnClickListener(this);
        bt_setImgInfo.getBackground().setAlpha(180);
        bt_underImg.getBackground().setAlpha(180);

        text1 = getActivity().findViewById(R.id.text_1);
        text2 = getActivity().findViewById(R.id.text_2);
        view1 = getActivity().findViewById(R.id.view_1);
        view2 = getActivity().findViewById(R.id.view_2);
        layout_myGoods_text_myConfirm = getActivity().findViewById(R.id.layout_myGoods_text_myConfirm);
        layout_myGoods_text_mySell = getActivity().findViewById(R.id.layout_myGoods_text_mySell);

        bt_setImgInfo.setVisibility(View.INVISIBLE);
        bt_underImg.setVisibility(View.INVISIBLE);
        layout_mySell.setVisibility(View.INVISIBLE);

        ur = new UserRequest();

        layout_myGoods_text_myConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setTextColor(Color.parseColor("#2196f3"));
                view1.setBackgroundColor(Color.parseColor("#2196f3"));
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noSellImg.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.VISIBLE);
                text2.setTextColor(Color.BLACK);
                layout_myConfirm.setVisibility(View.VISIBLE);
                layout_mySell.setVisibility(View.INVISIBLE);
                refreshLayout_myConfirm.autoRefresh();
            }
        });

        layout_myGoods_text_mySell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setTextColor(Color.parseColor("#2196f3"));
                view2.setBackgroundColor(Color.parseColor("#2196f3"));
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noSellImg.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                text1.setTextColor(Color.BLACK);
                layout_myConfirm.setVisibility(View.INVISIBLE);
                layout_mySell.setVisibility(View.VISIBLE);
                mySellRecyclerRefresh();
                refreshLayout_mySell.autoRefresh();
            }
        });
    }

    //本机获取我的确权照片
    private boolean getMyConfirmData() {
        //指定路径
        //String imgPath = Environment.getExternalStorageDirectory().toString() + "/MaskView确权/";
        SDCardPath = GetSDPath.getSDPath(myContext);
        String imgPath = SDCardPath + "/MaskView确权/";
        //判断路径是否存在
        File file = new File(imgPath);
        if (file.exists()) {
            //路径存在,获取该路径下所有图片
            imgFiles = getConfirmImages(imgPath);
            if (imgFiles == null || imgFiles.length == 0) {
                //没有图片提示
                layout_noConfirmImg.setVisibility(View.VISIBLE);
                refreshLayout_myConfirm.finishRefresh();
                return false;
            } else {
                //文件夹内有照片,判断是否是登陆者确权的照片
                reversToPath();
                if (myConfirmImgPath.size() > 0) {
                    layout_noConfirmImg.setVisibility(View.INVISIBLE);
                    getScreenSize();
                    showConfirmImg();
                    refreshLayout_myConfirm.finishRefresh();
                    return true;
                } else {
                    layout_noConfirmImg.setVisibility(View.VISIBLE);
                    refreshLayout_myConfirm.finishRefresh();
                    return false;
                }
            }
        } else {
            //没有图片提示
            layout_noConfirmImg.setVisibility(View.VISIBLE);
            refreshLayout_myConfirm.finishRefresh();
            return false;
        }
    }

    //服务器获取我的上架图片集合
    private void getMySellData() {
        mySellImgList = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getMySellImgList(UtilParameter.myToken);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String imgPath = UtilParameter.IMAGES_IP + jsonArray.get(i);
                                mySellImgList.add(imgPath);
                            }
                            Log.e("--------", jsonArray.length() + "张图片");
                        } else {
                            //刷新UI,没有上架的图片,请在我的确权中选择图片上架
                            noSellImgHandler.sendEmptyMessage(NO_SELL_IMAGE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    noSellImgHandler.sendEmptyMessage(SHOW_SELL_NO_RESPONSE);
                }
            }
        };

        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                getScreenSize();
                showMySellImg();
                refreshLayout_mySell.finishRefresh();
                break;
            }
        }
    }


    private void showConfirmImg() {
        recyclerView_myConfirm.setVisibility(View.VISIBLE);
        recyclerView_myConfirm.setHasFixedSize(true);
        recyclerView_myConfirm.setItemAnimator(null);
        bt_setImgInfo.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(myContext, 3);
        recyclerView_myConfirm.setLayoutManager(layoutManager);
        selectedSellImg = new ArrayList<>();
        refreshConfirmUI();
    }

    private void showMySellImg() {
        recyclerView_mySell.setVisibility(View.VISIBLE);
        recyclerView_mySell.setHasFixedSize(true);
        recyclerView_mySell.setItemAnimator(null);

        GridLayoutManager layoutManager = new GridLayoutManager(myContext, 2);
        recyclerView_mySell.setLayoutManager(layoutManager);
        selectedUnderImg = new ArrayList<>();
        refreshMySellUI();
    }

    private void refreshConfirmUI() {
        if (myConfirmAdapter == null) {
            myConfirmAdapter = new MyConfirmAdapter(myContext, myConfirmImgPath, screenWidth, screenHeight);
            recyclerView_myConfirm.setAdapter(myConfirmAdapter);
        } else {
            myConfirmAdapter.notifyDataSetChanged();
        }
    }

    private void refreshMySellUI() {
        if (mySellAdapter == null) {
            mySellAdapter = new MySellAdapter(myContext, mySellImgList, screenWidth, screenHeight);
            recyclerView_mySell.setAdapter(mySellAdapter);
        } else {
            mySellAdapter.notifyDataSetChanged();
        }
    }

    //确权浏览点击监听
    private void initConfirmListener() {
        //adapter中定义的监听事件　可以根据isShowCheck判断当前状态
        myConfirmAdapter.setOnItemClickListener(new MyConfirmAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, String imgUrl) {
                if (isShowChecked) {
                    if (selectedSellImg.contains(imgUrl)) {
                        selectedSellImg.remove(imgUrl);
                    } else {
                        selectedSellImg.add(imgUrl);
                    }
                } else {
                    //点击我的确权图片--放大图片展示
                    Log.e("------------", "onClick: 放大图片操作");
                }
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                if (isShowChecked) {
                    bt_setImgInfo.setVisibility(View.INVISIBLE);
                    myConfirmAdapter.setShowCheckBox(false);
                    refreshConfirmUI();
                    selectedSellImg.clear();
                } else {
                    bt_setImgInfo.setVisibility(View.VISIBLE);
                    myConfirmAdapter.setShowCheckBox(true);
                    refreshConfirmUI();
                }
                isShowChecked = !isShowChecked;
                return false;
            }
        });
    }

    //我的上架浏览点击监听
    private void initMySellListener() {
        //adapter中定义的监听事件　可以根据isShowCheck判断当前状态
        mySellAdapter.setOnItemClickListener(new MySellAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, String imgUrl) {
                if (isShowChecked) {
                    if (selectedUnderImg.contains(imgUrl)) {
                        selectedUnderImg.remove(imgUrl);
                    } else {
                        selectedUnderImg.add(imgUrl);
                    }
                } else {
                    //我的上架点击某张图片,进入编辑页面,可修改价格和主题
                    Intent intent = new Intent(getActivity(), ResetSellImgPrice.class);
                    intent.putExtra("selectedResetPriceImgUrl", imgUrl);
                    startActivity(intent);
                    Log.e("------------", "onClick: 放大图片操作");
                }
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                if (isShowChecked) {
                    bt_underImg.setVisibility(View.INVISIBLE);
                    mySellAdapter.setShowCheckBox(false);
                    refreshMySellUI();
                    selectedUnderImg.clear();
                } else {
                    bt_underImg.setVisibility(View.VISIBLE);
                    mySellAdapter.setShowCheckBox(true);
                    refreshMySellUI();
                }
                isShowChecked = !isShowChecked;
                return false;
            }
        });
    }

    //确权浏览上拉刷新
    private void confirmRecyclerRefresh() {
        refreshLayout_myConfirm.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (confirmRefreshTag == 0) {
                            //loadingView.setVisibility(View.VISIBLE);
                            recyclerView_myConfirm.setVisibility(View.INVISIBLE);
                            if (getMyConfirmData()) {
                                confirmRefreshTag++;
                                initConfirmListener();
                            }
                        } else {
                            if (getMyConfirmData()) {
                                myConfirmAdapter.replaceAll(myConfirmImgPath);
                                refreshLayout.finishRefresh();
                                initConfirmListener();
                            }
                        }
                    }
                }, 1000);
            }
        });
    }

    //我的上架浏览上拉刷新
    private void mySellRecyclerRefresh() {
        refreshLayout_mySell.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sellRefreshTag == 0) {
                            //loadingView.setVisibility(View.VISIBLE);
                            recyclerView_mySell.setVisibility(View.INVISIBLE);
                            getMySellData();
                            sellRefreshTag++;
                            initMySellListener();
                        } else {
                            getMySellData();
                            mySellAdapter.replaceAll(mySellImgList);
                            refreshLayout.finishRefresh();
                            initMySellListener();
                        }
                    }
                }, 1000);
            }
        });
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
        myConfirmImgPath = new ArrayList<>();
        for (File imgFile : imgFiles) {
            imgPath.add(imgFile.toString());
        }
        //遍历筛选自己的确权图片
        String imgName;
        for (int i = 0; i < imgPath.size(); i++) {
            imgName = imgPath.get(i).substring(imgPath.get(i).lastIndexOf("/") + 1);
            if (imgName.startsWith("con-" + UtilParameter.myPhoneNumber)) {
                myConfirmImgPath.add(imgPath.get(i));
            }
        }
    }

    //获取屏幕的宽高
    private void getScreenSize() {
        //获取手机屏幕的长宽
        Display defaultDisplay = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_myGoods_setInfo:
                setSelectedImgPrice();
                break;
            case R.id.bt_myGoods_under:
                isOrNoUnderAlert();
                break;
        }
    }


    //设置选中图片的价格和主题
    private void setSelectedImgPrice() {
        if (selectedSellImg.size() == 0) {
            Toast.makeText(myContext, "请选择要上架的图片", Toast.LENGTH_SHORT).show();
        } else if (selectedSellImg.size() > 6) {
            Toast.makeText(myContext, "每次最多可上架6张图片", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), SetSellImgInfo.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("selectedSellImg", selectedSellImg);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    //下架选中的图片
    private void underSelectedImg() {
        loadingAlert();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                String imgName;
                for (int i = 0; i < selectedUnderImg.size(); i++) {
                    imgName = selectedUnderImg.get(i).substring(selectedUnderImg.get(i).indexOf("con-"));
                    result = ur.underSellImg(imgName, UtilParameter.myToken);
                    if (!result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String tag = jsonObject.get("result") + "";
                            if (tag.equals("true")) {
                                underCount++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        waitingOrNoResponseHandler.sendEmptyMessage(UNDER_NO_RESPONSE);
                        return;
                    }
                }
            }
        }).start();
    }


    //确认下架提示
    private void isOrNoUnderAlert() {
        if (selectedUnderImg.size() == 0) {
            Toast.makeText(myContext, "请选择要下架的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog isUnderDialog = new AlertDialog.Builder(myContext).
                setMessage("确认下架" + selectedUnderImg.size() + "张图?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求服务器,删除选中的图片
                        underSelectedImg();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        isUnderDialog.show();
    }

    //显示下架进度提示
    private void loadingAlert() {
        View view = View.inflate(myContext, R.layout.sell_waiting_window, null);
        underImgWaitingDialog = new AlertDialog.Builder(myContext).setView(view).create();
        underImgWaitingDialog.setTitle("正在下架处理,请耐心等待......");
        underImgWaitingDialog.show();
        underImgWaitingDialog.setCanceledOnTouchOutside(false);
        waitingOrNoResponseHandler.sendEmptyMessage(UNDER_WAITING);
    }

    //显示下架进度或未响应提示
    private Handler waitingOrNoResponseHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == UNDER_WAITING) {
                TextView tv_now = underImgWaitingDialog.findViewById(R.id.loading_nowCount);
                TextView tv_all = underImgWaitingDialog.findViewById(R.id.loading_allCount);
                if (tv_now != null && tv_all != null) {
                    tv_now.setText(underCount + "");
                    tv_all.setText("/" + selectedUnderImg.size());
                }
                if (underImgWaitingDialog.isShowing()) {
                    waitingOrNoResponseHandler.sendEmptyMessageDelayed(UNDER_WAITING, 1000);
                    if (underCount == selectedUnderImg.size()) {
                        underImgWaitingDialog.cancel();
                        finishAlert();
                    }
                }
            } else if (msg.what == UNDER_NO_RESPONSE) {
                underImgWaitingDialog.cancel();
                AlertDialog noResponseAlertDialog = new AlertDialog.Builder(myContext).setMessage("服务器未响应,请稍后重试")
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

    //下架完成后提示
    private void finishAlert() {
        AlertDialog finishAlertDialog = new AlertDialog.Builder(myContext).setMessage("下架完成")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        refreshLayout_mySell.autoRefresh();
                    }
                }).create();
        finishAlertDialog.show();
        finishAlertDialog.setCanceledOnTouchOutside(false);
    }

    //我的上架图片为空,服务器未响应提示
    private Handler noSellImgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == NO_SELL_IMAGE) {
                layout_noSellImg.setVisibility(View.VISIBLE);
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
            } else if (msg.what == SHOW_SELL_NO_RESPONSE) {
                layout_noResponse.setVisibility(View.VISIBLE);
                layout_noConfirmImg.setVisibility(View.INVISIBLE);
                layout_noSellImg.setVisibility(View.INVISIBLE);
            }
            return false;
        }
    });

}

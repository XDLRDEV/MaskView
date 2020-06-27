package com.xdlr.maskview.mainView.displayHall;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
<<<<<<< HEAD
=======
import android.view.Gravity;
>>>>>>> '测试'
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
<<<<<<< HEAD
=======
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
>>>>>>> '测试'
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.MaskView;
import com.xdlr.maskview.mainView.displayHall.adapter.DisplayHallAdapter;
import com.xdlr.maskview.mainView.displayHall.beans.DisplayHallListBean;
import com.xdlr.maskview.util.UtilParameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DisplayHall extends Fragment {

    private List<DisplayHallListBean> displayHallDatas;  //接收服务器所有数据
    private List<DisplayHallListBean> firstShowDatas;  //第一次显示的数据(3个)
    private DisplayHallAdapter adapter;
    private int allCount;
    private int currentCount;
    private int screenWidth;
    private int screenHeight;
    private RefreshLayout refreshlayout;
    private RecyclerView myRecyclerView;
    private LinearLayout loadingView;
    private LinearLayout layout_noResponse;
    private int tagRefresh = 0;

    private final static int NO_RESPONSE = 0;
    private final static int RESPONSE_SUCCESS = 1;

    private UserRequest ur;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_hall, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        //设置刷新与加载更多监听
        onRefresh();
        //打开界面自动刷新
        refreshlayout.autoRefresh();
    }


    private void initView() {
        getScreenSize();
        refreshlayout = Objects.requireNonNull(getActivity()).findViewById(R.id.smartRefreshLayout_displayHall);
        myRecyclerView = getActivity().findViewById(R.id.recyclerView_displayHall);
        loadingView = getActivity().findViewById(R.id.layout_displayHall_loading);
        layout_noResponse = getActivity().findViewById(R.id.layout_displayHall_noResponse);
        layout_noResponse.setVisibility(View.INVISIBLE);
    }

    //设置下拉刷新和上拉加载监听
    private void onRefresh() {
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (tagRefresh == 0) {
                            loadingView.setVisibility(View.VISIBLE);
                            myRecyclerView.setVisibility(View.INVISIBLE);
                            initData();
                            tagRefresh++;
                        } else {
                            initData();
                            if (displayHallDatas.size() > 0) {
                                adapter.replaceAll(getInitialData());
                            }
                            refreshLayout.finishRefresh();
                        }
                    }
                }, 2000);
            }
        });

        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
<<<<<<< HEAD
                        Log.e("-------------", "currentCount: " + currentCount);
                        Log.e("-------------", "allCount: " + allCount);
=======
>>>>>>> '测试'
                        if (currentCount < allCount) {
                            adapter.addData(adapter.getItemCount(), getMoreData());
                            refreshLayout.finishLoadMore();
                        } else {
<<<<<<< HEAD
                            Toast.makeText(getContext(), "已全部加载!!!", Toast.LENGTH_SHORT).show();
=======
                            Toast toast = Toast.makeText(getContext(), "已全部加载!!!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
>>>>>>> '测试'
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 2000);
            }
        });
<<<<<<< HEAD
=======
        refreshlayout.setRefreshFooter(new BallPulseFooter(getActivity()));
>>>>>>> '测试'
    }

    //请求数据
    private void initData() {
        ur = new UserRequest();
        displayHallDatas = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getDisplayHallInfo(null);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObjectResult = new JSONObject(result);
                        String httpTag = jsonObjectResult.get("result") + "";
                        if (httpTag.equals("true")) {
                            JSONArray jsonData = jsonObjectResult.getJSONArray("data");
                            allCount = jsonData.length();
                            for (int i = 0; i < allCount; i++) {
                                JSONObject jsonObjectData = (JSONObject) jsonData.get(i);
                                DisplayHallListBean data = new DisplayHallListBean();
                                String httpUserName = jsonObjectData.get("UserName") + "";
                                String httpHeadViewPath = jsonObjectData.get("HeadView") + "";
                                JSONArray httpSellImgNameList = (JSONArray) jsonObjectData.get("SellImgName");
                                ArrayList<String> urlList = new ArrayList<>();
                                String singleUrl;
                                for (int k = 0; k < httpSellImgNameList.length(); k++) {
                                    singleUrl = UtilParameter.IMAGES_IP + httpSellImgNameList.get(k);
                                    urlList.add(singleUrl);
                                }
                                data.userName = httpUserName;
                                data.userHeadViewPath = httpHeadViewPath;
                                data.sellImgNameList = urlList;
                                displayHallDatas.add(data);
                            }
                            responseHandler.sendEmptyMessage(RESPONSE_SUCCESS);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    responseHandler.sendEmptyMessage(NO_RESPONSE);
                }
            }
        };
        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                break;
            }
        }
    }

    //展厅界面
    private void showRecyclerView() {
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setItemAnimator(null);

        currentCount = 0;
        //初始显示3个cardView
        firstShowDatas = new ArrayList<>();
        if (displayHallDatas.size() > 0) {
            //初始显示3条数据
            if (allCount < 3) {
                for (int i = 0; i < allCount; i++) {
                    firstShowDatas.add(displayHallDatas.get(i));
                    currentCount++;
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    firstShowDatas.add(displayHallDatas.get(i));
                    currentCount++;
                }
            }
        }

        //布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //垂直方向或水平
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //是否反转
        linearLayoutManager.setReverseLayout(false);
        //布局管理器与RecyclerView绑定
        myRecyclerView.setLayoutManager(linearLayoutManager);
        //创建适配器
        adapter = new DisplayHallAdapter(getActivity(), firstShowDatas, screenWidth, screenHeight);
        //RecyclerView绑定适配器
        myRecyclerView.setAdapter(adapter);
<<<<<<< HEAD

=======
>>>>>>> '测试'
        //refreshlayout.setRefreshFooter(new BallPulseFooter(getActivity()));
    }

    //刷新的时候获取初始数据
    private List<DisplayHallListBean> getInitialData() {
        //刷新时首先将当前数据量清零
        currentCount = 0;
        //打乱顺序
        Collections.shuffle(displayHallDatas);
        List<DisplayHallListBean> list = new ArrayList<>();
        //初始只显示3条数据, 每加载更多一次, 显示1条
        if (allCount < 3) {
            for (int i = 0; i < allCount; i++) {
                list.add(displayHallDatas.get(i));
                currentCount++;
            }
        } else {
            for (int i = 0; i < 3; i++) {
                list.add(displayHallDatas.get(i));
                currentCount++;
            }
        }
        return list;
    }

    //加载更多的时候添加的数据
    private List<DisplayHallListBean> getMoreData() {
        List<DisplayHallListBean> list = new ArrayList<>();
<<<<<<< HEAD
        //每加载更多一次, 显示1条
        for (int i = 0; i < 1; i++) {
            list.add(displayHallDatas.get(currentCount));
            currentCount++;
=======
        int laveCount = allCount - currentCount;
        //每加载更多一次, 显示5条
        if (laveCount >= 6) {
            for (int i = 0; i < 6; i++) {
                list.add(displayHallDatas.get(currentCount));
                currentCount++;
            }
        } else {
            for (int i = 0; i < laveCount; i++) {
                list.add(displayHallDatas.get(currentCount));
                currentCount++;
            }
>>>>>>> '测试'
        }
        return list;
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

    private Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == NO_RESPONSE) {
                loadingView.setVisibility(View.INVISIBLE);
                myRecyclerView.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.VISIBLE);
                showRecyclerView();
                refreshlayout.finishRefresh();
            } else if (msg.what == RESPONSE_SUCCESS) {
                loadingView.setVisibility(View.INVISIBLE);
                layout_noResponse.setVisibility(View.INVISIBLE);
                myRecyclerView.setVisibility(View.VISIBLE);
                showRecyclerView();
                refreshlayout.finishRefresh();
            }
            return false;
        }
    });
}

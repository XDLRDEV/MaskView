package com.xdlr.maskview.mainView.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.SPUtils;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.MaskView;
import com.xdlr.maskview.mainView.login.LoginByPhoneCode;
import com.xdlr.maskview.mainView.myInfo.MyInfo;
import com.xdlr.maskview.mainView.myPurchase.MyPurchase;
import com.xdlr.maskview.mainView.records.MyPurchaseRecords;
import com.xdlr.maskview.mainView.records.MySaleRecords;
import com.xdlr.maskview.mainView.updatePwd.UpdatePwd;
import com.xdlr.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mine extends Fragment implements View.OnClickListener {

    private TextView tv_myPoints;
    private TextView tv_myNickName;
    private ImageView iv_myHeadView;
    private boolean isGetData = false;

    private AlertDialog alertDialog;

    private UserRequest ur;
    private Context myContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

    }

    //每次进入fragment都刷新数据
    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            getMyInfo();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        myContext = getActivity();
        LinearLayout layout_myPurchaseRecords = Objects.requireNonNull(getActivity()).findViewById(R.id.layout_mine_myPurchaseRecords);
        layout_myPurchaseRecords.setOnClickListener(this);
        LinearLayout layout_mySaleRecords = getActivity().findViewById(R.id.layout_mine_mySaleRecords);
        layout_mySaleRecords.setOnClickListener(this);
        LinearLayout layout_myPurchase = getActivity().findViewById(R.id.layout_mine_myPurchase);
        layout_myPurchase.setOnClickListener(this);
        LinearLayout layout_myInfo = getActivity().findViewById(R.id.layout_mine_myInfo);
        layout_myInfo.setOnClickListener(this);
        LinearLayout layout_updatePwd = getActivity().findViewById(R.id.layout_mine_updatePwd);
        layout_updatePwd.setOnClickListener(this);
        tv_myPoints = getActivity().findViewById(R.id.tv_mine_points);
        tv_myNickName = getActivity().findViewById(R.id.tv_mine_myNickName);
        iv_myHeadView = getActivity().findViewById(R.id.mine_myHeadView);
        Button bt_logout = getActivity().findViewById(R.id.bt_mine_logout);
        bt_logout.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void getMyInfo() {
        ur = new UserRequest();
        final String[] myPoints = new String[1];
        final String[] myNickName = new String[1];
        final String[] myHeadViewPath = new String[1];
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getMyInfo(UtilParameter.myToken);
                if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        myPoints[0] = jsonObject.get("points") + "";
                        myNickName[0] = jsonObject.get("userName") + "";
                        myHeadViewPath[0] = jsonObject.get("headViewPath") + "";
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
                tv_myPoints.setText(myPoints[0] + "");
                tv_myNickName.setText(myNickName[0] + "");
                if (!myHeadViewPath[0].equals("")) {
                    String url = UtilParameter.IMAGES_IP + myHeadViewPath[0];
                    Glide.with(this).load(url).placeholder(R.mipmap.head).dontAnimate().into(iv_myHeadView);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_mine_myPurchaseRecords:
                Intent intent_myPurchaseRecords = new Intent(getActivity(), MyPurchaseRecords.class);
                startActivity(intent_myPurchaseRecords);
                break;
            case R.id.layout_mine_mySaleRecords:
                Intent intent_mySaleRecords = new Intent(getActivity(), MySaleRecords.class);
                startActivity(intent_mySaleRecords);
                break;
            case R.id.layout_mine_myPurchase:
                Intent intent_myPurchase = new Intent(getActivity(), MyPurchase.class);
                startActivity(intent_myPurchase);
                break;
            case R.id.layout_mine_myInfo:
                Intent intent_myInfo = new Intent(getActivity(), MyInfo.class);
                startActivity(intent_myInfo);
                break;
            case R.id.layout_mine_updatePwd:
                Intent intent_updatePwd = new Intent(getActivity(), UpdatePwd.class);
                startActivity(intent_updatePwd);
                getActivity().finish();
                break;
            case R.id.bt_mine_logout:
                logout();
                break;
        }
    }

    private void logout() {
        alertDialog = new AlertDialog.Builder(myContext).setMessage("确定要注销吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.clear(myContext);
                        UtilParameter.myNickName = null;
                        UtilParameter.myPhoneNumber = null;
                        UtilParameter.myPoints = null;
                        UtilParameter.myToken = null;
                        Intent intent = new Intent(getActivity(), MaskView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("fragmentID", 0);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }
}

package com.xdlr.maskview.mainView.shoppingCart;

import android.annotation.SuppressLint;
import android.content.Context;
<<<<<<< HEAD
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
=======
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
>>>>>>> '测试'
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
<<<<<<< HEAD
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
=======
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
>>>>>>> '测试'
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.UserRequest;
import com.xdlr.maskview.mainView.confirmOrders.ConfirmOrders;
import com.xdlr.maskview.mainView.shoppingCart.adapter.ShoppingCartAdapter;
import com.xdlr.maskview.mainView.shoppingCart.callBack.OnViewChildClickListener;
import com.xdlr.maskview.mainView.shoppingCart.callBack.OnViewChildDeleteClickListener;
import com.xdlr.maskview.mainView.shoppingCart.callBack.OnViewGroupClickListener;
import com.xdlr.maskview.mainView.shoppingCart.entity.ShoppingCartData;
import com.xdlr.maskview.util.UtilParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.Objects;
>>>>>>> '测试'
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingCart extends Fragment implements View.OnClickListener {

    private ExpandableListView myExpandableListView;
    private TextView tv_selectedCount;
    private TextView tv_selectedMoney;
    private CheckBox cb_allChecked;
    private ShoppingCartAdapter adapter;
    private ShoppingCartData shoppingCartData;
<<<<<<< HEAD
    private ImageView iv_emptyShoppingCart;
    private LinearLayout layout_empty_cart;
    private TextView tv_fail_note;
    List<ShoppingCartData.DataBean> selectData;
    private int allSelectedPrice;
=======
    private LinearLayout layout_empty_cart;
    private LinearLayout layout_no_response;
    private LinearLayout layout_deleteMany;
    private LinearLayout layout_selectedGoodsPrice;
    private TextView tv_edit;
    private TextView tv_exit_edit;
    private Button bt_text;
    List<ShoppingCartData.DataBean> selectData;
    ArrayList<String> selectedDeleteImgNamePath;

    private int allSelectedPrice;
    private ProgressBar deleteMany_loading;
>>>>>>> '测试'

    private Context myContext;
    private UserRequest ur;

<<<<<<< HEAD
=======

    private final static int SUCCESS = 0;
    private final static int NO_DATA = 1;
    private final static int NO_RESPONSE = 2;

>>>>>>> '测试'
    private boolean isGetData = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myContext = getActivity();
        initView();
    }

    //每次进入fragment都刷新数据
    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
<<<<<<< HEAD
=======
            layout_selectedGoodsPrice.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.VISIBLE);
            layout_deleteMany.setVisibility(View.INVISIBLE);
            tv_exit_edit.setVisibility(View.INVISIBLE);
>>>>>>> '测试'
            initData();
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

    private void initView() {
<<<<<<< HEAD
        myExpandableListView = getActivity().findViewById(R.id.cart_expandableListView);
=======
        myExpandableListView = Objects.requireNonNull(getActivity()).findViewById(R.id.cart_expandableListView);
>>>>>>> '测试'
        tv_selectedCount = getActivity().findViewById(R.id.cart_selectedCount);  //选中的总数量
        tv_selectedMoney = getActivity().findViewById(R.id.cart_selectedMoney);  //选中的总价钱
        cb_allChecked = getActivity().findViewById(R.id.cart_cb_allSelected);  //全选与反选
        cb_allChecked.setOnClickListener(this);
        Button bt_submit = getActivity().findViewById(R.id.cart_bt_countMoney);  //确认购买-->跳转确认订单界面
        bt_submit.setOnClickListener(this);
<<<<<<< HEAD
        iv_emptyShoppingCart = getActivity().findViewById(R.id.iv_empty_shoppingCart);
        layout_empty_cart = getActivity().findViewById(R.id.layout_empty_shoppingCart);
        tv_fail_note = getActivity().findViewById(R.id.tv_shopping_car_image_note);

        myExpandableListView.setGroupIndicator(null);
        ur = new UserRequest();
        //请求服务器获取我的购物车数据
        //initData();
    }

    private void initData() {
        final String[] tag = {""};
        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = ur.getShoppingCartInfo(UtilParameter.myToken);
=======
        Button bt_clearCart = getActivity().findViewById(R.id.clear_shoppingCart); //清空购物车
        Button bt_deleteMany = getActivity().findViewById(R.id.deleteMany_cart);  //删除多个购物车信息
        bt_clearCart.setOnClickListener(this);
        bt_deleteMany.setOnClickListener(this);
        layout_no_response = getActivity().findViewById(R.id.layout_shoppingCart_noResponse);
        layout_empty_cart = getActivity().findViewById(R.id.layout_empty_shoppingCart);
        myExpandableListView.setVisibility(View.INVISIBLE);
        layout_empty_cart.setVisibility(View.INVISIBLE);
        layout_no_response.setVisibility(View.INVISIBLE);
        myExpandableListView.setGroupIndicator(null);
        deleteMany_loading = getActivity().findViewById(R.id.deleteMany_loading);
        deleteMany_loading.setVisibility(View.INVISIBLE);
        layout_deleteMany = getActivity().findViewById(R.id.layout_deleteMany);
        layout_deleteMany.setVisibility(View.INVISIBLE);
        layout_selectedGoodsPrice = getActivity().findViewById(R.id.layout_selectedGoodsPrice);
        tv_edit = getActivity().findViewById(R.id.tv_edit_shoppingCart);
        tv_exit_edit = getActivity().findViewById(R.id.tv_exit_edit);
        tv_edit.setOnClickListener(this);
        tv_exit_edit.setOnClickListener(this);
        tv_exit_edit.setVisibility(View.INVISIBLE);
        ur = new UserRequest();
    }

    //获取购物车数据
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = ur.getShoppingCartInfo(UtilParameter.myToken);
                String tag;
>>>>>>> '测试'
                if (!result.equals("")) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
<<<<<<< HEAD
                        tag[0] = jsonObject.get("result") + "";
                        if (tag[0].equals("true")) {
                            shoppingCartData = JSON.parseObject(result, ShoppingCartData.class);
                        } else {
                            shoppingCartData = null;
=======
                        tag = jsonObject.get("result") + "";
                        if (tag.equals("true")) {
                            shoppingCartData = JSON.parseObject(result, ShoppingCartData.class);
                            shoppingCartHandler.sendEmptyMessage(SUCCESS);
                        } else {
                            shoppingCartData = null;
                            shoppingCartHandler.sendEmptyMessage(NO_DATA);
>>>>>>> '测试'
                            Log.e("-----------------", "run: 购物车是空的界面" + result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
<<<<<<< HEAD
                    tag[0] = "requestFail";
                    Log.e("-----------------", "run: 服务器未响应");
                }
            }
        };
        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated() && tag[0].equals("true")) {
                layout_empty_cart.setVisibility(View.INVISIBLE);
                showData();
                break;
            } else if (es.isTerminated() && tag[0].equals("false")) {
                myExpandableListView.setVisibility(View.INVISIBLE);
                iv_emptyShoppingCart.setImageResource(R.mipmap.empty_cart);
                break;
            } else if (es.isTerminated() && tag[0].equals("requestFail")) {
                myExpandableListView.setVisibility(View.INVISIBLE);
                iv_emptyShoppingCart.setImageResource(R.mipmap.request_fail);
                iv_emptyShoppingCart.setAlpha(0.5f);
                iv_emptyShoppingCart.setMaxHeight(60);
                iv_emptyShoppingCart.setMaxWidth(60);
                tv_fail_note.setText(R.string.request_fail);
                break;
            }
        }
    }
=======
                    shoppingCartHandler.sendEmptyMessage(NO_RESPONSE);
                    Log.e("-----------------", "run: 服务器未响应");
                }
            }
        }).start();
    }

    private Handler shoppingCartHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SUCCESS) {
                tv_selectedCount.setText("共0件");
                tv_selectedMoney.setText("¥ 0");
                layout_selectedGoodsPrice.setVisibility(View.VISIBLE);
                tv_edit.setVisibility(View.VISIBLE);
                tv_exit_edit.setVisibility(View.INVISIBLE);
                layout_deleteMany.setVisibility(View.INVISIBLE);
                deleteMany_loading.setVisibility(View.INVISIBLE);
                layout_empty_cart.setVisibility(View.INVISIBLE);
                myExpandableListView.setVisibility(View.VISIBLE);
                layout_no_response.setVisibility(View.INVISIBLE);
                showData();
            } else if (msg.what == NO_DATA) {
                tv_selectedCount.setText("共0件");
                tv_selectedMoney.setText("¥ 0");
                layout_selectedGoodsPrice.setVisibility(View.VISIBLE);
                tv_edit.setVisibility(View.VISIBLE);
                tv_exit_edit.setVisibility(View.INVISIBLE);
                layout_deleteMany.setVisibility(View.INVISIBLE);
                deleteMany_loading.setVisibility(View.INVISIBLE);
                myExpandableListView.setVisibility(View.INVISIBLE);
                layout_empty_cart.setVisibility(View.VISIBLE);
                layout_no_response.setVisibility(View.INVISIBLE);
            } else if (msg.what == NO_RESPONSE) {
                tv_selectedCount.setText("共0件");
                tv_selectedMoney.setText("¥ 0");
                layout_selectedGoodsPrice.setVisibility(View.VISIBLE);
                tv_edit.setVisibility(View.VISIBLE);
                tv_exit_edit.setVisibility(View.INVISIBLE);
                layout_deleteMany.setVisibility(View.INVISIBLE);
                deleteMany_loading.setVisibility(View.INVISIBLE);
                myExpandableListView.setVisibility(View.INVISIBLE);
                layout_empty_cart.setVisibility(View.INVISIBLE);
                layout_no_response.setVisibility(View.VISIBLE);
            }
            return false;
        }
    });
>>>>>>> '测试'


    private void showData() {
        if (shoppingCartData != null && shoppingCartData.getData().size() > 0) {
            adapter = null;
            showExpandData();
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private void showExpandData() {
        adapter = new ShoppingCartAdapter(myContext, myExpandableListView, shoppingCartData.getData());
        myExpandableListView.setAdapter(adapter);
        final int groupCount = myExpandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            myExpandableListView.expandGroup(i);
        }
        final int[] tag = {0, 0};
        final int[] notTag = {0};
        //group的checkbox选择(单组全选)
        adapter.setOnGroupClickListener(new OnViewGroupClickListener() {
            @Override
            public void onGroupCheckBoxClick(boolean isChecked, View view, int groupPosition) {
                shoppingCartData.getData().get(groupPosition).setCheck(isChecked);
                int childLength = shoppingCartData.getData().get(groupPosition).getGoodsInfo().size();
                for (int i = 0; i < childLength; i++) {
                    shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(i).setCheck(isChecked);
                }
                //判断整个购物车是否全选
                if (shoppingCartData.getData().get(groupPosition).isCheck()) {
                    tag[0]++;
                } else if (!shoppingCartData.getData().get(groupPosition).isCheck()) {
                    tag[0]--;
                }
                if (tag[0] == shoppingCartData.getData().size()) {
                    cb_allChecked.setChecked(true);
                } else {
                    cb_allChecked.setChecked(false);
                }
                Log.e("tag[0]", "group: " + tag[0]);
                adapter.notifyDataSetChanged();
                showCommodityCalculation();
            }
        });

        //child的checkbox选择(单个child选择)
        adapter.setOnChildClickListener(new OnViewChildClickListener() {
            @Override
            public void onChildCheckBoxClick(boolean isChecked, View view, int groupPosition, int childPosition) {
                shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).setCheck(isChecked);
                int childLength = shoppingCartData.getData().get(groupPosition).getGoodsInfo().size();
                for (int i = 0; i < childLength; i++) {
                    if (!shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(i).isCheck()) {
                        if (!isChecked) {
                            shoppingCartData.getData().get(groupPosition).setCheck(false);
                        }
                        adapter.notifyDataSetChanged();
                        showCommodityCalculation();
                        return;
                    } else {
                        if (i == (childLength - 1)) {
                            //child和group都选中,即单个group全部选中
                            tag[1]++;
                            shoppingCartData.getData().get(groupPosition).setCheck(isChecked);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                /*for (int i = 0; i < groupCount; i++) {
                    for (int j = 0; j < shoppingCartData.getData().get(i).getGoodsInfo().size(); j++) {
                        if (!shoppingCartData.getData().get(i).getGoodsInfo().get(j).isCheck()) {
                            tag[1]--;
                            break;
                        }
                    }
                }*/
                /*if (shoppingCartData.getData().get(groupPosition).isCheck()) {
                    tag[1]++;
                } else if (!shoppingCartData.getData().get(groupPosition).isCheck()) {
                    tag[1]--;
                }*/
                /*if (tag[1] == shoppingCartData.getData().size()) {
                    cb_allChecked.setChecked(true);
                } else {
                    cb_allChecked.setChecked(false);
                }
                Log.e("tag[1]", "child: " + tag[1]);*/
                showCommodityCalculation();
            }
        });

        //删除某个child
        adapter.setOnChildDeleteClickListener(new OnViewChildDeleteClickListener() {
            @Override
            public void onChildDeleteClick(View view, int groupPosition, int childPosition) {
                //删除当前选中的item(请求服务器删除数据)
                //shoppingCartData.getData().get(groupPosition).getGoodsInfo().remove(childPosition);
                String deletePath = shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).getImgPath();
                String deleteTopic = shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).getImgTopic();
                String deletePrice = shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).getImgPrice();
                //重新获取数据加载
                //initData();
                //Toast.makeText(myContext, "删除的第" + groupPosition + "个组的第" + childPosition + "个", Toast.LENGTH_LONG).show();
                Log.e("----------------", "删除的URL是: " + deletePath);
                Log.e("----------------", "删除的主题是: " + deleteTopic);
                Log.e("----------------", "删除的价格是: " + deletePrice);


                deleteOneToShoppingCart(groupPosition, childPosition);
            }
        });
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showCommodityCalculation() {
        allSelectedPrice = 0;
        int allSelectedNum = 0;
        for (int i = 0; i < shoppingCartData.getData().size(); i++) {
            for (int j = 0; j < shoppingCartData.getData().get(i).getGoodsInfo().size(); j++) {
                if (shoppingCartData.getData().get(i).getGoodsInfo().get(j).isCheck()) {
                    allSelectedPrice += Double.parseDouble(shoppingCartData.getData().get(i).getGoodsInfo().get(j).getImgPrice());
                    allSelectedNum++;
                }
            }
        }
        if (allSelectedPrice == 0) {
            tv_selectedCount.setText("共0件");
            tv_selectedMoney.setText("¥ 0");
            return;
        }
        Log.e("--------------", "allSelectedPrice: " + allSelectedPrice);
        tv_selectedCount.setText("共" + allSelectedNum + "件");
        tv_selectedMoney.setText("¥ " + allSelectedPrice);
    }


    /**
     * 获取选中的所有item,传给确认订单界面
     */
    private void getSelectedData() {
        selectData = new ArrayList<>();
<<<<<<< HEAD
=======
        selectedDeleteImgNamePath = new ArrayList<>(); //选中的删除购物车的图片名称
>>>>>>> '测试'
        ShoppingCartData.DataBean dataBean;
        String selectedSellerName;
        List<ShoppingCartData.DataBean.GoodsInfoBean> goodsInfoBeans;
        for (int i = 0; i < shoppingCartData.getData().size(); i++) {
            dataBean = new ShoppingCartData.DataBean();
            selectedSellerName = shoppingCartData.getData().get(i).getSellerName();
            goodsInfoBeans = new ArrayList<>();
            for (int j = 0; j < shoppingCartData.getData().get(i).getGoodsInfo().size(); j++) {
                if (shoppingCartData.getData().get(i).getGoodsInfo().get(j).isCheck()) {
                    goodsInfoBeans.add(shoppingCartData.getData().get(i).getGoodsInfo().get(j));
<<<<<<< HEAD
=======
                    selectedDeleteImgNamePath.add(shoppingCartData.getData().get(i).getGoodsInfo().get(j).getImgPath());
>>>>>>> '测试'
                }
            }
            if (goodsInfoBeans.size() > 0) {
                dataBean.setSellerName(selectedSellerName);
                dataBean.setGoodsInfo(goodsInfoBeans);
                selectData.add(dataBean);
            }
        }
    }


    //全选与反选
    private void selectedAll() {
<<<<<<< HEAD
        int length = shoppingCartData.getData().size();
        for (int i = 0; i < length; i++) {
            shoppingCartData.getData().get(i).setCheck(cb_allChecked.isChecked());
            List<ShoppingCartData.DataBean.GoodsInfoBean> groups = shoppingCartData.getData().get(i).getGoodsInfo();
            for (int j = 0; j < groups.size(); j++) {
                groups.get(j).setCheck(cb_allChecked.isChecked());
            }
        }
        adapter.notifyDataSetChanged();
        showCommodityCalculation();
=======
        if (shoppingCartData != null) {
            int length = shoppingCartData.getData().size();
            for (int i = 0; i < length; i++) {
                shoppingCartData.getData().get(i).setCheck(cb_allChecked.isChecked());
                List<ShoppingCartData.DataBean.GoodsInfoBean> groups = shoppingCartData.getData().get(i).getGoodsInfo();
                for (int j = 0; j < groups.size(); j++) {
                    groups.get(j).setCheck(cb_allChecked.isChecked());
                }
            }
            adapter.notifyDataSetChanged();
            showCommodityCalculation();
        }
>>>>>>> '测试'
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_cb_allSelected:
                selectedAll();
                break;
            case R.id.cart_bt_countMoney:
                jumpConfirmOrder();
                break;
<<<<<<< HEAD
=======
            case R.id.tv_edit_shoppingCart:
                //批量删除购物车或清空购物车控件显隐
                clearOrDeleteManyCart();
                break;
            case R.id.tv_exit_edit:
                //取消批量删除控件显隐
                exitDeleteMany();
                break;
            case R.id.deleteMany_cart:
                deleteSelected();
                break;
            case R.id.clear_shoppingCart:
                clearShoppingCart();
                break;
        }
    }

    //批量删除购物车或清空购物车控件显隐
    private void clearOrDeleteManyCart() {
        layout_selectedGoodsPrice.setVisibility(View.INVISIBLE);
        layout_deleteMany.setVisibility(View.VISIBLE);
        tv_edit.setVisibility(View.INVISIBLE);
        tv_exit_edit.setVisibility(View.VISIBLE);
    }

    //取消批量删除控件显隐
    private void exitDeleteMany() {
        layout_selectedGoodsPrice.setVisibility(View.VISIBLE);
        layout_deleteMany.setVisibility(View.INVISIBLE);
        tv_edit.setVisibility(View.VISIBLE);
        tv_exit_edit.setVisibility(View.INVISIBLE);
    }

    //删除购物车中选中的
    private void deleteSelected() {
        if (shoppingCartData != null && shoppingCartData.getData().size() > 0) {
            getSelectedData();
            if (selectData != null && selectData.size() > 0) {
                deleteMany_loading.setVisibility(View.VISIBLE);
                ExecutorService es = Executors.newCachedThreadPool();
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < selectedDeleteImgNamePath.size(); i++) {
                            ur.deleteOneShoppingCartItem(selectedDeleteImgNamePath.get(i), UtilParameter.myToken);
                        }
                    }
                };
                es.submit(task);
                es.shutdown();
                while (true) {
                    if (es.isTerminated()) {
                        initData();
                        break;
                    }
                }
            } else {
                Toast toast = Toast.makeText(myContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(myContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    //清空购物车
    private void clearShoppingCart() {
        if (shoppingCartData != null && shoppingCartData.getData().size() > 0) {
            AlertDialog finishAlertDialog = new AlertDialog.Builder(myContext)
                    .setMessage("确定要清空购物车吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ExecutorService es = Executors.newCachedThreadPool();
                            Runnable task = new Runnable() {
                                @Override
                                public void run() {
                                    ur.clearShoppingCart(UtilParameter.myToken);
                                }
                            };
                            es.submit(task);
                            es.shutdown();
                            while (true) {
                                if (es.isTerminated()) {
                                    initData();
                                    break;
                                }
                            }
                        }
                    }).setNegativeButton("再考虑考虑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            finishAlertDialog.show();
            finishAlertDialog.setCanceledOnTouchOutside(false);
        } else {
            Toast toast = Toast.makeText(myContext, "购物车是空的哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void text() {
        if (shoppingCartData != null && shoppingCartData.getData().size() > 0) {
            getSelectedData();
            if (selectData != null && selectData.size() > 0) {
                deleteMany_loading.setVisibility(View.VISIBLE);
                ExecutorService es = Executors.newCachedThreadPool();
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < selectedDeleteImgNamePath.size(); i++) {
                            ur.deleteOneShoppingCartItem(selectedDeleteImgNamePath.get(i), UtilParameter.myToken);
                        }
                    }
                };
                es.submit(task);
                es.shutdown();
                while (true) {
                    if (es.isTerminated()) {
                        initData();
                        break;
                    }
                }
            } else {
                Toast toast = Toast.makeText(myContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(myContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
>>>>>>> '测试'
        }
    }


    /**
     * 删除购物车某条数据
     */
    private void deleteOneToShoppingCart(final int groupPosition, final int childPosition) {

        ExecutorService es = Executors.newCachedThreadPool();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String imgPath = shoppingCartData.getData().get(groupPosition).getGoodsInfo().get(childPosition).getImgPath();
                String result = ur.deleteOneShoppingCartItem(imgPath, UtilParameter.myToken);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String tag = jsonObject.get("result") + "";
                    if (tag.equals("true")) {
                        Log.e("--------", "run: 删除成功");
                    } else if (tag.equals("false")) {
                        Log.e("--------", "run: 删除失败");
                    } else {
                        Log.e("--------", "run: 服务器未响应");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        es.submit(task);
        es.shutdown();

        while (true) {
            if (es.isTerminated()) {
                initData();
                break;
            }
        }
    }


    /**
     * 点击结算,跳转确认订单
     */
    private void jumpConfirmOrder() {
<<<<<<< HEAD
        getSelectedData();
        Intent intent = new Intent(myContext, ConfirmOrders.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_shopping_goods", (Serializable) selectData);
        bundle.putInt("allSelectedPrice", allSelectedPrice);
        intent.putExtras(bundle);
        startActivity(intent);
=======
        if (shoppingCartData != null && shoppingCartData.getData().size() > 0) {
            getSelectedData();
            if (selectData != null && selectData.size() > 0) {
                Intent intent = new Intent(myContext, ConfirmOrders.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_shopping_goods", (Serializable) selectData);
                bundle.putInt("allSelectedPrice", allSelectedPrice);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(myContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(myContext, "还没有选择商品哦!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
>>>>>>> '测试'
    }
}

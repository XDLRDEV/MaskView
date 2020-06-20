package com.xdlr.maskview.mainView.confirmOrders.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xdlr.maskview.R;
import com.xdlr.maskview.mainView.shoppingCart.entity.ShoppingCartData;

import java.util.List;

public class SellerNameAdapter extends RecyclerView.Adapter<SellerNameAdapter.BaseViewHolder> {

    private Context myContext;
    private List<ShoppingCartData.DataBean> datas;
    private GoodsInfoAdapter goodsInfoAdapter;

    public SellerNameAdapter(Context context, List<ShoppingCartData.DataBean> datas) {
        this.myContext = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_group_orders, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private TextView sellerName;
        private RecyclerView goodsInfoRecyclerView;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.item_group_sellName_orders);
            goodsInfoRecyclerView = itemView.findViewById(R.id.recyclerview_goodsInfo);
        }

        public void setData(ShoppingCartData.DataBean data) {
            sellerName.setText(data.getSellerName());
            goodsInfoRecyclerView.setHasFixedSize(true);
            goodsInfoRecyclerView.setItemAnimator(null);

            //布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myContext);
            //垂直方向或水平
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            //是否反转
            linearLayoutManager.setReverseLayout(false);
            //布局管理器与RecyclerView绑定
            goodsInfoRecyclerView.setLayoutManager(linearLayoutManager);
            //创建适配器
            goodsInfoAdapter = new GoodsInfoAdapter(myContext, data.getGoodsInfo());
            //RecyclerView绑定适配器
            goodsInfoRecyclerView.setAdapter(goodsInfoAdapter);
        }
    }
}

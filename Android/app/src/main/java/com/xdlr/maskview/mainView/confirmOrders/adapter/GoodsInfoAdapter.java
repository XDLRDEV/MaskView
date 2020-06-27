package com.xdlr.maskview.mainView.confirmOrders.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.mainView.shoppingCart.entity.ShoppingCartData;
import com.xdlr.maskview.util.UtilParameter;

import com.xdlr.maskview.dao.ZQRoundOvalImageView;


import java.util.List;

public class GoodsInfoAdapter extends RecyclerView.Adapter<GoodsInfoAdapter.BaseViewHolder> {

    private Context myContext;
    private List<ShoppingCartData.DataBean.GoodsInfoBean> datas;

    public GoodsInfoAdapter(Context context, List<ShoppingCartData.DataBean.GoodsInfoBean> datas) {
        this.myContext = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_child_orders, null);
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

        private TextView imgTopic;
        private TextView imgPrice;
        private ZQRoundOvalImageView image;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTopic = itemView.findViewById(R.id.item_child_imgTopic_orders);
            imgPrice = itemView.findViewById(R.id.item_child_imgPrice_orders);
            image = itemView.findViewById(R.id.item_child_image_orders);
            image.setType(ZQRoundOvalImageView.TYPE_ROUND);
            image.setRoundRadius(6);
        }

        public void setData(ShoppingCartData.DataBean.GoodsInfoBean data) {
            imgTopic.setText(data.getImgTopic());
            imgPrice.setText(data.getImgPrice());
            String url = UtilParameter.IMAGES_IP + data.getImgPath();
            Glide.with(myContext).load(url).into(image);
        }
    }
}

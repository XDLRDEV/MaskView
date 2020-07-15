package com.xdlr.maskview.mainView.userInfoAndSellList.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xdlr.maskview.R;
import com.xdlr.maskview.util.UtilParameter;

import java.util.List;

public class UserSellListAdapter extends RecyclerView.Adapter<UserSellListAdapter.BaseViewHolder> {

    private Context mContext;
    private List<String> mSellImgList;

    public UserSellListAdapter(Context context, List<String> sellImgList) {
        this.mContext = context;
        this.mSellImgList = sellImgList;
    }

    public void replaceAll(List<String> list) {
        mSellImgList.clear();
        if (list != null && list.size() > 0) {
            mSellImgList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_user_sell_list, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(mSellImgList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSellImgList != null ? mSellImgList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvItemImage;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvItemImage = itemView.findViewById(R.id.item_iv_userSellList_image);
        }

        public void setData(String imgPath) {
            String url = UtilParameter.IMAGES_IP + imgPath;
            Glide.with(mContext).load(url).dontAnimate().placeholder(R.drawable.loading_anim).override(1000, 800).into(mIvItemImage);
        }
    }
}

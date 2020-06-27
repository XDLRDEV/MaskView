package com.xdlr.maskview.mainView.setSellImgInfo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdlr.maskview.R;

<<<<<<< HEAD
import java.io.File;
import java.util.ArrayList;

public class SellImgInfoAdapter extends RecyclerView.Adapter<SellImgInfoAdapter.BaseViewHolder> {

    private ArrayList<String> selectedImgPath;
    private Context myContext;

    public SellImgInfoAdapter(ArrayList<String> selectedImgPath, Context myContext) {
        this.selectedImgPath = selectedImgPath;
        this.myContext = myContext;
=======
import java.util.List;

public class SellImgInfoAdapter extends RecyclerView.Adapter<SellImgInfoAdapter.BaseViewHolder> {

    private List<Uri> mSelectedImgUri;
    private Context mContext;

    public SellImgInfoAdapter(Context Context, List<Uri> selectedImgUri) {
        this.mContext = Context;
        this.mSelectedImgUri = selectedImgUri;
>>>>>>> '测试'
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        View view = View.inflate(myContext, R.layout.item_set_sell_img_info, null);
=======
        View view = View.inflate(mContext, R.layout.item_set_sell_img_info, null);
>>>>>>> '测试'
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
<<<<<<< HEAD
        holder.setData(selectedImgPath.get(position));
=======
        holder.setData(mSelectedImgUri.get(position));
>>>>>>> '测试'
    }

    @Override
    public int getItemCount() {
<<<<<<< HEAD
        return selectedImgPath != null ? selectedImgPath.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_image;
        private EditText item_et_topic;
        private EditText item_et_price;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_setSellImgInfo_image);
            item_et_topic = itemView.findViewById(R.id.item_setSellImgInfo_topic);
            item_et_price = itemView.findViewById(R.id.item_setSellImgInfo_price);
        }

        public void setData(String imgPath) {
            item_image.setImageURI(Uri.fromFile(new File(imgPath)));
=======
        return mSelectedImgUri != null ? mSelectedImgUri.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvImage;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.item_setSellImgInfo_image);
            EditText mEtTopic = itemView.findViewById(R.id.item_setSellImgInfo_topic);
            EditText mEtPrice = itemView.findViewById(R.id.item_setSellImgInfo_price);
        }

        public void setData(Uri imgUri) {
            mIvImage.setImageURI(imgUri);
>>>>>>> '测试'
        }
    }
}

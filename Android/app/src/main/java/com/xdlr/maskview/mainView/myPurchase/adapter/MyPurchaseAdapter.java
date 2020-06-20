package com.xdlr.maskview.mainView.myPurchase.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdlr.maskview.R;

import java.util.ArrayList;

public class MyPurchaseAdapter extends RecyclerView.Adapter<MyPurchaseAdapter.BaseViewHolder> {

    private Context myContext;
    private ArrayList<String> myPurchaseImgPath;
    private int screenWidth;
    private int screenHeight;

    public MyPurchaseAdapter(Context myContext, ArrayList<String> myPurchaseImgPath, int screenWidth, int screenHeight) {
        this.myContext = myContext;
        this.myPurchaseImgPath = myPurchaseImgPath;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void replaceAll(ArrayList<String> list) {
        myPurchaseImgPath.clear();
        if (list != null && list.size() > 0) {
            myPurchaseImgPath.addAll(list);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(myContext, R.layout.item_my_purchase, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(myPurchaseImgPath.get(position));
    }

    @Override
    public int getItemCount() {
        return myPurchaseImgPath != null ? myPurchaseImgPath.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_image;
        private RelativeLayout item_layout;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_myPurchase_img);
            item_layout = itemView.findViewById(R.id.item_layout_myPurchase);
            //设置item宽高为屏幕宽度的1/2
            ViewGroup.LayoutParams ps = item_image.getLayoutParams();
            ps.width = (screenWidth - 8) / 2;
            ps.height = (screenWidth - 8) / 2;
        }

        public void setData(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            item_image.setImageBitmap(bitmap);
        }
    }
}

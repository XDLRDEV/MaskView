package com.xdlr.maskview.mainView.myPurchase.adapter;

import android.content.Context;
<<<<<<< HEAD
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
=======
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
>>>>>>> '测试'
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdlr.maskview.R;
<<<<<<< HEAD

import java.util.ArrayList;
=======
import com.xdlr.maskview.dao.AvertTwoTouch;
import com.xdlr.maskview.mainView.myPurchase.OneImageView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
>>>>>>> '测试'

public class MyPurchaseAdapter extends RecyclerView.Adapter<MyPurchaseAdapter.BaseViewHolder> {

    private Context myContext;
<<<<<<< HEAD
    private ArrayList<String> myPurchaseImgPath;
    private int screenWidth;
    private int screenHeight;

    public MyPurchaseAdapter(Context myContext, ArrayList<String> myPurchaseImgPath, int screenWidth, int screenHeight) {
        this.myContext = myContext;
        this.myPurchaseImgPath = myPurchaseImgPath;
=======
    private List<Bitmap> myPurchaseImgBitmap;
    private int screenWidth;
    private int screenHeight;

    public MyPurchaseAdapter(Context myContext, List<Bitmap> myPurchaseImgBitmap, int screenWidth, int screenHeight) {
        this.myContext = myContext;
        this.myPurchaseImgBitmap = myPurchaseImgBitmap;
>>>>>>> '测试'
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

<<<<<<< HEAD
    public void replaceAll(ArrayList<String> list) {
        myPurchaseImgPath.clear();
        if (list != null && list.size() > 0) {
            myPurchaseImgPath.addAll(list);
=======
    public void replaceAll(List<Bitmap> list) {
        myPurchaseImgBitmap.clear();
        if (list != null && list.size() > 0) {
            myPurchaseImgBitmap.addAll(list);
>>>>>>> '测试'
        }
        notifyDataSetChanged();
    }

<<<<<<< HEAD

=======
>>>>>>> '测试'
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(myContext, R.layout.item_my_purchase, null);
        return new BaseViewHolder(view);
    }

    @Override
<<<<<<< HEAD
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(myPurchaseImgPath.get(position));
=======
    public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
        holder.setData(myPurchaseImgBitmap.get(position));
        // 我的购买点击图片放大处理
        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Bitmap bitmap = myPurchaseImgBitmap.get(position);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(myContext.getContentResolver(), bitmap, "IMG"+ Calendar.getInstance().getTime(),null));
                    Intent intent = new Intent(myContext, OneImageView.class);
                    intent.putExtra("myBitmap", uri.toString());
                    myContext.startActivity(intent);
                }
            }
        });
>>>>>>> '测试'
    }

    @Override
    public int getItemCount() {
<<<<<<< HEAD
        return myPurchaseImgPath != null ? myPurchaseImgPath.size() : 0;
=======
        return myPurchaseImgBitmap != null ? myPurchaseImgBitmap.size() : 0;
>>>>>>> '测试'
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_image;
<<<<<<< HEAD
        private RelativeLayout item_layout;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_myPurchase_img);
            item_layout = itemView.findViewById(R.id.item_layout_myPurchase);
=======

        private BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_myPurchase_img);
            RelativeLayout item_layout = itemView.findViewById(R.id.item_layout_myPurchase);
>>>>>>> '测试'
            //设置item宽高为屏幕宽度的1/2
            ViewGroup.LayoutParams ps = item_image.getLayoutParams();
            ps.width = (screenWidth - 8) / 2;
            ps.height = (screenWidth - 8) / 2;
        }

<<<<<<< HEAD
        public void setData(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
=======
        public void setData(Bitmap bitmap) {
>>>>>>> '测试'
            item_image.setImageBitmap(bitmap);
        }
    }
}

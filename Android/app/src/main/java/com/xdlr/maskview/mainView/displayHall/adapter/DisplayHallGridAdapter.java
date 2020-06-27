package com.xdlr.maskview.mainView.displayHall.adapter;


import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.dao.AvertTwoTouch;

import com.xdlr.maskview.mainView.purchaseView.PurchaseView;

import java.util.ArrayList;

public class DisplayHallGridAdapter extends BaseAdapter {


    private int screenWidth;
    private int screenHeight;
    private Context myContext;
    private ArrayList<String> datas;

    public DisplayHallGridAdapter(Context context, ArrayList<String> datas, int screenWidth, int screenHeight) {
        myContext = context;
        this.datas = datas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHodler viewHodler;

        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = View.inflate(myContext, R.layout.item_display_hall_grid_view, null);
            viewHodler.imageView = convertView.findViewById(R.id.item_displayHall_grid_img);
            //根据图片的数量设置图片的大小，这里的大小是写的具体数值，也可以获得手机屏幕的尺寸来设置图片的大小
            if (datas.size() == 1) {
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = screenHeight / 2 + 40;
                params.width = screenWidth - 50;
                viewHodler.imageView.setLayoutParams(params);
            } else if (datas.size() == 2) {
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = screenHeight / 3;
                params.width = screenWidth / 2 - 20;
                viewHodler.imageView.setLayoutParams(params);
            } else if (datas.size() == 3) {
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = screenHeight / 3 + 20;
                params.width = screenWidth / 3 - 20;
                viewHodler.imageView.setLayoutParams(params);
            } else {
                ViewGroup.LayoutParams params = viewHodler.imageView.getLayoutParams();
                params.height = screenHeight / 4 + 20;
                params.width = screenWidth / 2 - 20;
                viewHodler.imageView.setLayoutParams(params);
            }
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        //网络加载对应图片
        Glide.with(myContext).load(datas.get(position)).dontAnimate().into(viewHodler.imageView);

        //点击图片查看信息
        viewHodler.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AvertTwoTouch.isFastClick()) {
                    Intent intent = new Intent(myContext, PurchaseView.class);
                    intent.putExtra("selectedImgUrl", datas.get(position));
                    myContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }


    public class ViewHodler {
        ImageView imageView;
    }
}

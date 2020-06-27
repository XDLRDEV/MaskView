package com.xdlr.maskview.mainView.displayHall.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.mainView.displayHall.beans.DisplayHallListBean;
import com.xdlr.maskview.util.UtilParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisplayHallAdapter extends RecyclerView.Adapter<DisplayHallAdapter.BaseViewHolder> {

    private int screenWidth;  //手机屏幕宽度
    private int screenHeight;  //手机屏幕高度
    private Context myContext;
    private List<DisplayHallListBean> datas;  //总数据

    //构造方法
    public DisplayHallAdapter(Context context, List<DisplayHallListBean> datas, int screenWidth, int screenHeight) {
        myContext = context;
        this.datas = datas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


    public void replaceAll(List<DisplayHallListBean> list) {
        datas.clear();
        if (list != null && list.size() > 0) {
            datas.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addData(int position, List<DisplayHallListBean> list) {
        datas.addAll(position, list);
        notifyItemInserted(position);
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_display_hall, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }


    /**
     * 这个方法解决了下拉刷新后图片显示混乱的问题
     */
    public int getItemViewType(int position) {
        return position;
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_userHeadView;  //用户头像
        private TextView tv_userName;  //用户昵称
        private TextView tv_fans;  //用户粉丝数
        private TextView tv_herLike;  //用户关注数
        private GridView gridView;  //图片展示

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_userHeadView = itemView.findViewById(R.id.item_displayHall_user_headView);  //用户头像
            tv_userName = itemView.findViewById(R.id.item_displayHall_userName);  //用户昵称
            tv_fans = itemView.findViewById(R.id.item_displayHall_fans);  //用户粉丝数
            tv_herLike = itemView.findViewById(R.id.item_displayHall_her_like);  //用户关注数
            gridView = itemView.findViewById(R.id.item_displayHall_gridView);  //用户上架的图片
        }


        private void setGridNumColumns(int index) {
            if (index == 1) {
                //一张图片的显示,一行有一个
                gridView.setNumColumns(1);
            } else if (index == 2 || index == 4) {
                //两张或四张图片的显示
                gridView.setNumColumns(2);
            } else {
                //三张图片的显示
                gridView.setNumColumns(3);
            }
        }

        public void setData(DisplayHallListBean displayHallListBean, int position) {

            tv_userName.setText(displayHallListBean.userName);
            if (!displayHallListBean.userHeadViewPath.equals("")) {
                String headUrl = UtilParameter.IMAGES_IP + displayHallListBean.userHeadViewPath;
                Glide.with(myContext).load(headUrl).into(iv_userHeadView);
            }

            //图片展示
            ArrayList<String> allImgUrl = displayHallListBean.sellImgNameList;
            int imgListSize = allImgUrl.size();  //图片总数
            //检查图片数量,随机抽取1,2,3,4张
            Random random = new Random();
            int extractNum = random.nextInt(4) + 1; //生成1-4的随机数
            int index = 0; //计数器
            //检查显示图片的数目,并规定grid行数

            ArrayList<String> finalImgList = new ArrayList<>();
            //先判断生成的数字是否不为0,且小于总数量
            if (extractNum <= imgListSize) {
                while (index != extractNum) {
                    int ran = random.nextInt(imgListSize); //随机下标:0到size-1
                    if (!finalImgList.contains(allImgUrl.get(ran))) {
                        finalImgList.add(allImgUrl.get(ran));
                        index++;
                    }
                }
                setGridNumColumns(extractNum);
            } else {
                finalImgList.addAll(allImgUrl);
                setGridNumColumns(imgListSize);
            }
<<<<<<< HEAD

            Log.e("------------", "finalImgList的图片数量: " + finalImgList);

            DisplayHallGridAdapter gridAdapter = new DisplayHallGridAdapter(myContext, finalImgList, screenWidth, screenHeight);
            //GridViewAdapter gridViewAdapter = new GridViewAdapter(myContext, gridImage.imgArr, screenWidth, screenHeight);
=======
            DisplayHallGridAdapter gridAdapter = new DisplayHallGridAdapter(myContext, finalImgList, screenWidth, screenHeight);
>>>>>>> '测试'
            gridView.setAdapter(gridAdapter);
        }
    }

}

package com.xdlr.maskview.mainView.mgGoods.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdlr.maskview.R;

import java.util.ArrayList;

public class MyConfirmAdapter extends RecyclerView.Adapter<MyConfirmAdapter.BaseViewHolder> {

    private Context myContext;
    private ArrayList<String> confirmImgPath;
    private int screenWidth;
    private int screenHeight;

    private boolean showCheckBox;

    public MyConfirmAdapter(Context myContext, ArrayList<String> confirmImgPath, int screenWidth, int screenHeight) {
        this.myContext = myContext;
        this.confirmImgPath = confirmImgPath;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void replaceAll(ArrayList<String> list) {
        confirmImgPath.clear();
        if (list != null && list.size() > 0) {
            confirmImgPath.addAll(list);
        }
        notifyDataSetChanged();
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }


    //防止Checkbox错乱 做setTag  getTag操作
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();


    //自己写接口，实现点击和长按监听
    public interface onItemClickListener {
        void onClick(View view, String imgUrl);

        boolean onLongClick(View view, int pos);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyConfirmAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MyConfirmAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_my_confirm, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyConfirmAdapter.BaseViewHolder holder, final int position) {
        holder.setData(confirmImgPath.get(position));  //显示所有照片

        //防止复用导致的checkbox显示错乱
        holder.item_confirmCheckbox.setTag(position);
        //判断当前checkbox的状态
        if (showCheckBox) {
            holder.item_confirmCheckbox.setVisibility(View.VISIBLE);
            //防止显示错乱
            holder.item_confirmCheckbox.setChecked(mCheckStates.get(position, false));
        } else {
            holder.item_confirmCheckbox.setVisibility(View.GONE);
            //取消掉Checkbox后不再保存当前选择的状态
            holder.item_confirmCheckbox.setChecked(false);
            mCheckStates.clear();
        }
        //点击监听
        holder.item_confirm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCheckBox) {
                    holder.item_confirmCheckbox.setChecked(!holder.item_confirmCheckbox.isChecked());
                }
                onItemClickListener.onClick(view, confirmImgPath.get(position));
            }
        });
        //长按监听
        holder.item_confirm_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onItemClickListener.onLongClick(view, position);
            }
        });
        //对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.item_confirmCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                if (b) {
                    mCheckStates.put(pos, true);
                } else {
                    mCheckStates.delete(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return confirmImgPath != null ? confirmImgPath.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_confirmImg;
        private CheckBox item_confirmCheckbox;
        private RelativeLayout item_confirm_layout;

        private BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            item_confirmImg = itemView.findViewById(R.id.item_confirm_img);
            item_confirmCheckbox = itemView.findViewById(R.id.item_confirm_checkbox);
            item_confirm_layout = itemView.findViewById(R.id.item_confirm_layout);
            //设置item宽高为屏幕宽度的1/3
            ViewGroup.LayoutParams ps = item_confirmImg.getLayoutParams();
            ps.width = screenWidth / 3;
            ps.height = screenWidth / 3;
        }

        public void setData(String path) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            item_confirmImg.setImageBitmap(bitmap);
        }
    }
}

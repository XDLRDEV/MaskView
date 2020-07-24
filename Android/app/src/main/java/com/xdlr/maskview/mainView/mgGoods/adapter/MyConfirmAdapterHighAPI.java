package com.xdlr.maskview.mainView.mgGoods.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xdlr.maskview.R;
import com.xdlr.maskview.util.API29ImgBean;

import java.util.List;

public class MyConfirmAdapterHighAPI extends RecyclerView.Adapter<MyConfirmAdapterHighAPI.BaseViewHolder> {

    private Context mContext;
    //private List<Bitmap> mConfirmImgBitmapList;
    private List<API29ImgBean> mConfirmImgInfoList;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mShowCheckBox;

    public MyConfirmAdapterHighAPI(Context context, List<API29ImgBean> confirmImgInfoList, int screenWidth, int screenHeight) {
        this.mContext = context;
        this.mConfirmImgInfoList = confirmImgInfoList;
        this.mScreenWidth = screenWidth;
        this.mScreenHeight = screenHeight;
    }

    public void replaceAll(List<API29ImgBean> list) {
        mConfirmImgInfoList.clear();
        if (list != null && list.size() > 0) {
            mConfirmImgInfoList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public boolean isShowCheckBox() {
        return mShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.mShowCheckBox = showCheckBox;
    }

    // 防止Checkbox错乱
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    // 接口:实现点击和长按监听
    public interface onItemClickListener {
        void onClick(View view, API29ImgBean imgBean);

        boolean onLongClick(View view, API29ImgBean imgBean);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyConfirmAdapterHighAPI.onItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyConfirmAdapterHighAPI.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_my_goods_confirm, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyConfirmAdapterHighAPI.BaseViewHolder holder, final int position) {
        holder.setData(mConfirmImgInfoList.get(position));
        // 防止复用导致的checkbox显示错乱
        holder.mChkConfirmCheckbox.setTag(position);
        // 判断当前checkbox的状态
        if (mShowCheckBox) {
            holder.mChkConfirmCheckbox.setVisibility(View.VISIBLE);
            //防止显示错乱
            holder.mChkConfirmCheckbox.setChecked(mCheckStates.get(position, false));
        } else {
            holder.mChkConfirmCheckbox.setVisibility(View.GONE);
            // 取消掉Checkbox后不再保存当前选择的状态
            holder.mChkConfirmCheckbox.setChecked(false);
            mCheckStates.clear();
        }
        // 点击监听
        holder.mRlConfirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShowCheckBox) {
                    holder.mChkConfirmCheckbox.setChecked(!holder.mChkConfirmCheckbox.isChecked());
                }
                mOnItemClickListener.onClick(view, mConfirmImgInfoList.get(position));
            }
        });
        // 长按监听
        holder.mRlConfirmLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return mOnItemClickListener.onLongClick(view, mConfirmImgInfoList.get(position));
            }
        });
        // 对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.mChkConfirmCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        return mConfirmImgInfoList != null ? mConfirmImgInfoList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvConfirmImg;
        private CheckBox mChkConfirmCheckbox;
        private RelativeLayout mRlConfirmLayout;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvConfirmImg = itemView.findViewById(R.id.item_confirm_img);
            mChkConfirmCheckbox = itemView.findViewById(R.id.item_confirm_checkbox);
            mRlConfirmLayout = itemView.findViewById(R.id.item_confirm_layout);
            // 设置item宽高为屏幕宽度的1/3
            ViewGroup.LayoutParams ps = mIvConfirmImg.getLayoutParams();
            ps.width = mScreenWidth / 3;
            ps.height = mScreenWidth / 3;
        }

        public void setData(API29ImgBean imgBean) {
            Glide.with(mContext).load(imgBean.getImgUri()).dontAnimate().into(mIvConfirmImg);
        }
    }
}

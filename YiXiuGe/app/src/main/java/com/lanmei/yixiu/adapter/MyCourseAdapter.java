package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.CheckingInManageBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 我的教程
 */
public class MyCourseAdapter extends SwipeRefreshAdapter<CheckingInManageBean> {

//    private FormatTime formatTime;

    public MyCourseAdapter(Context context) {
        super(context);
//        formatTime = new FormatTime();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_course, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
//        CheckingInManageBean bean = getItem(position);
//        if (bean == null){
//            return;
//        }
//        ViewHolder viewHolder = (ViewHolder)holder;
//        viewHolder.setParameter(bean);
    }

    @Override
    public int getCount() {
        return CommonUtils.quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(CheckingInManageBean bean) {

        }
    }

}

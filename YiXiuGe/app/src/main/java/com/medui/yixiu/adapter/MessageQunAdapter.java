package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.CourseClassifyBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 消息-群聊
 */
public class MessageQunAdapter extends SwipeRefreshAdapter<CourseClassifyBean> {



    public MessageQunAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_qun, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

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


        public void setParameter(final CourseClassifyBean bean) {
        }
    }

}

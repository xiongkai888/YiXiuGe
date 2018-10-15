package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.TeachingAttachmentsBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 教学设备
 */
public class TeachingAttachmentsAdapter extends SwipeRefreshAdapter<TeachingAttachmentsBean> {

    public TeachingAttachmentsAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teaching_attachments, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        TeachingAttachmentsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.icon_iv)
        CircleImageView iconIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.status_tv)
        TextView statusTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(TeachingAttachmentsBean bean) {
            ImageHelper.load(context, bean.getIcon(), iconIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
            nameTv.setText(bean.getName());
            switch (bean.getStatus()) {
                case "1":
                    statusTv.setText(R.string.unused);
                    break;
                case "2":
                    statusTv.setText(R.string.in_use);
                    break;
                case "3":
                    statusTv.setText(R.string.in_maintenance);
                    break;
            }
        }

    }

}

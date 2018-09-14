package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.NoticeBean;
import com.lanmei.yixiu.ui.home.activity.NoticeDetailsActivity;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 公告通知
 */
public class NoticeAdapter extends SwipeRefreshAdapter<NoticeBean> {

    FormatTime time;

    public NoticeAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        NoticeBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title_et)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.notice_details_tv)
        TextView noticeDetailsTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final NoticeBean bean) {
            titleTv.setText(bean.getTitle());
            contentTv.setText(bean.getIntro());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.formatterTime());
            L.d(L.TAG,"label:"+bean.getLabel());
            noticeDetailsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", bean);
                    IntentUtil.startActivity(context, NoticeDetailsActivity.class, bundle);
                }
            });
        }
    }

}

package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.TestListBean;
import com.medui.yixiu.ui.teacher.activity.TestItemActivity;
import com.medui.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的评估
 */
public class TestsListAdapter extends SwipeRefreshAdapter<TestListBean> {

    private FormatTime formatTime;

    public TestsListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tests_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final TestListBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getStatus() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", bean);
                    IntentUtil.startActivity(context, TestItemActivity.class, bundle);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.status_tv)
        TextView statusTv;
        @InjectView(R.id.number_tv)
        TextView numberTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final TestListBean bean) {

            titleTv.setText(bean.getTitle());
            timeTv.setText(String.format(context.getString(R.string.start_and_time), formatTime.formatterTime(bean.getStarttime()), formatTime.formatterTime(bean.getEndtime())));
            int status = bean.getStatus();
            switch (status) {
                case 0:
                    statusTv.setText(R.string.not_started);
                    break;
                case 1:
                    statusTv.setText(R.string.in_progress);
                    break;
                case 2:
                    statusTv.setText(R.string.finished);
                    break;
            }
            numberTv.setText(String.format(context.getString(R.string.submit_num), bean.getSubmit_num() + "", bean.getNumber() + ""));
        }
    }

}

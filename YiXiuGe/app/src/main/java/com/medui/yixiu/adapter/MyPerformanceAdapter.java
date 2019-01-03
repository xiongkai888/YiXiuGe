package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.PerformanceBean;
import com.medui.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.widget.FormatTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的成绩
 */
public class MyPerformanceAdapter extends SwipeRefreshAdapter<PerformanceBean> {


    private FormatTime time;

    public MyPerformanceAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
        time.setApplyPattern("yyyy.MM");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_performance, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        PerformanceBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.performance_tv)
        FormatTextView performanceTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.teacher_remark_tv)
        TextView teacherRemarkTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(PerformanceBean bean) {
            teacherRemarkTv.setText(String.format(context.getString(R.string.teacher_remark),bean.getComment()));
            performanceTv.setTextValue(bean.getGrade());
            timeTv.setText(String.format(context.getString(R.string.start_and_time), time.formatterTime(bean.getStarttime()), time.formatterTime(bean.getEndtime())));
        }
    }

}

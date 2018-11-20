package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.MyCheckingBean;
import com.medui.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 考勤
 */
public class CheckInAdapter extends SwipeRefreshAdapter<MyCheckingBean> {

    private FormatTime formatTime;

    public CheckInAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
        formatTime.setApplyPattern("MM/dd");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check_in, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        MyCheckingBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.date_tv)
        TextView dateTv;
        @InjectView(R.id.attend_time_tv)
        TextView attendTimeTv;//打卡时间
        @InjectView(R.id.explain_tv)
        TextView explainTv;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(MyCheckingBean bean) {
            formatTime.setTime(bean.getAddtime());
            dateTv.setText(formatTime.getTimeForWeek());

            formatTime.setTime(bean.getAttend_time());
            attendTimeTv.setText(formatTime.formatterTime());
            explainTv.setText(bean.getExplain());
        }
    }

}

package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.CheckingInManageBean;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 考勤管理
 */
public class CheckInManageAdapter extends SwipeRefreshAdapter<CheckingInManageBean> {

    private FormatTime formatTime;

    public CheckInManageAdapter(Context context) {
        super(context);
        formatTime = new FormatTime();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check_in_manage, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        CheckingInManageBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.realname_tv)
        TextView realnameTv;
        @InjectView(R.id.attend_time_tv)
        TextView attendTimeTv;
        @InjectView(R.id.attend_type_tv)
        TextView attendTypeTv;
        @InjectView(R.id.explain_tv)
        TextView explainTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(CheckingInManageBean bean) {
            realnameTv.setText(bean.getRealname());
            formatTime.setTime(bean.getAttend_time());
            attendTimeTv.setText(formatTime.formatterCheckIn());
            attendTypeTv.setText(bean.getAttend_type());
            explainTv.setText(bean.getExplain());
        }
    }

}

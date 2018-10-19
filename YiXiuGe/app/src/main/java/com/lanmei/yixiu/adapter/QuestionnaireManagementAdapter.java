package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 问题调查管理
 */
public class QuestionnaireManagementAdapter extends SwipeRefreshAdapter<QuestionnaireManagementBean> {

    private FormatTime formatTime;

    public QuestionnaireManagementAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_questionnaaire_management, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        QuestionnaireManagementBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);

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


        public void setParameter(QuestionnaireManagementBean bean) {
            titleTv.setText(bean.getTitle());
            timeTv.setText(formatTime.formatterTime(bean.getStarttime())+" -- " +formatTime.formatterTime(bean.getEndtime()));
            switch (bean.getStatus()){
                case "0":
                    statusTv.setText("正在进行");
                    break;
                case "1":
                    statusTv.setText("已结束");
                    break;
                case "2":
                    statusTv.setText("未开始");
                    break;
            }
            numberTv.setText(bean.getSubmit_num()+"人/"+bean.getNumber()+"人");
        }
    }

}

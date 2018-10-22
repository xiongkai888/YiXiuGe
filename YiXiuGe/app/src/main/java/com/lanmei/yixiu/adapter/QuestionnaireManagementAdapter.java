package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 问题调查管理
 */
public class QuestionnaireManagementAdapter extends SwipeRefreshAdapter<QuestionnaireManagementBean> {

    private FormatTime formatTime;
    private boolean isStudent;//true 学生  false 老师

    public void setStudent(boolean student) {
        isStudent = student;
    }

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
        final QuestionnaireManagementBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!isStudent){
//                    return;
//                }
//                if (!StringUtils.isSame(CommonUtils.isOne,bean.getStatus())){
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("bean", bean);
//                IntentUtil.startActivity(context, AnswerQuestionnaireActivity.class,bundle);
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
        @InjectView(R.id.remark_tv)
        TextView remarkTv;
        @InjectView(R.id.ll_remark)
        LinearLayout llRemark;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(QuestionnaireManagementBean bean) {
            titleTv.setText(bean.getTitle());
            timeTv.setText(formatTime.formatterTime(bean.getStarttime()) + " -- " + formatTime.formatterTime(bean.getEndtime()));
            String status = bean.getStatus();
            if (StringUtils.isEmpty(status)) {
                status = CommonUtils.isZero;
            }
            switch (status) {
                case CommonUtils.isZero:
                    statusTv.setText("未开始");
                    break;
                case CommonUtils.isOne:
                    statusTv.setText("正在进行");
                    break;
                case CommonUtils.isTwo:
                    statusTv.setText("未开始");
                    break;
            }
            if (isStudent) {
                numberTv.setVisibility(View.GONE);
            } else {
                numberTv.setText(bean.getSubmit_num() + "人/" + bean.getNumber() + "人");
            }
            if (StringUtils.isEmpty(bean.getContent())) {
                llRemark.setVisibility(View.GONE);
            } else {
                llRemark.setVisibility(View.VISIBLE);
                remarkTv.setText(bean.getContent());
            }
        }
    }

}

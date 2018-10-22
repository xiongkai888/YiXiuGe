package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 学生回答调查问卷
 */
public class AnswerQuestionnaireAdapter extends SwipeRefreshAdapter<QuestionnaireManagementBean.QuestBean> {

    public AnswerQuestionnaireAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_answer_questionnaaire, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        QuestionnaireManagementBean.QuestBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;

        @InjectView(R.id.content_et)
        EditText contentEt;
        @InjectView(R.id.ll_answer)
        FrameLayout llAnswer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(QuestionnaireManagementBean.QuestBean bean) {

            titleTv.setText(bean.getTitle());
            if (StringUtils.isSame(bean.getType(), CommonUtils.isOne)) {
                recyclerView.setVisibility(View.VISIBLE);
                llAnswer.setVisibility(View.GONE);
                AnswerQuestionnaireItemAdapter adapter = new AnswerQuestionnaireItemAdapter(context);
                recyclerView.setNestedScrollingEnabled(false);
                adapter.setData(bean.getSelect());
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                llAnswer.setVisibility(View.VISIBLE);
            }

        }
    }

}

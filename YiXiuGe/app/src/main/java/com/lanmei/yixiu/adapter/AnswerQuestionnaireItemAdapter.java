package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.QuestionnaireManagementBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 回答问卷问题选项
 */
public class AnswerQuestionnaireItemAdapter extends SwipeRefreshAdapter<QuestionnaireManagementBean.QuestBean.SelectBean> {


    public AnswerQuestionnaireItemAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_students, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final QuestionnaireManagementBean.QuestBean.SelectBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isSelect()){
                    return;
                }
                List<QuestionnaireManagementBean.QuestBean.SelectBean> list = getData();
                for (QuestionnaireManagementBean.QuestBean.SelectBean selectBean:list){
                    selectBean.setSelect(false);
                }
                bean.setSelect(true);
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.select_iv)
        ImageView selectIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(QuestionnaireManagementBean.QuestBean.SelectBean bean) {
            nameTv.setText(bean.getText());
            selectIv.setImageResource(bean.isSelect()?R.drawable.pay_on:R.drawable.pay_off);
        }
    }

}

package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.AddQuestionnaireOptionBean;
import com.lanmei.yixiu.bean.QuestionnaireSubjectBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 添加问卷题目
 */
public class QuestionnaireSubjectAdapter extends SwipeRefreshAdapter<QuestionnaireSubjectBean> {

    public QuestionnaireSubjectAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_questionnaaire_subject, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        QuestionnaireSubjectBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.root)
        LinearLayout root;
        @InjectView(R.id.ll_question)
        LinearLayout llQuestion;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(QuestionnaireSubjectBean bean) {
            root.removeAllViews();

            titleTv.setText(bean.getTitle());
            if (StringUtils.isSame(bean.getType(), CommonUtils.isOne)){
                llQuestion.setVisibility(View.VISIBLE);
                List<AddQuestionnaireOptionBean> list = bean.getList();
                if (!StringUtils.isEmpty(list)){
                    int size = list.size();
                    for (int i = 0;i<size;i++){
                        TextView view = new TextView(context);
                        view.setText((i+1)+"、"+list.get(i).getOption());
                        root.addView(view);
                    }
                }
            }else {
                llQuestion.setVisibility(View.GONE);
            }

        }
    }

}

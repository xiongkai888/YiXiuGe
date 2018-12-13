package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.StudentTestAnswerBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 学生线下考试题目
 */
public class StudentTestTopicAdapter extends SwipeRefreshAdapter<StudentTestAnswerBean> {

    public StudentTestTopicAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_student_text_topic, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final StudentTestAnswerBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(StudentTestAnswerBean bean) {

        }
    }

}

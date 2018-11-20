package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.CourseClassifyBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 选择考试题目
 */
public class ExamTopicsAdapter extends SwipeRefreshAdapter<CourseClassifyBean> {

    public ExamTopicsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exam_topics, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
//        final CourseClassifyBean bean = getItem(position);
//        if (bean == null) {
//            return;
//        }
//        ViewHolder viewHolder = (ViewHolder) holder;
//        viewHolder.setParameter(bean);
    }


    @Override
    public int getCount() {
        return CommonUtils.quantity * 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(CourseClassifyBean bean) {

        }
    }


}

package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.ExaminationResultBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 考试结果 答案
 */
public class ExaminationResultAdapter extends SwipeRefreshAdapter<ExaminationResultBean> {

    public ExaminationResultAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_examination_result, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final ExaminationResultBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean,position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.item_tv)
        TextView itemTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(ExaminationResultBean bean,int position) {
            itemTv.setText(""+(position+1));

            itemTv.setBackgroundResource(bean.isRight() ? R.drawable.circle_result_off : R.drawable.circle_topic_off);
            itemTv.setTextColor(bean.isRight() ? context.getResources().getColor(R.color.color0cc215) : context.getResources().getColor(R.color.colorFF4A4A));
        }
    }


}

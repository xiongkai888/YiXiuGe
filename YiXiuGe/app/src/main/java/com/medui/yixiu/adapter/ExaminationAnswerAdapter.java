package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.ExaminationAnswerBean;
import com.medui.yixiu.helper.ClickAnswerListener;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 考试答案
 */
public class ExaminationAnswerAdapter extends SwipeRefreshAdapter<ExaminationAnswerBean> {


    public ExaminationAnswerAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_examination_answer, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder,final int position) {
        final ExaminationAnswerBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.item_tv)
        TextView itemTv;
        @InjectView(R.id.topic_tv)
        TextView topicTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(ExaminationAnswerBean bean) {

            itemTv.setText(String.valueOf(getAdapterPosition()+1));
            topicTv.setText(bean.getTopic());
        }
    }

    private ClickAnswerListener listener;


    public void setClickAnswerListener(ClickAnswerListener listener){
        this.listener = listener;
    }

}

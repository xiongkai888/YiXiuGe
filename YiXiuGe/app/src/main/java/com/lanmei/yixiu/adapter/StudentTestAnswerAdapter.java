package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.StudentTestAnswerBean;
import com.lanmei.yixiu.helper.ClickAnswerListener;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 学生线下考试答案
 */
public class StudentTestAnswerAdapter extends SwipeRefreshAdapter<StudentTestAnswerBean> {


    public StudentTestAnswerAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_examination_answer, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder,final int position) {
        final StudentTestAnswerBean bean = getItem(position);
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


        public void setParameter(StudentTestAnswerBean bean) {
            itemTv.setText(String.valueOf(getAdapterPosition()+1));
            if (!StringUtils.isSame(CommonUtils.isOne,bean.getType())){
                CommonUtils.setCompoundDrawables(context,topicTv,0,0,0);
                topicTv.setText(bean.getAnswer());
            }else {
                if (bean.isClick()){
                    topicTv.setText("");
                    CommonUtils.setCompoundDrawables(context,topicTv,bean.isRight()?R.drawable.test_right:R.drawable.test_wrong,0,0);
                }
            }
        }
    }

    private ClickAnswerListener listener;

    public void setClickAnswerListener(ClickAnswerListener listener){
        this.listener = listener;
    }

}

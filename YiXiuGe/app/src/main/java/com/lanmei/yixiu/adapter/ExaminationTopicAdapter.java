package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.ExaminationTopicBean;
import com.lanmei.yixiu.helper.ExaminationPresenter;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 考试选项
 */
public class ExaminationTopicAdapter extends SwipeRefreshAdapter<ExaminationTopicBean> {


    private boolean isMultiterm;//是否是多选项
    private ExaminationPresenter presenter;

    public void setPresenter(ExaminationPresenter presenter) {
        this.presenter = presenter;
    }

    public ExaminationTopicAdapter(Context context) {
        super(context);
    }

    public void setMultiterm(boolean multiterm) {
        isMultiterm = multiterm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_examination_topic, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final ExaminationTopicBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultiterm) {//多选
                    bean.setSelect(!bean.isSelect());
                    notifyDataSetChanged();
                } else {//单选
                    if (bean.isSelect()) {
                        return;
                    }
                    List<ExaminationTopicBean> list = getData();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        ExaminationTopicBean examinationTopicBean = list.get(i);
                        if (examinationTopicBean.isSelect()) {
                            examinationTopicBean.setSelect(false);
                        }
                    }
                    bean.setSelect(!bean.isSelect());
                    notifyDataSetChanged();
                }
                presenter.getAnswerAtTopic(getData());
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


        public void setParameter(ExaminationTopicBean bean, int position) {
            itemTv.setBackgroundResource(bean.isSelect() ? R.drawable.circle_topic_on : R.drawable.circle_topic_off);
            itemTv.setTextColor(bean.isSelect() ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.colorFF4A4A));

            itemTv.setText(CommonUtils.getLetterByPosition(context, position));
            topicTv.setText(bean.getTopic());
        }
    }


}

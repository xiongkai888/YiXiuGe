package com.medui.yixiu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.QuestionnaireSubjectBean;
import com.medui.yixiu.utils.AKDialog;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

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

        @InjectView(R.id.delete_iv)
        ImageView deleteIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(QuestionnaireSubjectBean bean) {

            titleTv.setText(bean.getTitle());
            if (StringUtils.isSame(bean.getType(), CommonUtils.isOne)){
                recyclerView.setVisibility(View.VISIBLE);
                SelectQuestionItemAdapter adapter = new SelectQuestionItemAdapter(context);
                recyclerView.setNestedScrollingEnabled(false);
                adapter.setData(bean.getSelect());
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }else {
                recyclerView.setVisibility(View.GONE);
            }
            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AKDialog.getAlertDialog(context, "确认删除？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = getAdapterPosition();
                            getData().remove(position);
                            notifyItemChanged(position);
                        }
                    });

//                    if (listener != null){
//                        listener.onClick(getAdapterPosition());
//                    }
                }
            });
        }
    }

//    public ClickAnswerListener listener;
//
//    public void setClickAnswerListener(ClickAnswerListener listener){
//        this.listener = listener;
//    }


}

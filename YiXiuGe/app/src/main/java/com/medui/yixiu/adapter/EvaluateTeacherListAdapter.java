package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.EvaluateListBean;
import com.medui.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 评价列表（教师端）
 */
public class EvaluateTeacherListAdapter extends SwipeRefreshAdapter<EvaluateListBean> {

    private FormatTime formatTime;

    public EvaluateTeacherListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
        formatTime.setApplyToAllTime();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evaluate_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        EvaluateListBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        CircleImageView picIv;
        @InjectView(R.id.realname_tv)
        TextView realnameTv;
        @InjectView(R.id.ratingbar)
        RatingBar ratingbar;
        @InjectView(R.id.grade_tv)
        TextView gradeTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(EvaluateListBean bean) {
            ImageHelper.load(context,bean.getPic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            realnameTv.setText(bean.getRealname());
            ratingbar.setRating(Float.parseFloat(bean.getGrade()));
            gradeTv.setText(String.format(context.getString(R.string.grade),bean.getGrade()));
            contentTv.setText(bean.getContent());
            formatTime.setTime(bean.getAddtime());
            timeTv.setText(formatTime.formatterTime());
        }
    }

}

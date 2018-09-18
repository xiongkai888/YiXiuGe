package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.EvaluateListBean;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CircleImageView;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 评价列表
 */
public class EvaluateListAdapter extends SwipeRefreshAdapter<EvaluateListBean> {


    private FormatTime formatTime;
    private SimpleDateFormat format;

    public EvaluateListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime();
        format = formatTime.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evaluate, parent, false));
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
            contentTv.setText(bean.getContent());
            formatTime.setTime(bean.getAddtime());
            timeTv.setText(formatTime.formatterTime(format));

            float grade = Float.parseFloat(bean.getGrade());
            ratingbar.setRating(grade);
            gradeTv.setText(String.format(context.getString(R.string.grade),grade+""));
        }
    }

}

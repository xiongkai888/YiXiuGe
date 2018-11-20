package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.TeacherBean;
import com.medui.yixiu.ui.home.activity.TeacherDetailsSubActivity;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 老师
 */
public class TeacherAdapter extends SwipeRefreshAdapter<TeacherBean> {

    public TeacherAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final TeacherBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, TeacherDetailsSubActivity.class,bean.getId());
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        CircleImageView picIv;
        @InjectView(R.id.realname_tv)
        TextView realnameTv;
        @InjectView(R.id.teachingage_tv)
        TextView teachingageTv;
        @InjectView(R.id.info_tv)
        TextView infoTv;
        @InjectView(R.id.rating_bar)
        RatingBar ratingBar;
        @InjectView(R.id.grade_tv)
        TextView gradeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(TeacherBean bean) {
            ImageHelper.load(context,bean.getPic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            realnameTv.setText(bean.getRealname());
            teachingageTv.setText(String.format(context.getString(R.string.teaching_age),bean.getTeachingage()+""));
            infoTv.setText(String.format(context.getString(R.string.teacher_info), StringUtils.isSame(bean.getSex(), CommonUtils.isOne)?context.getString(R.string.man):context.getString(R.string.woman),bean.getAge(),bean.getCityname()));
            float grade = Float.parseFloat(bean.getGrade());
            ratingBar.setRating(grade);
            gradeTv.setText(String.format(context.getString(R.string.grade),grade+""));
        }
    }

}

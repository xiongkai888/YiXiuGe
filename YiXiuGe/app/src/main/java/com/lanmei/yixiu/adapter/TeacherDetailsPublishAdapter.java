package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.TeacherDetailsBean;
import com.lanmei.yixiu.ui.teacher.activity.CourseListActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 老师详情 发布列表
 */
public class TeacherDetailsPublishAdapter extends SwipeRefreshAdapter<TeacherDetailsBean.VideoBean> {


    private TeacherDetailsBean detailsBean;//老师详情

    public void setDetailsBean(TeacherDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
    }

    public TeacherDetailsPublishAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teacher_details_publish, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        TeacherDetailsBean.VideoBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsBean == null){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("id",detailsBean.getId());
                bundle.putString("realname",detailsBean.getRealname());
                IntentUtil.startActivity(context, CourseListActivity.class,bundle);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.image)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(TeacherDetailsBean.VideoBean bean) {
            ImageHelper.load(context,bean.getPic(),imageView,null,true,R.drawable.default_pic,R.drawable.default_pic);
        }
    }

}

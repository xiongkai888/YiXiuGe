package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.CourseClassifyBean;
import com.medui.yixiu.ui.teacher.activity.ExaminationDetailsActivity;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 考试管理
 */
public class ExaminationManagementAdapter extends SwipeRefreshAdapter<CourseClassifyBean> {



    public ExaminationManagementAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_examination_management, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.examinationDetailsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, ExaminationDetailsActivity.class);
            }
        });
    }


    @Override
    public int getCount() {
        return CommonUtils.quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.examination_details_tv)
        TextView examinationDetailsTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final CourseClassifyBean bean) {

        }
    }

}

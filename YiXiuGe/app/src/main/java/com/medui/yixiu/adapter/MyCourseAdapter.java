package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.CourseClassifyListBean;
import com.medui.yixiu.bean.MyCourseBean;
import com.medui.yixiu.ui.home.activity.CourseDetailsActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的教程
 */
public class MyCourseAdapter extends SwipeRefreshAdapter<MyCourseBean> {

//    private FormatTime formatTime;
//    private SimpleDateFormat format;

    public MyCourseAdapter(Context context) {
        super(context);
//        formatTime = new FormatTime();
//        format = formatTime.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_course, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        MyCourseBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final MyCourseBean bean) {
            ImageHelper.load(context,bean.getPic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            titleTv.setText(bean.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseClassifyListBean classifyListBean = new CourseClassifyListBean();
                    classifyListBean.setVideo(bean.getVideo());
                    classifyListBean.setPic(bean.getPic());
                    classifyListBean.setTitle(bean.getTitle());
                    classifyListBean.setId(bean.getId());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",classifyListBean);
                    IntentUtil.startActivity(context, CourseDetailsActivity.class,bundle);
                }
            });
//            formatTime.setTime(bean.getAddtime());
//            timeTv.setText(formatTime.formatterTime(format));
        }
    }

}

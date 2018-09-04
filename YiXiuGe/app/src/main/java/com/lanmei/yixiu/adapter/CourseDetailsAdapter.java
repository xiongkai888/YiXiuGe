package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.bean.HomeBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 教程详情
 */
public class CourseDetailsAdapter extends SwipeRefreshAdapter<HomeBean> {

    public int TYPE_BANNER = 100;
    private CourseClassifyListBean courseClassifyListBean;

    public void setCourseClassifyListBean(CourseClassifyListBean courseClassifyListBean) {
        this.courseClassifyListBean = courseClassifyListBean;

    }

    public CourseDetailsAdapter(Context context) {
        super(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            return new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_course, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(null);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final HomeBean bean) {
        }
    }


    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }


    //头部
    public class BannerViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.member_pic_iv)
        CircleImageView memberPicIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.view_tv)
        TextView viewTv;
        @InjectView(R.id.addtime_tv)
        TextView addtimeTv;
        @InjectView(R.id.reviews_tv)
        TextView reviewsTv;
        @InjectView(R.id.like_tv)
        TextView likeTv;
        @InjectView(R.id.subscribe_tv)
        TextView subscribeTv;

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter() {
            ImageHelper.load(context,courseClassifyListBean.getMemberpic(),memberPicIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            nameTv.setText(courseClassifyListBean.getNickname());
            titleTv.setText(courseClassifyListBean.getTitle());
            viewTv.setText(courseClassifyListBean.getView());
            FormatTime time = new FormatTime(courseClassifyListBean.getAddtime());
            addtimeTv.setText(time.getAgoDateFomat());
            reviewsTv.setText(courseClassifyListBean.getReviews());
            likeTv.setText(courseClassifyListBean.getLike());
            subscribeTv.setText(courseClassifyListBean.getSubscribe()+"");
        }

    }

    @Override
    public int getCount() {
        return CommonUtils.quantity * 3;
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (courseClassifyListBean == null){
            return;
        }
        BannerViewHolder viewHolder = (BannerViewHolder) holder;
        viewHolder.setParameter();
    }


}

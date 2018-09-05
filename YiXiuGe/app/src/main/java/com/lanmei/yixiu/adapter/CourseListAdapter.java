package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.ui.home.activity.CourseDetailsActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 教程列表
 */
public class CourseListAdapter extends SwipeRefreshAdapter<CourseClassifyListBean> {



    public CourseListAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final CourseClassifyListBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                IntentUtil.startActivity(context, CourseDetailsActivity.class,bundle);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.thumb_iv)
        ImageView thumbIv;//视频封面
        @InjectView(R.id.title_tv)
        TextView titleTv;//
        @InjectView(R.id.pic_iv)
        CircleImageView picIv;//头像
        @InjectView(R.id.nickname_tv)
        TextView nicknameTv;//
        @InjectView(R.id.like_iv)
        ImageView likeIv;//


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final CourseClassifyListBean bean) {
            ImageHelper.load(context,bean.getPic(),thumbIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            titleTv.setText(bean.getTitle());
            nicknameTv.setText(bean.getNickname());
            ImageHelper.load(context,bean.getMemberpic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            likeIv.setImageResource(StringUtils.isSame(CommonUtils.isZero,bean.getLiked())?R.drawable.dianzan_off:R.drawable.news_like_on);
        }
    }

}

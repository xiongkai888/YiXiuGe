package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.TeacherDetailsCommentBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 教师详情评论列表
 */
public class TeacherDetailsCommentListAdapter extends SwipeRefreshAdapter<TeacherDetailsCommentBean> {


    private FormatTime formatTime;
    private SimpleDateFormat format ;

    public TeacherDetailsCommentListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime();
        format = formatTime.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teacher_details_comment_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        TeacherDetailsCommentBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        CircleImageView picIv;
        @InjectView(R.id.realname_tv)
        TextView realnameTv;
        @InjectView(R.id.rating_bar)
        RatingBar ratingBar;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.like_tv)
        TextView likeTv;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final TeacherDetailsCommentBean bean) {
            ImageHelper.load(context, bean.getPic(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
            realnameTv.setText(bean.getRealname());
            ratingBar.setRating(Float.parseFloat(bean.getGrade()));
            contentTv.setText(bean.getContent());
            likeTv.setText(bean.getLike());
            setCompoundDrawables(likeTv,bean.getLiked());
            formatTime.setTime(bean.getAddtime());
            timeTv.setText(formatTime.formatterTime(format));
            likeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadLiked(bean);
                }
            });
        }

        private void setCompoundDrawables(TextView textView, String liked) {
            int drawableId;
            if (StringUtils.isSame(liked, CommonUtils.isZero)) {
                drawableId = R.drawable.dianzan_off;
            } else {
                drawableId = R.drawable.news_like_on;
            }
            Drawable img = context.getResources().getDrawable(drawableId);
// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            textView.setCompoundDrawables(img, null, null, null); //设置左边图标
        }

    }

    private void loadLiked(final TeacherDetailsCommentBean bean) {
        YiXiuGeApi api = new YiXiuGeApi("app/post_like");
        api.addParams("id", bean.getId());
        api.addParams("mod", "comment");//评论点赞
        api.addParams("uid", api.getUserId(context));
        if (StringUtils.isSame(bean.getLiked(), CommonUtils.isOne)) {
            api.addParams("del", CommonUtils.isOne);//取消点赞这个值随便传，不为空就行
        }
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (context == null) {
                    return;
                }
                bean.setLiked(StringUtils.isSame(bean.getLiked(), CommonUtils.isOne)?CommonUtils.isZero:(CommonUtils.isOne));
                bean.setLike(StringUtils.isSame(bean.getLiked(), CommonUtils.isOne)?(StringUtils.toInt(bean.getLike(),0)+1)+"":(StringUtils.toInt(bean.getLike(),0)-1)+"");
                notifyDataSetChanged();
            }
        });
    }

}

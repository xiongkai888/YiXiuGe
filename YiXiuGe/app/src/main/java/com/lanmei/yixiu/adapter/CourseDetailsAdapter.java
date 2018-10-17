package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyListBean;
import com.lanmei.yixiu.bean.NewsCommentBean;
import com.lanmei.yixiu.event.CourseCollectEvent;
import com.lanmei.yixiu.event.CourseOperationEvent;
import com.lanmei.yixiu.helper.ShareListener;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 教程详情
 */
public class CourseDetailsAdapter extends SwipeRefreshAdapter<NewsCommentBean> {

    public int TYPE_BANNER = 100;
    private CourseClassifyListBean courseClassifyListBean;
    private FormatTime time;

    public void setCourseClassifyListBean(CourseClassifyListBean courseClassifyListBean) {
        this.courseClassifyListBean = courseClassifyListBean;
    }

    public CourseDetailsAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
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
            onBindBannerViewHolder(holder); // banner
            return;
        }
        NewsCommentBean bean = getItem(position-1);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(NewsCommentBean bean) {
            ImageHelper.load(context,bean.getMemberpic(),headIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            nameTv.setText(bean.getNickname());
            contentTv.setText(bean.getContent());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.formatterTime());
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
        @InjectView(R.id.title_et)
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
        TextView subscribeTv;//订阅数
        @InjectView(R.id.favoured_iv)
        ImageView favouredIv;//是否收藏
        @InjectView(R.id.share_iv)
        ImageView shareIv;//分享

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter() {
            ImageHelper.load(context,courseClassifyListBean.getMemberpic(),memberPicIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            nameTv.setText(courseClassifyListBean.getNickname());
            titleTv.setText(courseClassifyListBean.getTitle());
            viewTv.setText(courseClassifyListBean.getView());
            FormatTime time = new FormatTime(context,courseClassifyListBean.getAddtime());
            addtimeTv.setText(time.getAgoDateFormat());
            reviewsTv.setText(courseClassifyListBean.getReviews());
            likeTv.setText(courseClassifyListBean.getLike());
            setCompoundDrawables(likeTv,courseClassifyListBean.getLiked());
            subscribeTv.setText(courseClassifyListBean.getFavour());//订阅数改为收藏数
            likeTv.setOnClickListener(new View.OnClickListener() {//
                @Override
                public void onClick(View v) {
                    loadLiked();
                }
            });
            favouredIv.setImageResource(StringUtils.isSame(courseClassifyListBean.getFavoured(),CommonUtils.isZero)?R.drawable.favoured_off:R.drawable.favoured_on);
            favouredIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFavoured();
                }
            });
            shareIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (listener != null){
                       listener.share(courseClassifyListBean.getVideo());
                   }
                }
            });
        }

    }

    private ShareListener listener;

    public void setShare(ShareListener listener){
        this.listener = listener;
    }

    private void loadLiked() {
        YiXiuGeApi api = new YiXiuGeApi("app/post_like");
        api.addParams("id", courseClassifyListBean.getId());
        api.addParams("mod", "video");//教程
        api.addParams("uid", api.getUserId(context));
        if (StringUtils.isSame(courseClassifyListBean.getLiked(), CommonUtils.isOne)) {
            api.addParams("del", CommonUtils.isOne);//取消点赞这个值随便传，不为空就行
        }
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (context == null) {
                    return;
                }
                courseClassifyListBean.setLiked(StringUtils.isSame(courseClassifyListBean.getLiked(), CommonUtils.isOne)?CommonUtils.isZero:(CommonUtils.isOne));
                courseClassifyListBean.setLike(StringUtils.isSame(courseClassifyListBean.getLiked(), CommonUtils.isOne)?(StringUtils.toInt(courseClassifyListBean.getLike(),0)+1)+"":(StringUtils.toInt(courseClassifyListBean.getLike(),0)-1)+"");
                EventBus.getDefault().post(new CourseOperationEvent(courseClassifyListBean.getId(),courseClassifyListBean.getLiked(),courseClassifyListBean.getLike(),courseClassifyListBean.getView(),courseClassifyListBean.getReviews(),courseClassifyListBean.getFavoured(), courseClassifyListBean.getFavour()));
                notifyDataSetChanged();
            }
        });
    }
    //点击收藏或者取消收藏
    private void loadFavoured() {
        YiXiuGeApi api = new YiXiuGeApi("app/favour");
        api.addParams("id", courseClassifyListBean.getId());
        api.addParams("uid", api.getUserId(context));
        api.addParams("mod", "video");
        if (StringUtils.isSame(courseClassifyListBean.getFavoured(), CommonUtils.isOne)) {
            api.addParams("del", CommonUtils.isOne);//取消收藏这个值随便传，不为空就行
        }
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (context == null) {
                    return;
                }
                courseClassifyListBean.setFavoured(StringUtils.isSame(courseClassifyListBean.getFavoured(), CommonUtils.isOne)?CommonUtils.isZero:CommonUtils.isOne);
                int favoure = StringUtils.toInt(courseClassifyListBean.getFavour(),0);
                courseClassifyListBean.setFavour(StringUtils.isSame(courseClassifyListBean.getFavoured(), CommonUtils.isOne)?(favoure+1)+"":(favoure-1)+"");
                EventBus.getDefault().post(new CourseOperationEvent(courseClassifyListBean.getId(),courseClassifyListBean.getLiked(),courseClassifyListBean.getLike(),courseClassifyListBean.getView(),courseClassifyListBean.getReviews(),courseClassifyListBean.getFavoured(),courseClassifyListBean.getFavour()));
                notifyDataSetChanged();
                EventBus.getDefault().post(new CourseCollectEvent());//如果是从收藏列表点击进来的就通知刷新教程收藏列表
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

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder) {
        if (courseClassifyListBean == null){
            return;
        }
        BannerViewHolder viewHolder = (BannerViewHolder) holder;
        viewHolder.setParameter();
    }


}

package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NewsClassifyListBean;
import com.lanmei.yixiu.ui.home.activity.NewsDetailsActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 收藏资讯
 */
public class CollectNewsAdapter extends SwipeRefreshAdapter<NewsClassifyListBean> {

    public CollectNewsAdapter(Context context) {
        super(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        NewsClassifyListBean  bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.like_tv)
        TextView likeTv;
        @InjectView(R.id.reviews_tv)
        TextView reviewsTv;
        @InjectView(R.id.share_iv)
        ImageView shareIv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }

        public void setParameter(final NewsClassifyListBean bean) {
            final List<String> list = bean.getPic();
            sudokuView.setListData(list);
            sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
                @Override
                public void onClick(int positionSub) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    IntentUtil.startActivity(context, NewsDetailsActivity.class, bundle);
                }

                @Override
                public void onDoubleTap(int position) {
                    CommonUtils.showPhotoBrowserActivity(context, list, list.get(position));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    IntentUtil.startActivity(context, NewsDetailsActivity.class, bundle);
                }
            });
            titleTv.setText(bean.getTitle());
            reviewsTv.setText(bean.getReviews());
            shareIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.developing(context);
                }
            });
            likeTv.setText(bean.getLike());
            setCompoundDrawables(likeTv, bean.getLiked());
            likeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadLiked(bean);
                }
            });
        }
    }

    private void loadLiked(final NewsClassifyListBean bean) {
        YiXiuGeApi api = new YiXiuGeApi("app/post_like");
        api.addParams("id", bean.getId());
        api.addParams("mod", "post");//资讯
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

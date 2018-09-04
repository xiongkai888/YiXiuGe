package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.NewsClassifyListBean;
import com.lanmei.yixiu.bean.NewsCommentBean;
import com.lanmei.yixiu.utils.FormatTime;
import com.lanmei.yixiu.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 资讯详情评论列表
 */
public class NewsDetailsAdapter extends SwipeRefreshAdapter<NewsCommentBean> {

    public int TYPE_BANNER = 100;
    private NewsClassifyListBean newsBean;
    private FormatTime time;
    private BannerViewHolder bannerViewHolder;

    public void setNewsBean(NewsClassifyListBean newsBean) {
        this.newsBean = newsBean;
        notifyDataSetChanged();
    }

    public NewsDetailsAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            bannerViewHolder = new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_news_details, parent, false));
            return bannerViewHolder;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder, position); // banner
            return;
        }
        NewsCommentBean bean = getItem(position - 1);
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

        @InjectView(R.id.web_view)
        WebView mWebView;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_sub_tv)
        TextView timeTv;

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void remove() {
            if (mWebView != null) {
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        }

    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (newsBean == null) {
            return;
        }
        BannerViewHolder viewHolder = (BannerViewHolder) holder;
        WebViewPhotoBrowserUtil.photoBrowser(context, viewHolder.mWebView, newsBean.getContent());
        viewHolder.titleTv.setText(newsBean.getTitle());
        time.setTime(newsBean.getAddtime());
        viewHolder.timeTv.setText(time.getAgoDateFomat());
    }

    public void removeWebView() {
        bannerViewHolder.remove();
    }

}

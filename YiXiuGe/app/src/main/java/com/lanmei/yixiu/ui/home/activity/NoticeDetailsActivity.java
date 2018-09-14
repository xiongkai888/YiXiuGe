package com.lanmei.yixiu.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.NoticeBean;
import com.lanmei.yixiu.utils.FormatTime;
import com.lanmei.yixiu.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 公告通知详情
 */
public class NoticeDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.title_et)
    TextView titleTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.webView)
    WebView webView;


    @Override
    public int getContentViewId() {
        return R.layout.activity_notice_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.notice);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle == null) {
            return;
        }
        NoticeBean bean = (NoticeBean) bundle.getSerializable("bean");
        if (bean == null) {
            return;
        }
        FormatTime time = new FormatTime(bean.getAddtime());
        titleTv.setText(bean.getTitle());
        timeTv.setText(time.formatterTime());
        WebViewPhotoBrowserUtil.photoBrowser(this, webView, bean.getContent());
    }

}

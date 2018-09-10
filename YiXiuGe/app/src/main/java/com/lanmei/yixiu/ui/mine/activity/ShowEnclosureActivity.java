package com.lanmei.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.L;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 附件详情
 */
public class ShowEnclosureActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.webView)
    WebView webView;
    private String enclosureUrl;

    @Override
    public int getContentViewId() {
        return R.layout.activity_show_enclosure;
    }



    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.enclosure_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        enclosureUrl = getIntent().getStringExtra("value");
        L.d(L.TAG,"enclosureUrl:"+enclosureUrl);
        WebViewPhotoBrowserUtil.loadUrl(webView,enclosureUrl);
//        WebViewPhotoBrowserUtil.photoBrowser(this,webView,enclosureUrl);
    }

}

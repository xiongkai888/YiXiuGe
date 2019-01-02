package com.medui.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;

import com.medui.yixiu.R;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.AboutUsBean;
import com.medui.yixiu.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;


/**
 * 关于我们
 */

public class AboutUsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.web_view)
    WebView webView;


    @Override
    public int getContentViewId() {
        return R.layout.activity_about_us;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.about_us);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/aboutus");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<DataBean<AboutUsBean>>() {
            @Override
            public void onResponse(DataBean<AboutUsBean> response) {
                if (isFinishing()){
                    return;
                }
                AboutUsBean bean = response.data;
                if (bean == null){
                    return;
                }
                WebViewPhotoBrowserUtil.photoBrowser(getContext(),webView,bean.getContent());
            }
        });
    }
}

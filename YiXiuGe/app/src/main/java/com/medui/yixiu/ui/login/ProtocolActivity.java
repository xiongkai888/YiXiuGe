package com.medui.yixiu.ui.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.api.YiXiuGeApi;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 用户协议
 */
public class ProtocolActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.content_tv)
    TextView contentTv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_protocol;
    }



    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {


        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getIntent().getStringExtra("value"));
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        YiXiuGeApi api = new YiXiuGeApi("app/agreement");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()){
                    return;
                }
                contentTv.setText(response.data);
            }
        });
    }

}

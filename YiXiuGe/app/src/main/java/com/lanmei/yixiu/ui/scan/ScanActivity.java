package com.lanmei.yixiu.ui.scan;

import android.os.Bundle;

import com.google.zxing.Result;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.UIHelper;
import com.yzq.zxinglibrary.android.CaptureActivity;

/**
 * Created by xkai on 2018/4/25.
 */

public class ScanActivity extends CaptureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void handleDecode(Result rawResult) {
        super.handleDecode(rawResult);
        YiXiuGeApi api = new YiXiuGeApi("app/signin");
        api.addParams("uid",api.getUserId(this));
        api.addParams("cid",rawResult.getText());
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(ScanActivity.this,response.getMsg());
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        L.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }
}

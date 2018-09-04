package com.lanmei.yixiu.ui.scan;

import android.os.Bundle;

import com.google.zxing.Result;
import com.lanmei.yixiu.R;
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
        UIHelper.ToastMessage(this, R.string.developing);
        finish();
    }

}

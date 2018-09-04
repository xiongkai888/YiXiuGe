package com.lanmei.yixiu.ui.message.fragment;

import android.os.Bundle;
import android.view.View;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.ui.home.activity.NoticeActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.IntentUtil;

import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/27.
 * 消息-消息
 */

public class MessageFragment extends BaseFragment {


    @Override
    public int getContentViewId() {
        return R.layout.fragment_message_message;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
    }


    @OnClick({R.id.ll_notice, R.id.ll_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_notice:
                IntentUtil.startActivity(context, NoticeActivity.class);
                break;
            case R.id.ll_action:
                CommonUtils.developing(context);
                break;
        }
    }
}

package com.medui.yixiu.ui.message.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.medui.yixiu.R;
import com.medui.yixiu.ui.message.activity.NoticeActivity;
import com.medui.yixiu.ui.message.activity.SiXinActivity;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.IntentUtil;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/27.
 * 消息-消息
 */

public class MessageFragment extends BaseFragment {

    @InjectView(R.id.ll_sx)
    LinearLayout llsx;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_message_message;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        llsx.setVisibility(CommonUtils.isStudent(context) ? View.VISIBLE : View.GONE);
    }


    @OnClick({R.id.ll_notice, R.id.ll_action, R.id.ll_sx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_notice://通知公告
                IntentUtil.startActivity(context, NoticeActivity.class);
                break;
            case R.id.ll_action://福利活动
                CommonUtils.developing(context);
                break;
            case R.id.ll_sx://私信
                IntentUtil.startActivity(context, SiXinActivity.class);
                break;
        }
    }
}

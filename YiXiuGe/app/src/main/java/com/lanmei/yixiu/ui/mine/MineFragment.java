package com.lanmei.yixiu.ui.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.event.SetUserEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

/**
 * Created by Administrator on 2018/8/24.
 * 我的
 */

public class MineFragment extends BaseFragment {

    @InjectView(R.id.fl_content)
    FrameLayout fl_content;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setUserType();
    }

    public void setUserType() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, !StringUtils.isSame(CommonUtils.getUserType(context), CommonUtils.isZero) ? new MineStudentFragment() : new MineTeacherFragment());
        transaction.commitAllowingStateLoss();
    }


    @Subscribe
    public void setUserEvent(SetUserEvent event) {
        setUserType();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

package com.medui.yixiu.ui.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.medui.yixiu.R;
import com.medui.yixiu.event.SetUserEvent;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;

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
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        setUserType();
    }

    public void setUserType() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, CommonUtils.isStudent(context) ? new MineStudentFragment() : new MineTeacherFragment());
        transaction.commitAllowingStateLoss();
    }

//
//    @Subscribe
//    public void setUserEvent(SetUserEvent event) {
//        setUserType();
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

}

package com.lanmei.yixiu.ui.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UserHelper;

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
        setUserType();
    }

    public void setUserType() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (UserHelper.getInstance(context).hasLogin()) {
            if (StringUtils.isSame("1", CommonUtils.isZero)) {
                transaction.replace(R.id.fl_content, new MineStudentFragment());
            } else {
                transaction.replace(R.id.fl_content, new MineTeacherFragment());
            }
        } else {
            transaction.replace(R.id.fl_content, new MineStudentFragment());
        }
        transaction.commitAllowingStateLoss();
    }

}

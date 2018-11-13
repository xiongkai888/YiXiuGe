package com.lanmei.yixiu.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.event.SetUserEvent;
import com.lanmei.yixiu.ui.mine.activity.MyCheckingInActivity;
import com.lanmei.yixiu.ui.mine.activity.MyClassScheduleActivity;
import com.lanmei.yixiu.ui.mine.activity.MyCollectActivity;
import com.lanmei.yixiu.ui.mine.activity.MyEvaluateActivity;
import com.lanmei.yixiu.ui.mine.activity.MyNoteActivity;
import com.lanmei.yixiu.ui.mine.activity.MyTestsListActivity;
import com.lanmei.yixiu.ui.mine.activity.PersonalDataSubActivity;
import com.lanmei.yixiu.ui.mine.activity.SettingActivity;
import com.lanmei.yixiu.ui.teacher.activity.TutorialCoursewareActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xkai on 2018/7/13.
 * (学生端)我的
 */

public class MineStudentFragment extends BaseFragment {

    @InjectView(R.id.pic_iv)
    CircleImageView picIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.id_tv)
    TextView idTv;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine_student;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setUser();
    }


    @Subscribe
    public void setUserEvent(SetUserEvent event) {
        setUser();
    }


    private void setUser() {
        UserBean userBean = CommonUtils.getUserBean(context);
        if (StringUtils.isEmpty(userBean)) {
            return;
        }
        ImageHelper.load(context, userBean.getPic(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        nameTv.setText(userBean.getNickname());
        idTv.setText("id："+ userBean.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.ll_tests_list,R.id.pic_iv,R.id.ll_courseware,R.id.ll_data, R.id.ll_collect, R.id.ll_checking_in, R.id.ll_notes, R.id.ll_evaluate, R.id.ll_setting,R.id.class_schedule_tv,R.id.kao_shi_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_iv://个人资料
            case R.id.ll_data://个人资料
                IntentUtil.startActivity(context, PersonalDataSubActivity.class);
                break;
            case R.id.ll_collect://我的收藏
                IntentUtil.startActivity(context, MyCollectActivity.class);
                break;
            case R.id.ll_checking_in://我的考勤
//                CommonUtils.developing(context);
                IntentUtil.startActivity(context, MyCheckingInActivity.class);
                break;
            case R.id.ll_notes://我的笔记
//                CommonUtils.developing(context);
                IntentUtil.startActivity(context, MyNoteActivity.class);
                break;
            case R.id.ll_courseware://教程课件
                IntentUtil.startActivity(context, TutorialCoursewareActivity.class);
                break;
            case R.id.ll_evaluate://我的评价
//                CommonUtils.developing(context);
                IntentUtil.startActivity(context, MyEvaluateActivity.class);
                break;
            case R.id.ll_setting://设置
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.class_schedule_tv://课程表
                IntentUtil.startActivity(context, MyClassScheduleActivity.class);
                break;
            case R.id.kao_shi_tv://考试
                CommonUtils.developing(context);
//                IntentUtil.startActivity(context, MyExaminationActivity.class);
                break;
            case R.id.ll_tests_list://我的评估
                IntentUtil.startActivity(context, MyTestsListActivity.class);
                break;
        }
    }
}

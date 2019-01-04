package com.medui.yixiu.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.event.SetUserEvent;
import com.medui.yixiu.ui.mine.activity.ExaminationActivity;
import com.medui.yixiu.ui.mine.activity.MyCheckingInActivity;
import com.medui.yixiu.ui.mine.activity.MyClassScheduleActivity;
import com.medui.yixiu.ui.mine.activity.MyCollectActivity;
import com.medui.yixiu.ui.mine.activity.MyEvaluateActivity;
import com.medui.yixiu.ui.mine.activity.MyNoteActivity;
import com.medui.yixiu.ui.mine.activity.MyPerformanceActivity;
import com.medui.yixiu.ui.mine.activity.MyTestsListActivity;
import com.medui.yixiu.ui.mine.activity.PersonalDataSubActivity;
import com.medui.yixiu.ui.mine.activity.SettingActivity;
import com.medui.yixiu.ui.teacher.activity.TutorialCoursewareActivity;
import com.medui.yixiu.utils.CommonUtils;
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
            nameTv.setText("游客");
            picIv.setImageResource(R.drawable.default_pic);
            return;
        }
        ImageHelper.load(context, userBean.getPic(), picIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        String examine = userBean.getExamine();
        if (StringUtils.isSame(examine,CommonUtils.isZero)){
            examine = " (资料未提交)";
        }else if (StringUtils.isSame(examine,CommonUtils.isOne)){
            examine = " (资料审核中)";
        }else if(StringUtils.isSame(examine,CommonUtils.isThree)){
            examine = " (资料审核不通过)";
        }else {
            examine = "";
        }
        nameTv.setText(userBean.getNickname()+examine);
        idTv.setText("id：" + userBean.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.ll_tests_list, R.id.ll_performance, R.id.pic_iv, R.id.ll_courseware, R.id.ll_data, R.id.ll_collect, R.id.ll_checking_in, R.id.ll_notes, R.id.ll_evaluate, R.id.ll_setting, R.id.class_schedule_tv, R.id.kao_shi_tv})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id != R.id.ll_setting){
            if (!CommonUtils.isExamine(context)) {
                return;
            }
        }
        switch (id) {
            case R.id.pic_iv://个人资料
            case R.id.ll_data://个人资料
                IntentUtil.startActivity(context, PersonalDataSubActivity.class);
                break;
            case R.id.ll_performance://我的成绩
                IntentUtil.startActivity(context, MyPerformanceActivity.class);
                break;
            case R.id.ll_collect://我的收藏
                IntentUtil.startActivity(context, MyCollectActivity.class);
                break;
            case R.id.ll_checking_in://我的考勤
                IntentUtil.startActivity(context, MyCheckingInActivity.class);
                break;
            case R.id.ll_notes://我的笔记
                IntentUtil.startActivity(context, MyNoteActivity.class);
                break;
            case R.id.ll_courseware://教程课件
                IntentUtil.startActivity(context, TutorialCoursewareActivity.class);
                break;
            case R.id.ll_evaluate://我的评价
                IntentUtil.startActivity(context, MyEvaluateActivity.class);
                break;
            case R.id.ll_setting://设置
//                CommonUtils.loadUserInfo(context,null);
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.class_schedule_tv://课程表
                IntentUtil.startActivity(context, MyClassScheduleActivity.class);
                break;
            case R.id.kao_shi_tv://考试
                IntentUtil.startActivity(context, ExaminationActivity.class);
                break;
            case R.id.ll_tests_list://我的评估
                IntentUtil.startActivity(context, MyTestsListActivity.class);
                break;
        }
    }
}

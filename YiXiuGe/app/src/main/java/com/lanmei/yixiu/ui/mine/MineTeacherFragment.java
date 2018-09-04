package com.lanmei.yixiu.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.event.SetUserEvent;
import com.lanmei.yixiu.ui.mine.activity.MyCheckingInActivity;
import com.lanmei.yixiu.ui.mine.activity.MyCollectActivity;
import com.lanmei.yixiu.ui.mine.activity.PersonalDataSubActivity;
import com.lanmei.yixiu.ui.mine.activity.SettingActivity;
import com.lanmei.yixiu.ui.teacher.activity.AddQuestionnaireSubjectActivity;
import com.lanmei.yixiu.ui.teacher.activity.ClassListActivity;
import com.lanmei.yixiu.ui.teacher.activity.EvaluateActivity;
import com.lanmei.yixiu.ui.teacher.activity.ExaminationManagementActivity;
import com.lanmei.yixiu.ui.teacher.activity.PublishCourseActivity;
import com.lanmei.yixiu.ui.teacher.activity.QuestionnaireManagementActivity;
import com.lanmei.yixiu.ui.teacher.activity.TeachingAttachmentsActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xkai on 2018/7/13.
 * (教师端)我的
 */

public class MineTeacherFragment extends BaseFragment {

    @InjectView(R.id.pic_iv)
    CircleImageView picIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.id_tv)
    TextView idTv;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine_teacher;
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
        idTv.setText("id:"+ userBean.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @OnClick({R.id.ll_data, R.id.ll_collect, R.id.ll_checking_in, R.id.ll_evaluate, R.id.ll_setting, R.id.class_schedule_tv, R.id.kao_shi_tv, R.id.ll_mine_teacher1, R.id.ll_mine_teacher2, R.id.ll_mine_teacher3, R.id.ll_mine_teacher4, R.id.ll_mine_teacher5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_data://个人资料
                IntentUtil.startActivity(context, PersonalDataSubActivity.class);
                break;
            case R.id.ll_collect://我的收藏
                IntentUtil.startActivity(context, MyCollectActivity.class);
                break;
            case R.id.ll_checking_in://我的考勤
                IntentUtil.startActivity(context, MyCheckingInActivity.class);
                break;
            case R.id.ll_evaluate://我的评价
                IntentUtil.startActivity(context, EvaluateActivity.class);
                break;
            case R.id.ll_setting://设置
                IntentUtil.startActivity(context, SettingActivity.class);
                break;
            case R.id.class_schedule_tv://课程表
                IntentUtil.startActivity(context, ClassListActivity.class);
                break;
            case R.id.kao_shi_tv://考试(管理)
                IntentUtil.startActivity(context, ExaminationManagementActivity.class);
                break;
            case R.id.ll_mine_teacher1://发布教程
                IntentUtil.startActivity(context, PublishCourseActivity.class);
                break;
            case R.id.ll_mine_teacher2://发布课时
                CommonUtils.developing(getContext());
//                IntentUtil.startActivity(context, MainActivity.class);
                break;
            case R.id.ll_mine_teacher3://调查问卷
                IntentUtil.startActivity(context, QuestionnaireManagementActivity.class);
                break;
            case R.id.ll_mine_teacher4://发布题库
//                CommonUtils.developing(getContext());
                IntentUtil.startActivity(context, AddQuestionnaireSubjectActivity.class);
                break;
            case R.id.ll_mine_teacher5://教学设备
                IntentUtil.startActivity(context, TeachingAttachmentsActivity.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

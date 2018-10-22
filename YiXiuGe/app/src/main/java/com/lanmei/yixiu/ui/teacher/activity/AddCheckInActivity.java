package com.lanmei.yixiu.ui.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.ClassroomBean;
import com.lanmei.yixiu.bean.StudentsBean;
import com.lanmei.yixiu.event.AddCheckInEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 添加考勤
 */
public class AddCheckInActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.student_tv)
    TextView studentTv;
    @InjectView(R.id.check_type_tv)
    TextView checkTypeTv;
    @InjectView(R.id.check_in_time_tv)
    TextView checkInTimeTv;
    @InjectView(R.id.remark_et)
    EditText remarkEt;
    private List<StudentsBean> studentsBeans;//学生列表
    private OptionPicker studentsPicker;//学生选择器
    private StudentsBean studentsBean;//

    private List<ClassroomBean> classroomBeans;//出勤类型列表
    private OptionPicker checkTypePicker;//出勤类型选择器
    private ClassroomBean checkTypeBean;//


    private DateTimePicker picker;
    private FormatTime time;
    private String cid;

    public int getContentViewId() {
        return R.layout.activity_add_check_in;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.add_check_in);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        cid = getIntent().getStringExtra("value");
        initDatePicker();//开始和结束时间
        initStudent();
        initCheckType();

    }

    private void initDatePicker() {
        picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        time = new FormatTime(this);
        int year = time.getYear();
        int month = time.getMonth();
        int day = time.getDay();
        int hour = time.getHour();
        int minute = time.getMinute();
        picker.setDateRangeStart(year- 1, month, day);
        picker.setDateRangeEnd(year, month, day);
        picker.setSelectedItem(year, month, day, hour, minute);
//        picker.setLabel("年","月","日",":","");
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {

                try {
                    String timeStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    if (time.dateToStampLong(timeStr) > System.currentTimeMillis() / 1000) {
                        UIHelper.ToastMessage(getContext(), "出勤时间不能大于当前时间");
                        checkInTimeTv.setText("");
                    }else {
                        checkInTimeTv.setText(timeStr);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //学生列表
    private void initStudent() {
        YiXiuGeApi api = new YiXiuGeApi("app/getstudent");
        api.addParams("cid", cid);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<StudentsBean>>() {
            @Override
            public void onResponse(NoPageListBean<StudentsBean> response) {
                if (isFinishing()) {
                    return;
                }
                studentsBeans = response.data;
                initStudentPicker();
            }
        });
    }

    private void initStudentPicker() {
        if (StringUtils.isEmpty(studentsBeans)) {
            return;
        }
        int size = studentsBeans.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            String realName = studentsBeans.get(i).getRealname();
            if (StringUtils.isEmpty(realName)){
                strings[i] = getString(R.string.anonymity_student)+i;
            }else {
                strings[i] = realName;
            }
        }
        studentsPicker = new OptionPicker(this, strings);
        studentsPicker.setOffset(3);
        studentsPicker.setSelectedIndex(1);
        studentsPicker.setTextSize(18);
        studentsPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                studentsBean = studentsBeans.get(index);
                studentTv.setText(item);
            }
        });
    }

    private void initClassroomPicker() {
        if (StringUtils.isEmpty(classroomBeans)) {
            return;
        }
        int size = classroomBeans.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            String name = classroomBeans.get(i).getName();
            if (StringUtils.isEmpty(name)){
                strings[i] = getString(R.string.anonymity_classroom)+i;
            }else {
                strings[i] = name;
            }
        }
        checkTypePicker = new OptionPicker(this, strings);
        checkTypePicker.setOffset(3);
        checkTypePicker.setSelectedIndex(1);
        checkTypePicker.setTextSize(18);
        checkTypePicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                checkTypeBean = classroomBeans.get(index);
                checkTypeTv.setText(item);
            }
        });
    }

    //出勤类型列表
    private void initCheckType() {
        YiXiuGeApi api = new YiXiuGeApi("app/geteducation");
        api.addParams("pid", 7);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<ClassroomBean>>() {
            @Override
            public void onResponse(NoPageListBean<ClassroomBean> response) {
                if (isFinishing()) {
                    return;
                }
                classroomBeans = response.data;
                initClassroomPicker();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit) {
            submitPublish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitPublish() {
        try {
            if (StringUtils.isEmpty(studentsBean)) {
                UIHelper.ToastMessage(this, R.string.choose_students);
                return;
            }
            if (StringUtils.isEmpty(checkTypeBean)) {
                UIHelper.ToastMessage(this, "请选择出勤类型");
                return;
            }
            YiXiuGeApi api = new YiXiuGeApi("app/add_attendance");
            api.addParams("uid", studentsBean.getId());
            api.addParams("cid", cid);
            api.addParams("attend_time", time.dateToStampLong(CommonUtils.getStringByTextView(checkInTimeTv)));
            api.addParams("explain", CommonUtils.getStringByEditText(remarkEt));
            api.addParams("attend_type", checkTypeBean.getName());
            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    if (isFinishing()) {
                        return;
                    }
                    EventBus.getDefault().post(new AddCheckInEvent());
                    UIHelper.ToastMessage(getContext(), response.getMsg());
                    finish();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.ll_student, R.id.ll_check_type, R.id.ll_check_in_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_student://学生名字
                if (studentsPicker != null) {
                    studentsPicker.show();
                }
                break;
            case R.id.ll_check_type://出勤类型
                if (checkTypePicker != null) {
                    checkTypePicker.show();
                }
                break;
            case R.id.ll_check_in_time://出勤时间
                if (picker != null) {
                    picker.show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        picker = null;
        studentsPicker = null;
        checkTypePicker = null;
    }

}


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
import com.lanmei.yixiu.bean.ClassSelectBean;
import com.lanmei.yixiu.bean.ClassroomBean;
import com.lanmei.yixiu.bean.SubjectsBean;
import com.lanmei.yixiu.event.ClassSelectEvent;
import com.lanmei.yixiu.event.PublishClassHourEvent;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 发布课时
 */
public class PublishClassHourActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.subjects_tv)
    TextView subjectsTv;
    @InjectView(R.id.class_tv)
    TextView classTv;
    @InjectView(R.id.classroom_tv)
    TextView classroomTv;
    @InjectView(R.id.start_time_tv)
    TextView startTimeTv;
    @InjectView(R.id.end_time_tv)
    TextView endTimeTv;
    @InjectView(R.id.remark_et)
    EditText remarkEt;
    @InjectView(R.id.title_et)
    EditText titleEt;//标题
    @InjectView(R.id.teaching_methods_et)
    EditText teachingMethodsEt;//教学方式
    private List<SubjectsBean> subjectsBeans;//科目列表
    private OptionPicker subjectsPicker;//科目选择器
    private SubjectsBean subjectsBean;//

    private List<ClassroomBean> classroomBeans;//教室列表
    private OptionPicker classroomPicker;//教室选择器
    private ClassroomBean classroomBean;//

    private List<ClassSelectBean> classSelectBeans;//班级列表
    private List<ClassSelectBean.XiajiBean> xiajiBeanList;//选择的班级

    private DateTimePicker picker;
    private FormatTime time;
    private String startTime;
    private String endTime;
    private int timeType;//1开始时间,2结束时间

    public int getContentViewId() {
        return R.layout.activity_publish_class_hour;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.publish_class_hour);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        initClass();//班级
        initSubjects();//科目
        initClassroom();//教室
        initDatePicker();//开始和结束时间
    }

    private void initDatePicker() {
        picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        time = new FormatTime(this);
        time.setTime(System.currentTimeMillis() / 1000 + 60);
        int year = time.getYear();
        int month = time.getMonth();
        int day = time.getDay();
        int hour = time.getHour();
        int minute = time.getMinute();
        picker.setDateRangeStart(year, month, day);
        picker.setDateRangeEnd(year + 1, month, day);
        picker.setSelectedItem(year, month, day, hour, minute);
//        picker.setLabel("年","月","日",":","");
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                try {
                    String timeStr = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                    if (timeType == 1) {
                        if (time.dateToStampLong(timeStr) < System.currentTimeMillis() / 1000) {
                            UIHelper.ToastMessage(getContext(), "开始时间不能小于当前时间");
                            setTimeNull();
                        } else if (!StringUtils.isEmpty(endTime) && time.dateToStampLong(timeStr) > time.dateToStampLong(endTime)) {
                            UIHelper.ToastMessage(getContext(), "开始时间不能大于当结束时间");
                            setTimeNull();
                        } else {
                            startTime = timeStr;
                            startTimeTv.setText(startTime);
                        }
                    } else {
                        if (time.dateToStampLong(startTime) > time.dateToStampLong(timeStr)) {
                            UIHelper.ToastMessage(getContext(), "结束时间不能小于开始时间");
                            setTimeNull();
                        } else {
                            endTime = timeStr;
                            endTimeTv.setText(endTime);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //选错时间重新选择（清空）
    public void setTimeNull() {
        startTime = "";
        endTime = "";
        startTimeTv.setText("");
        endTimeTv.setText("");
    }

    //获取课程（科目）
    private void initSubjects() {
        YiXiuGeApi api = new YiXiuGeApi("app/getteaching");
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<SubjectsBean>>() {
            @Override
            public void onResponse(NoPageListBean<SubjectsBean> response) {
                if (isFinishing()) {
                    return;
                }
                subjectsBeans = response.data;
                initSubjectsPicker();
            }
        });
    }

    private void initSubjectsPicker() {
        if (StringUtils.isEmpty(subjectsBeans)) {
            return;
        }
        int size = subjectsBeans.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = subjectsBeans.get(i).getName();
        }
        subjectsPicker = new OptionPicker(this, strings);
        subjectsPicker.setOffset(3);
        subjectsPicker.setSelectedIndex(1);
        subjectsPicker.setTextSize(18);
        subjectsPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                subjectsBean = subjectsBeans.get(index);
                subjectsTv.setText(item);
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
            strings[i] = classroomBeans.get(i).getName();
        }
        classroomPicker = new OptionPicker(this, strings);
        classroomPicker.setOffset(3);
        classroomPicker.setSelectedIndex(1);
        classroomPicker.setTextSize(18);
        classroomPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                classroomBean = classroomBeans.get(index);
                classroomTv.setText(item);
            }
        });
    }

    //获取教室
    private void initClassroom() {
        YiXiuGeApi api = new YiXiuGeApi("app/getroom");
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

    //获取班级
    private void initClass() {
        YiXiuGeApi api = new YiXiuGeApi("app/getclass");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<ClassSelectBean>>() {
            @Override
            public void onResponse(NoPageListBean<ClassSelectBean> response) {
                if (isFinishing()) {
                    return;
                }
                classSelectBeans = response.data;
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
            if (StringUtils.isEmpty(subjectsBean)) {
                UIHelper.ToastMessage(this, "请选择科目");
                return;
            }
            if (StringUtils.isEmpty(xiajiBeanList)) {
                UIHelper.ToastMessage(this, "请选择班级");
                return;
            }
            String title = CommonUtils.getStringByEditText(titleEt);
            if (StringUtils.isEmpty(title)) {
                UIHelper.ToastMessage(this, R.string.input_title);
                return;
            }
            String teachingMethods = CommonUtils.getStringByEditText(teachingMethodsEt);
            if (StringUtils.isEmpty(teachingMethods)) {
                UIHelper.ToastMessage(this, R.string.input_teaching_methods);
                return;
            }
            if (StringUtils.isEmpty(classroomBean)) {
                UIHelper.ToastMessage(this, "请选择教室");
                return;
            }
            if (StringUtils.isEmpty(startTime)) {
                UIHelper.ToastMessage(this, "请选择开始时间");
                return;
            }
            if (StringUtils.isEmpty(endTime)) {
                UIHelper.ToastMessage(this, "请选择结束时间");
                return;
            }
            YiXiuGeApi api = new YiXiuGeApi("app/add_course");
            api.addParams("uid", api.getUserId(this));
            api.addParams("start_time", time.dateToStampLong(startTime));
            api.addParams("end_time", time.dateToStampLong(endTime));
            api.addParams("remark", CommonUtils.getStringByEditText(remarkEt));
            api.addParams("room", classroomBean.getId());
            api.addParams("cid", getClassId());
            api.addParams("kid", subjectsBean.getId());
            api.addParams("title", title);
            api.addParams("type", teachingMethods);
            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    if (isFinishing()) {
                        return;
                    }
                    EventBus.getDefault().post(new PublishClassHourEvent(CommonUtils.isTwo));
                    UIHelper.ToastMessage(getContext(), response.getMsg());
                    finish();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.ll_subjects, R.id.ll_class, R.id.ll_classroom, R.id.ll_start_time, R.id.ll_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_subjects://选择科目
                if (subjectsPicker != null) {
                    subjectsPicker.show();
                }
                break;
            case R.id.ll_class://选择班级
                if (!StringUtils.isEmpty(classSelectBeans)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", (Serializable) classSelectBeans);
                    IntentUtil.startActivity(this, ClassSelectActivity.class, bundle);
                }
                break;
            case R.id.ll_classroom://所在教室
                if (classroomPicker != null) {
                    classroomPicker.show();
                }
                break;
            case R.id.ll_start_time://开始时间
                if (picker != null) {
                    timeType = 1;
                    picker.show();
                }
                break;
            case R.id.ll_end_time://结束时间
                if (picker != null) {
                    if (StringUtils.isEmpty(CommonUtils.getStringByTextView(startTimeTv))) {
                        UIHelper.ToastMessage(this, "先选择开始时间");
                        return;
                    }
                    timeType = 2;
                    picker.show();
                }
                break;
        }
    }

    //选择班级的时候调用
    @Subscribe
    public void classSelectEvent(ClassSelectEvent event) {
        xiajiBeanList = event.getList();
        classTv.setText(getName());
    }

    private String getName() {
        StringBuffer buffer = new StringBuffer();
        for (ClassSelectBean.XiajiBean xiajiBean:xiajiBeanList){
            buffer.append(xiajiBean.getParent_name()+xiajiBean.getName()+",");
        }
        String name = buffer.toString();
        if (!StringUtils.isEmpty(name)){
            name = name.substring(0,name.length()-1);
        }
        return name;
    }

    private String getClassId() {
        StringBuffer buffer = new StringBuffer();
        for (ClassSelectBean.XiajiBean xiajiBean:xiajiBeanList){
            buffer.append(xiajiBean.getParent_id()+","+xiajiBean.getId()+"|");
        }
        String id = buffer.toString();
        if (!StringUtils.isEmpty(id)){
            id = id.substring(0,id.length()-1);
        }
        return id;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        picker = null;
        subjectsPicker = null;
        classroomPicker = null;
    }

}


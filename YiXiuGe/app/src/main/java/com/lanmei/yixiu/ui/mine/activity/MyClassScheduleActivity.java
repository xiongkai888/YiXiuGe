package com.lanmei.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.SolarUtil;
import com.othershe.calendarview.weiget.CalendarView;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的课程表
 */
public class MyClassScheduleActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.calendar)
    CalendarView calendarView;
    @InjectView(R.id.title)
    TextView title;
    private FormatTime formatTime;
    private int year;
    private int month;
    private int filtrate = 0;//筛选0|1|2|3=>全部上课|评价|已评
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private YiXiuGeApi api = new YiXiuGeApi("app/syllabuslist");

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_schedule;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_class_schedule);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        api.addParams("uid", api.getUserId(this));
        formatTime = new FormatTime();
        year = formatTime.getYear();
        month = formatTime.getMonth();
//        initClassSchedule();


        calendarView.setInitDate(year + "." + month).init();

        title.setText(year + "年" + month);
        initClassSchedule();
        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
//                TextView solarDay = (TextView) view.findViewById(com.othershe.calendarview.R.id.solar_day);
//                TextView lunarDay = (TextView) view.findViewById(com.othershe.calendarview.R.id.lunar_day);
//                solarDay.setText("111");
//                lunarDay.setText("222");
//                IntentUtil.startActivity(getContext(), ClassDetailsActivity.class);
                CommonUtils.developing(getContext());
            }
        });


        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                L.d(L.TAG, date[0] + "年" + date[1] + "月一共" + SolarUtil.getMonthDays(date[0], date[1]) + "天");
                year = date[0];
                month = date[1];
                title.setText(date[0] + "年" + date[1] + "月");
                initClassSchedule();
            }
        });
    }


    private void initClassSchedule() {
        try {
            final int days = SolarUtil.getMonthDays(year, month);
            api.addParams("start_time", formatTime.dateToStampLong(year + "-" + month + "-" + 1, format));
            api.addParams("end_time", formatTime.dateToStampLong(year + "-" + month + "-" + days, format) + 86400);
            api.addParams("screen", filtrate);//筛选0|1|2|3=>全部|上课|评价|已评
            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<Integer>>() {
                @Override
                public void onResponse(NoPageListBean<Integer> response) {
                    if (isFinishing()) {
                        return;
                    }
                    List<Integer> list = response.data;
                    if (StringUtils.isEmpty(list)) {
                        return;
                    }
                    if (days != list.size()) {
                        return;
                    }
//                    List<Integer> integerList = new ArrayList<>();
//                    for (int i = 0;i<30;i++){
//                        Integer integer = new Integer(1);
//                        integerList.add(integer);
//                    }
                    calendarView.setParameter(list,year, month);
                    Log.d("AyncListObjects", "year:"+year+" , month" +month);
                    UIHelper.ToastMessage(getContext(), response.getMsg());
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.last_month_iv, R.id.next_month_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_month_iv:
                calendarView.lastMonth();
                break;
            case R.id.next_month_iv:
                calendarView.nextMonth();
                break;
        }
    }
}

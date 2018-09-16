package com.lanmei.yixiu.ui.mine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.othershe.calendarview.bean.CalendarEvent;
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
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private int filtrate = 0;//筛选0|1|2|3=>全部上课|评价|已评
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private YiXiuGeApi api = new YiXiuGeApi("app/syllabuslist");

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_schedule;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_class_schedule);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        api.addParams("uid", api.getUserId(this));
        formatTime = new FormatTime();
        int year= formatTime.getYear();
        int month = formatTime.getMonth();
        calendarView.setInitDate(year + "." + month).init();
        title.setText(year + "年" + month);
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
                title.setText(date[0] + "年" + date[1] + "月");
            }
        });
    }
    private void loadClassSchedule(final int year, final int month, final int monthDays, final int position) {
        try {
            api.addParams("start_time", formatTime.dateToStampLong(year + "-" + month + "-" + 1, format));
            api.addParams("end_time", formatTime.dateToStampLong(year + "-" + month + "-" + monthDays, format) + 86400);
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
                    if (monthDays != list.size()) {
                        return;
                    }
                    calendarView.setParameter(list,year, month,position);
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

    @Subscribe
    public void calendarEvent(CalendarEvent event){
        loadClassSchedule(event.getYear(),event.getMonth(),event.getMonthDays(),event.getPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

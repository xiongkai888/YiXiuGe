package com.medui.yixiu.ui.mine.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.ScheduleFiltrateAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.TeacherFiltrateBean;
import com.medui.yixiu.utils.CommonUtils;
import com.medui.yixiu.utils.FormatTime;
import com.othershe.calendarview.bean.CalendarEvent;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.weiget.CalendarView;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.utils.UIBaseUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的课程表
 */
public class MyClassScheduleActivity extends BaseActivity {

    //    @InjectView(R.id.toolbar)
//    CenterTitleToolbar toolbar;
    @InjectView(R.id.calendar)
    CalendarView calendarView;
    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.line_tv)
    TextView lineTv;
    private FormatTime formatTime;
    private int filtrate = 0;//筛选0|1|2|3=>全部上课|评价|已评
    private YiXiuGeApi api = new YiXiuGeApi("app/syllabuslist");//课程表

    @Override
    public int getContentViewId() {
        return R.layout.activity_class_schedule;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        toolbarNameTv.setText(R.string.my_class_schedule);
        menuTv.setText(R.string.all);
        menuTv.setVisibility(CommonUtils.isStudent(this)?View.VISIBLE:View.GONE);//是老师就隐藏掉

        api.addParams("uid", api.getUserId(this));
        formatTime = new FormatTime(this);
        formatTime.setApplyToTimeYearMonthDay();

        int year = formatTime.getYear();
        int month = formatTime.getMonth();

        String[] string = formatTime.getMonthAgoOrNext(year, month, true);
        if (!StringUtils.isEmpty(string) && string.length == 2) {
            calendarView.setStartEndDate((year - 3) + "." + month, string[0] + "." + string[1]);
        }
        calendarView.setInitDate(year + "." + month)
                .init();

        title.setText(getString(R.string.year_month, String.valueOf(year), String.valueOf(month)));
        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                if (date.getScreen() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", calendarView.getCurrentPosition());
                    bundle.putSerializable("bean", date);
                    IntentUtil.startActivity(getContext(), ClassDetailsActivity.class, bundle);
                }
            }
        });


        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
//                L.d(L.TAG, date[0] + "年" + date[1] + "月一共" + SolarUtil.getMonthDays(date[0], date[1]) + "天");
                title.setText(getString(R.string.year_month, String.valueOf(date[0]), String.valueOf(date[1])));
            }
        });
    }

    private void loadClassSchedule(final int year, final int month, final int monthDays, final int position) {
        try {
            api.addParams("start_time", formatTime.dateToStampLongSub(year + "-" + month + "-" + 1));
            api.addParams("end_time", formatTime.dateToStampLongSub(year + "-" + month + "-" + monthDays) + 86400);
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
                    calendarView.setParameter(list, year, month, position);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.last_month_iv, R.id.next_month_iv, R.id.menu_tv, R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_month_iv:
                calendarView.lastMonth();
                break;
            case R.id.next_month_iv:
                calendarView.nextMonth();
                break;
            case R.id.menu_tv:
                popupWindow();
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }


    PopupWindow window;
    int width;
    int xoff;

    private void popupWindow() {
        if (window != null) {
            window.showAsDropDown(lineTv, xoff, 2);
            return;
        }
        RecyclerView view = new RecyclerView(this);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setBackgroundColor(getResources().getColor(R.color.white));

        ScheduleFiltrateAdapter teacherFiltrateAdapter = new ScheduleFiltrateAdapter(this);
        teacherFiltrateAdapter.setData(getList());
        view.setAdapter(teacherFiltrateAdapter);
        teacherFiltrateAdapter.setScheduleFiltrateListener(new ScheduleFiltrateAdapter.ScheduleFiltrateListener() {
            @Override
            public void onFiltrate(TeacherFiltrateBean bean) {
                menuTv.setText(bean.getName());
                window.dismiss();
                calendarView.setType(Integer.valueOf(bean.getId()));
//                calendarView.getAdapter().notifyDataSetChanged();
            }
        });
        width = UIBaseUtils.dp2pxInt(this, 80);
        window = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        int paddingRight = UIBaseUtils.dp2pxInt(this, 0);
        xoff = SysUtils.getScreenWidth(this) - width - paddingRight;
        window.showAsDropDown(lineTv, xoff, 2);
//        L.d(L.TAG,"width:"+width+",paddingRight:"+paddingRight+",xoff:"+xoff);
    }

    private List<TeacherFiltrateBean> getList() {
        List<TeacherFiltrateBean> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TeacherFiltrateBean bean = new TeacherFiltrateBean();
            switch (i){
                case 0:
                    bean.setName("全部");
                    bean.setSelect(true);
                    break;
                case 1:
                    bean.setName("上课");
                    break;
                case 2:
                    bean.setName("未评价");
                    break;
                case 3:
                    bean.setName("已评价");
                    break;
            }
            bean.setId(String.valueOf(i));
            list.add(bean);
        }
        return list;
    }

    @Subscribe
    public void calendarEvent(CalendarEvent event) {
        loadClassSchedule(event.getYear(), event.getMonth(), event.getMonthDays(), event.getPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.othershe.calendarview.weiget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.othershe.calendarview.R;
import com.othershe.calendarview.bean.AttrsBean;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.CalendarViewAdapter;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonthView extends ViewGroup {

    private static final int ROW = 6;
    private static final int COLUMN = 7;

    private static final int COLOR_RESET = 0;//重置文字颜色
    private static final int COLOR_SET = 1;//设置文字颜色

    private Context mContext;

    private View lastClickedView;//记录上次点击的Item
    private int currentMonthDays;//记录当月天数
    private int lastMonthDays;//记录当月显示的上个月天数
    private int nextMonthDays;//记录当月显示的下个月天数

    private int item_layout;
    private CalendarViewAdapter calendarViewAdapter;
    private Set<Integer> chooseDays = new HashSet<>();//记录多选时当前页选中的日期
    private AttrsBean mAttrsBean;
    private List<DateBean> dates;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        mContext = context;
        setBackgroundColor(Color.WHITE);
    }

    private int type = 0;//设置(筛选) 上课、未评价、已评价 在日历上那个显示 0全部显示、1只上课可见、2只未评价可见、3只已评价可见

    /**
     *
     * @param type
     * @param isFiltrate 是不是筛选
     */
    public void setType(int type,boolean isFiltrate){
        this.type = type;
        if (isFiltrate){
            setDateList(dates,currentMonthDays);
        }
    }

    /**
     * @param dates            需要展示的日期数据
     * @param currentMonthDays 当月天数
     */
    public void setDateList(List<DateBean> dates, int currentMonthDays) {
        this.dates = dates;
        if (getChildCount() > 0) {
            removeAllViews();
        }
        lastMonthDays = 0;
        nextMonthDays = 0;
//        boolean findSingleDate = false;//是否找到单选时默认选中的日期
        chooseDays.clear();
        int size = dates.size();
        this.currentMonthDays = currentMonthDays;
        for (int i = 0; i < size; i++) {
            final DateBean date = dates.get(i);
//            Log.d("MonthView", "date"+i+" : " +date.toString());
            if (date.getType() == 0) {
                lastMonthDays++;
                if (!mAttrsBean.isShowLastNext()) {
                    addView(new View(mContext), i);
                    continue;
                }
            }

            if (date.getType() == 2) {
                nextMonthDays++;
                if (!mAttrsBean.isShowLastNext()) {
                    addView(new View(mContext), i);
                    continue;
                }
            }

            View view;
            TextView solarDay;//阳历TextView
            TextView lunarDay;//阴历TextView(节假日、节气同样使用阴历TextView来显示)
            if (item_layout != 0 && calendarViewAdapter != null) {
                view = LayoutInflater.from(mContext).inflate(item_layout, null);
                TextView[] views = calendarViewAdapter.convertView(view, date);
                solarDay = views[0];
                lunarDay = views[1];
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_month_layout, null);
                solarDay = (TextView) view.findViewById(R.id.solar_day);
                lunarDay = (TextView) view.findViewById(R.id.lunar_day);
            }

            solarDay.setTextColor(mAttrsBean.getColorSolar());
            solarDay.setTextSize(mAttrsBean.getSizeSolar());
            lunarDay.setTextColor(mAttrsBean.getColorLunar());
            lunarDay.setTextSize(mAttrsBean.getSizeLunar());

            //设置上个月和下个月的阳历颜色
            if (date.getType() == 0 || date.getType() == 2) {
                solarDay.setTextColor(mAttrsBean.getColorLunar());
            }
            solarDay.setText(String.valueOf(date.getSolar()[2]));
//            Log.d("AyncListObjects", "date"+i+" : " +date.toString());
            switch (date.getScreen()){
                case 1:
                    if (type == 0 || type == 1){
                        view.setBackgroundResource(mAttrsBean.getDayBg_1());
                        solarDay.setTextColor(mAttrsBean.getColorChoose());
                        lunarDay.setTextColor(mAttrsBean.getColorChoose());
                    }
//                    lunarDay.setText(R.string.attend_class);
                    break;
                case 2:
                    if (type == 0 || type == 2){
                        view.setBackgroundResource(mAttrsBean.getDayBg_2());
                        solarDay.setTextColor(mAttrsBean.getColorChoose());
                        lunarDay.setTextColor(mAttrsBean.getColorChoose());
                    }
//                    lunarDay.setText(R.string.evaluate);
                    break;
                case 3:
                    if (type == 0 || type == 3){
                        view.setBackgroundResource(mAttrsBean.getDayBg_3());
                        solarDay.setTextColor(mAttrsBean.getColorChoose());
                        lunarDay.setTextColor(mAttrsBean.getColorChoose());
                    }
//                    lunarDay.setText(R.string.evaluated);
                    break;
            }
            //设置农历（节假日显示）
            if (mAttrsBean.isShowLunar()) {
                if ("初一".equals(date.getLunar()[1])) {
                    lunarDay.setText(date.getLunar()[0]);
                    if ("正月".equals(date.getLunar()[0]) && mAttrsBean.isShowHoliday()) {
                        lunarDay.setTextColor(mAttrsBean.getColorHoliday());
                        lunarDay.setText("春节");
                    }
                } else {
                    if (!TextUtils.isEmpty(date.getSolarHoliday()) && mAttrsBean.isShowHoliday()) {//阳历节日
                        setLunarText(date.getSolarHoliday(), lunarDay, date.getType());
                    } else if (!TextUtils.isEmpty(date.getLunarHoliday()) && mAttrsBean.isShowHoliday()) {//农历节日
                        setLunarText(date.getLunarHoliday(), lunarDay, date.getType());
                    } else if (!TextUtils.isEmpty(date.getTerm()) && mAttrsBean.isShowTerm()) {//节气
                        setLunarText(date.getTerm(), lunarDay, date.getType());
                    } else {
                        if (TextUtils.isEmpty(date.getLunar()[1])) {
                            lunarDay.setVisibility(GONE);
                        } else {
                            lunarDay.setText(date.getLunar()[1]);//农历日期
                        }
                    }
                }

            } else {
                lunarDay.setVisibility(GONE);
            }

            //找到单选时默认选中的日期，并选中（如果有）
//            if (mAttrsBean.getChooseType() == 0
//                    && mAttrsBean.getSingleDate() != null
//                    && !findSingleDate
//                    && date.getType() == 1
//                    && mAttrsBean.getSingleDate()[0] == date.getSolar()[0]
//                    && mAttrsBean.getSingleDate()[1] == date.getSolar()[1]
//                    && mAttrsBean.getSingleDate()[2] == date.getSolar()[2]) {
//                lastClickedView = view;
//                setDayColor(view, COLOR_SET);
//                findSingleDate = true;
//            }

            //找到多选时默认选中的多个日期，并选中（如果有）
            if (mAttrsBean.getChooseType() == 1 && mAttrsBean.getMultiDates() != null) {
                for (int[] d : mAttrsBean.getMultiDates()) {
                    if (date.getType() == 1
                            && d[0] == date.getSolar()[0]
                            && d[1] == date.getSolar()[1]
                            && d[2] == date.getSolar()[2]) {
                        setDayColor(view, COLOR_SET);//重新写个方法
                        chooseDays.add(d[2]);
                        break;
                    }
                }
            }

            //设置禁用日期
            if (date.getType() == 1) {
                view.setTag(date.getSolar()[2]);
                if (mAttrsBean.getDisableStartDate() != null
                        && (CalendarUtil.dateToMillis(mAttrsBean.getDisableStartDate()) > CalendarUtil.dateToMillis(date.getSolar()))) {
                    solarDay.setTextColor(mAttrsBean.getColorLunar());
                    lunarDay.setTextColor(mAttrsBean.getColorLunar());
                    view.setTag(-1);
                    addView(view, i);
                    continue;
                }

                if (mAttrsBean.getDisableEndDate() != null
                        && (CalendarUtil.dateToMillis(mAttrsBean.getDisableEndDate()) < CalendarUtil.dateToMillis(date.getSolar()))) {
                    solarDay.setTextColor(mAttrsBean.getColorLunar());
                    lunarDay.setTextColor(mAttrsBean.getColorLunar());
                    view.setTag(-1);
                    addView(view, i);
                    continue;
                }
            }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int day = date.getSolar()[2];
                    CalendarView calendarView = (CalendarView) getParent();
                    OnSingleChooseListener clickListener = calendarView.getSingleChooseListener();
                    if (clickListener != null) {
                        clickListener.onSingleChoose(v, date);
//                        Log.d("AyncListObjects", date.toString());
                    }

//                    OnMultiChooseListener chooseListener = calendarView.getMultiChooseListener();

//                    if (date.getType() == 1) {//点击当月
//                        if (mAttrsBean.getChooseType() == 1 && chooseListener != null) {//多选的情况
//                            boolean flag;
//                            if (chooseDays.contains(day)) {
//                                setDayColor(v, COLOR_RESET);
//                                chooseDays.remove(day);
//                                flag = false;
//                            } else {
//                                setDayColor(v, COLOR_SET);
//                                chooseDays.add(day);
//                                flag = true;
//                            }
//                            calendarView.setChooseDate(day, flag, -1);
//                            chooseListener.onMultiChoose(v, date, flag);
//                        } else {
//                            calendarView.setLastClickDay(day);
//                            if (lastClickedView != null) {
//                                setDayColor(lastClickedView, COLOR_RESET);
//                            }
//                            setDayColor(v, COLOR_SET);
//                            lastClickedView = v;
//
//                            if (clickListener != null) {
//                                clickListener.onSingleChoose(v, date);
//                            }
//                        }
//                    } else if (date.getType() == 0) {//点击上月
//                        if (mAttrsBean.isSwitchChoose()) {
//                            calendarView.setLastClickDay(day);
//                        }
//                        calendarView.lastMonth();
//                        if (clickListener != null) {
//                            clickListener.onSingleChoose(v, date);
//                        }
//                    } else if (date.getType() == 2) {//点击下月
//                        if (mAttrsBean.isSwitchChoose()) {
//                            calendarView.setLastClickDay(day);
//                        }
//                        calendarView.nextMonth();
//                        if (clickListener != null) {
//                            clickListener.onSingleChoose(v, date);
//                        }
//                    }
                }
            });
            addView(view, i);
        }
        requestLayout();
    }

    private void setLunarText(String str, TextView text, int type) {
        text.setText(str);
        if (type == 1) {
            text.setTextColor(mAttrsBean.getColorHoliday());
        }
        text.setTag("holiday");
    }

    private void setDayColor(View v, int type) {
        TextView solarDay = (TextView) v.findViewById(R.id.solar_day);
        TextView lunarDay = (TextView) v.findViewById(R.id.lunar_day);
        solarDay.setTextSize(mAttrsBean.getSizeSolar());
        lunarDay.setTextSize(mAttrsBean.getSizeLunar());

        if (type == 0) {
            v.setBackgroundResource(0);
            solarDay.setTextColor(mAttrsBean.getColorSolar());
            if ("holiday".equals(lunarDay.getTag())) {
                lunarDay.setTextColor(mAttrsBean.getColorHoliday());
            } else {
                lunarDay.setTextColor(mAttrsBean.getColorLunar());
            }
        } else if (type == 1) {//这里设置选中日期的背景颜色、日期字体颜色和农历字体颜色
//            if (type == 1) {
//                v.setBackgroundResource(mAttrsBean.getDayBg_1());
//            } else if (type == 2) {
//                v.setBackgroundResource(mAttrsBean.getDayBg_1());
//            } else if (type == 3) {
//                v.setBackgroundResource(mAttrsBean.getDayBg_1());
//            }
            v.setBackgroundResource(mAttrsBean.getDayBg_1());
            solarDay.setTextColor(mAttrsBean.getColorChoose());
            lunarDay.setTextColor(mAttrsBean.getColorChoose());
            lunarDay.setText("上课");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int itemWidth = widthSpecSize / COLUMN;

        //计算日历的最大高度
        if (heightSpecSize > itemWidth * ROW) {
            heightSpecSize = itemWidth * ROW;
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);

        int itemHeight = heightSpecSize / ROW;

        int itemSize = Math.min(itemWidth, itemHeight);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }

        View childView = getChildAt(0);
        int itemWidth = childView.getMeasuredWidth();
        int itemHeight = childView.getMeasuredHeight();
        //计算列间距
        int dx = (getMeasuredWidth() - itemWidth * COLUMN) / (COLUMN * 2);

        //当显示五行时扩大行间距
        int dy = 0;
        if (getChildCount() == 35) {
            dy = itemHeight / 5;
        }

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);

            int left = i % COLUMN * itemWidth + ((2 * (i % COLUMN) + 1)) * dx;
            int top = i / COLUMN * (itemHeight + dy);
            int right = left + itemWidth;
            int bottom = top + itemHeight;
            view.layout(left, top, right, bottom);
        }
    }

    public void refresh(int day, boolean flag) {
        if (lastClickedView != null) {
            setDayColor(lastClickedView, COLOR_RESET);
        }
        if (!flag) {
            return;
        }
        View destView = findDestView(day);
        if (destView == null) {
            return;
        }
        setDayColor(destView, COLOR_SET);
        lastClickedView = destView;
        invalidate();
    }

    /**
     * 多选时刷新日期，以恢复之前选中的日期
     *
     * @param set
     */
    public void multiChooseRefresh(HashSet<Integer> set) {
        for (Integer day : set) {
            setDayColor(findDestView(day), COLOR_SET);
            chooseDays.add(day);
        }
        invalidate();
    }

    /**
     * 查找要跳转到的页面需要展示的日期View
     *
     * @param day
     * @return
     */
    private View findDestView(int day) {
        View view = null;
        for (int i = lastMonthDays; i < getChildCount() - nextMonthDays; i++) {
            int dayNum = (Integer) getChildAt(i).getTag();
            Log.d("lastMonthDays", dayNum + "," + day);
            if (dayNum == day) {
                view = getChildAt(i);
                break;
            }
        }

        if (view == null) {
            view = getChildAt(currentMonthDays + lastMonthDays - 1);
        }

        if ((Integer) view.getTag() == -1) {
            view = null;
        }
        return view;
    }

    public void setAttrsBean(AttrsBean attrsBean) {
        mAttrsBean = attrsBean;
    }

    public void setOnCalendarViewAdapter(int item_layout, CalendarViewAdapter calendarViewAdapter) {
        this.item_layout = item_layout;
        this.calendarViewAdapter = calendarViewAdapter;
    }
}

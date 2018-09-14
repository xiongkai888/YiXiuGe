package com.othershe.calendarview.weiget;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.othershe.calendarview.bean.AttrsBean;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.CalendarViewAdapter;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.utils.SolarUtil;

import java.util.LinkedList;
import java.util.List;

public class CalendarPagerAdapter extends PagerAdapter {

    //缓存上一次回收的MonthView
    private LinkedList<MonthView> cache = new LinkedList<>();
    private SparseArray<MonthView> mViews = new SparseArray<>();
    private int year;//日历当前年
    private int month;//日历当前月
    private List<Integer> list;//网络请求筛选的数据
    private int listCount;//数据个数
    private int count;

    private int item_layout;
    private CalendarViewAdapter calendarViewAdapter;

    private AttrsBean mAttrsBean;

    public CalendarPagerAdapter(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setParameter(List<Integer> list, int year, int month) {
        this.list = list;
        this.year = year;
        this.month = month;
        listCount = (list == null) ? 0 : list.size();
//        Log.d("AyncListObjects", "year:"+year+" , month:" +month);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
        } else {
            view = new MonthView(container.getContext());
        }
        //根据position计算对应年、月
        int[] date = CalendarUtil.positionToDate(position, mAttrsBean.getStartDate()[0], mAttrsBean.getStartDate()[1]);
        view.setAttrsBean(mAttrsBean);
        view.setOnCalendarViewAdapter(item_layout, calendarViewAdapter);
        List<DateBean> dateBeanList = CalendarUtil.getMonthDate(date[0], date[1], mAttrsBean.getSpecifyMap());
        int monthDays = SolarUtil.getMonthDays(date[0], date[1]);
//        Log.d("AyncListObjects", "year:"+year+" , month:" +month);
        if (year != 0 && listCount == monthDays){
            int size = dateBeanList.size();
            for (int i = 0; i < size; i++) {
                DateBean bean = dateBeanList.get(i);
                if (bean.getSolar()[0] == year && bean.getSolar()[1] == month) {
                    int day = bean.getSolar()[2];
//                    Log.d("AyncListObjects", "day"+day+" : " +date.toString());
                    if (day < listCount) {
                        bean.setScreen(list.get(day-1));
                    }
                }
            }
        }
        view.setDateList(dateBeanList, monthDays);
        mViews.put(position, view);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((MonthView) object);
        cache.addLast((MonthView) object);
        mViews.remove(position);
    }

    /**
     * 获得ViewPager缓存的View
     *
     * @return
     */
    public SparseArray<MonthView> getViews() {
        return mViews;
    }


    public void setAttrsBean(AttrsBean attrsBean) {
        mAttrsBean = attrsBean;
    }

    public void setOnCalendarViewAdapter(int item_layout, CalendarViewAdapter calendarViewAdapter) {
        this.item_layout = item_layout;
        this.calendarViewAdapter = calendarViewAdapter;
    }
}

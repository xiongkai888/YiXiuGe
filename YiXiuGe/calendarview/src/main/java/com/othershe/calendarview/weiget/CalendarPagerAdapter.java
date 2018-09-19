package com.othershe.calendarview.weiget;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.othershe.calendarview.bean.AttrsBean;
import com.othershe.calendarview.bean.CalendarEvent;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.CalendarViewAdapter;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.utils.SolarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CalendarPagerAdapter extends PagerAdapter {

    //缓存上一次回收的MonthView
    private LinkedList<MonthView> cache = new LinkedList<>();
    private SparseArray<MonthView> mViews = new SparseArray<>();
    private int listCount;//数据个数
    private int count;

    private Map<String, List<DateBean>> listMap = new HashMap<>();

    private int item_layout;
    private int type = 0;//设置(筛选) 上课、未评价、已评价 在日历上那个显示 0全部显示、1只上课可见、2只未评价可见、3只已评价可见
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

    public void setParameter(List<Integer> list, int year, int month,int position) {
        listCount = (list == null) ? 0 : list.size();
        if (listMap.containsKey(year + "-" + month)) {
            List<DateBean> beanList = listMap.get(year + "-" + month);
            if (beanList != null && listCount > 0) {
                int size = beanList.size();
                for (int i = 0; i < size; i++) {
                    DateBean bean = beanList.get(i);
                    if (bean.getSolar()[0] == year && bean.getSolar()[1] == month){
                        bean.setScreen(list.get(bean.getSolar()[2]-1));
                    }
                }
            }
            MonthView monthView = mViews.get(position);
            monthView.setDateList(beanList,listCount);
        }
    }

    /**
     *
     * @param type
     * @param currentPosition 当前日历的下标
     */
    public void setType(int type,int currentPosition){
        this.type = type;
        mViews.get(currentPosition).setType(type,true);
        if (currentPosition != 0){
            mViews.get(currentPosition-1).setType(type,true);
        }
        if (currentPosition+1 != count){
            mViews.get(currentPosition+1).setType(type,true);
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthView view;
        if (!cache.isEmpty()) {
            view = cache.removeFirst();
        } else {
            view = new MonthView(container.getContext());
        }
        view.setType(type,false);
        //根据position计算对应年、月
        int[] date = CalendarUtil.positionToDate(position, mAttrsBean.getStartDate()[0], mAttrsBean.getStartDate()[1]);
        int yearPosition = date[0];
        int monthPosition = date[1];
        view.setAttrsBean(mAttrsBean);
        view.setOnCalendarViewAdapter(item_layout, calendarViewAdapter);
        List<DateBean> dateBeanList;
        String key = yearPosition + "-" + monthPosition;
        int monthDays = SolarUtil.getMonthDays(yearPosition, monthPosition);
        if (listMap.containsKey(key)) {
            dateBeanList = listMap.get(key);
        } else {
            dateBeanList = CalendarUtil.getMonthDate(yearPosition, monthPosition, mAttrsBean.getSpecifyMap());
            listMap.put(key, dateBeanList);
            EventBus.getDefault().post(new CalendarEvent(yearPosition, monthPosition, monthDays,position));
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

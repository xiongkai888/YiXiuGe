package com.medui.yixiu.ui.teacher.presenter;

import com.medui.yixiu.bean.SelectStudentsBean;
import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkai on 2018/8/24.
 */

public class SelectStudentsDataHelper {

    private List<SelectStudentsBean> list;

    public void setList(List<SelectStudentsBean> list) {
        this.list = list;
    }

    public List<SelectStudentsBean> getList() {
        return list;
    }

    public boolean isAllSelect() {
        if (isNull()) {
            return false;
        }
        for (SelectStudentsBean bean : list) {
            if (!bean.isSelect()){
                return false;
            }
        }
        return true;
    }

    public boolean isNull() {
        return StringUtils.isEmpty(list);
    }

    //是否有一个或以上被选中
    public boolean isSelect() {
        if (isNull()) {
            return false;
        }
        for (SelectStudentsBean bean : list) {
            if (bean.isSelect()){
                return bean.isSelect();
            }
        }
        return false;
    }

    public void setInvertSelect() {
        if (isNull()) {
            return;
        }
        for (SelectStudentsBean bean : list) {
            bean.setSelect(!bean.isSelect());
        }
    }

    public List<SelectStudentsBean> getSelectStudents() {
        if (isNull()) {
            return null;
        }
        List<SelectStudentsBean> list = new ArrayList<>();
        for (SelectStudentsBean bean : list) {
            if (bean.isSelect()){
                list.add(bean);
            }
        }
        return list;
    }

    public void setAllSelect(boolean isAllSelect) {
        if (isNull()) {
            return ;
        }
        for (SelectStudentsBean bean : list) {
            bean.setSelect(isAllSelect);
        }
    }

}

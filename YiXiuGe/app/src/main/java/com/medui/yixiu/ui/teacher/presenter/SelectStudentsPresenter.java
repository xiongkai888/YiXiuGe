package com.medui.yixiu.ui.teacher.presenter;

import com.medui.yixiu.bean.SelectStudentsBean;

import java.util.List;

/**
 * Created by xkai on 2018/8/24.
 */

public class SelectStudentsPresenter implements SelectStudentsContract.Presenter {

    private SelectStudentsContract.View view;
    private SelectStudentsDataHelper dataHelper;
    private boolean isInvert;

    public SelectStudentsPresenter(SelectStudentsContract.View view) {
        this.view = view;
        dataHelper = new SelectStudentsDataHelper();
    }

    @Override
    public void setList(List<SelectStudentsBean> list) {
        dataHelper.setList(list);
    }

    @Override
    public boolean isNull() {
        return dataHelper.isNull();
    }

    @Override
    public boolean isInvert() {
        return isInvert;
    }

    @Override
    public void setInvert(boolean isInvert) {
        this.isInvert = isInvert;
    }

    @Override
    public boolean isAllSelect() {
        return dataHelper.isAllSelect();
    }

    @Override
    public boolean isSelect() {
        return dataHelper.isSelect();
    }

    @Override
    public void setAllSelect(boolean isAllSelect) {
        view.showAllSelect(isAllSelect);
        dataHelper.setAllSelect(isAllSelect);
        view.showInvertSelect(false);
        setInvert(false);
    }

    @Override
    public void setInvertSelect(boolean isInvertSelect) {
        view.showInvertSelect(isInvertSelect);
        dataHelper.setInvertSelect();
        view.showAllSelect(isAllSelect());
        setInvert(isInvertSelect);
    }

    @Override
    public List<SelectStudentsBean> getSelectStudents() {
        return dataHelper.getSelectStudents();
    }

    @Override
    public void onClickSingleItem() {
        view.showInvertSelect(false);
        setInvert(false);
        view.showAllSelect(isAllSelect());
    }
}

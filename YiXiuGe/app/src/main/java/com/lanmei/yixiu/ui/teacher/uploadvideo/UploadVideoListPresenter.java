package com.lanmei.yixiu.ui.teacher.uploadvideo;

import android.content.Context;

import java.util.List;

/**
 * Created by xkai on 2018/6/7.
 */

public class UploadVideoListPresenter implements UploadVideoListContract.Presenter {

    private UploadVideoListContract.View view;
    private UploadVideoListDataHelper dataHelper;
    private boolean isEdit = false;//是否为编辑状态

    @Override
    public boolean isSelect() {
        return dataHelper.isSelect();
    }

    @Override
    public List<UploadVideoBean> getListBySelected() {
        return dataHelper.getListBySelected();
    }

    @Override
    public boolean isEdit() {
        return isEdit;
    }

    @Override
    public void insertUploadVideoBean(UploadVideoBean bean) {
        dataHelper.insertUploadVideoBean(bean);
    }

    @Override
    public void deleteUploadVideoBean(UploadVideoBean bean) {
        dataHelper.deleteUploadVideoBean(bean);
    }

    @Override
    public void deleteBySelectBean() {
        dataHelper.deleteBySelectBean();
    }

    @Override
    public List<UploadVideoBean> getUploadVideoList() {
        return dataHelper.getList();
    }

    @Override
    public void setAllSelect(boolean isAllSelect) {
        dataHelper.setAllSelect(isAllSelect);
        view.showTextView(isAllSelect);
    }

    @Override
    public void showAllSelect(boolean isAllSelect) {
        view.showTextView(isAllSelect);
    }

    @Override
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        view.showBottom(isEdit);//是否显示底部
    }

    @Override
    public String getIdsBySelectedAndNoRead() {
        return dataHelper.getIdsBySelectedAndNoRead();
    }

    @Override
    public int getUploadVideoCount() {
        return dataHelper.getUploadVideoCount();
    }

    public UploadVideoListPresenter(Context context, UploadVideoListContract.View view) {
        this.view = view;
        dataHelper = new UploadVideoListDataHelper(context);
    }

    @Override
    public boolean isAllSelect() {
        return dataHelper.isAllSelect();
    }



}

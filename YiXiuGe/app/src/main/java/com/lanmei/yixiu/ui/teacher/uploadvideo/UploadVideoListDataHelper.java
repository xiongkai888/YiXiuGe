package com.lanmei.yixiu.ui.teacher.uploadvideo;

import android.content.Context;

import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkai on 2018/6/7.
 */

public class UploadVideoListDataHelper {

    private DBUploadViewHelper dbUploadViewHelper;
    private List<UploadVideoBean> list;

    public List<UploadVideoBean> getList() {
        return list;
    }

    public UploadVideoListDataHelper(Context context) {
        dbUploadViewHelper = DBUploadViewHelper.getInstance(context);
        list = dbUploadViewHelper.getUploadVideoList();
    }


    public void deleteBySelectBean() {
        dbUploadViewHelper.delete(getListBySelected());
    }

    public boolean isAllSelect() {//是否全部选中
        if (isNull()) {
            return false;
        }
        for (UploadVideoBean bean : list) {
            if (!bean.isEdit()) {
                return false;
            }
        }
        return true;
    }

    private boolean isNull() {
        return StringUtils.isEmpty(list);
    }


    //被选中的
    public List<UploadVideoBean> getListBySelected() {
        List<UploadVideoBean> beanList = new ArrayList<>();
        if (isNull()){
            return beanList;
        }
        for (UploadVideoBean bean : list) {
            if (bean.isEdit()) {
                beanList.add(bean);
            }
        }
        return beanList;
    }

    //用于设置为已读
    public String getIdsBySelectedAndNoRead() {
        if (isNull()){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (UploadVideoBean bean : list) {
//            if (bean.isEdit() && StringUtils.isSame(bean.getSee_state(), CommonUtils.isZero)) {
//                buffer.append(bean.getId() + d);
//            }
        }
        return "";
    }


    //设置全选还是全不选
    public void setAllSelect(boolean isAllSelect) {
        if (isNull()){
            return;
        }
        for (UploadVideoBean bean : list) {
            bean.setEdit(isAllSelect);
        }
    }

    public void insertUploadVideoBean(UploadVideoBean bean) {
        dbUploadViewHelper.insertUploadVideoBean(bean);
    }

    //是否交易通知被选中
    public boolean isSelect() {
        if (isNull()){
            return false;
        }
        for (UploadVideoBean bean : list) {
            if (bean.isEdit()) {
                return true;
            }
        }
        return false;
    }

}

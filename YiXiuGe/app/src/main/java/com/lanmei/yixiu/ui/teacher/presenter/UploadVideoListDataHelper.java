package com.lanmei.yixiu.ui.teacher.presenter;

import com.xson.common.utils.StringUtils;

import java.util.List;

/**
 * Created by xkai on 2018/6/7.
 */

public class UploadVideoListDataHelper {

    private String d = ",";

    public UploadVideoListDataHelper() {

    }

    public boolean isAllSelect() {//是否全部选中
        if (isNull()) {
            return false;
        }
        for (UploadVideoListBean bean : list) {
            if (!bean.isEdit()) {
                return false;
            }
        }
        return true;
    }

    private List<UploadVideoListBean> list;

    public void setList(List<UploadVideoListBean> list) {
        this.list = list;
    }

    private boolean isNull() {
        return StringUtils.isEmpty(list);
    }


    //被选中的通知ids 用户删除通知
    public String getIdBySelected() {
        if (isNull()){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (UploadVideoListBean bean : list) {
//            if (bean.isEdit()) {
//                buffer.append(bean.getId() + d);
//            }
        }
        return "";
    }

    //用于设置为已读
    public String getIdsBySelectedAndNoRead() {
        if (isNull()){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (UploadVideoListBean bean : list) {
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
        for (UploadVideoListBean bean : list) {
            bean.setEdit(isAllSelect);
        }
    }

    //是否交易通知被选中
    public boolean isSelect() {
        if (isNull()){
            return false;
        }
        for (UploadVideoListBean bean : list) {
            if (bean.isEdit()) {
                return true;
            }
        }
        return false;
    }

}

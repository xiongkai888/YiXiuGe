package com.lanmei.yixiu.ui.teacher.presenter;

import java.util.List;

/**
 * Created by xkai on 2018/6/7.
 */

public class UploadVideoListContract {

    public interface View {
        void showTextView(boolean isAllSelect);//是否全选或者取消
        void showBottom(boolean isShow);//是否显示底部
    }

    public interface Presenter {
        boolean isAllSelect();//是否全部选中
        boolean isSelect();//是否有选中的
        void setAllSelect(boolean isAllSelect);//设置全选或全部取消
        void showAllSelect(boolean isAllSelect);//点击item时设置是否为全部选中
        void setEdit(boolean isEdit);//是不是设置为编辑状态
        String getIdBySelected();//获取选中的id，用，拼接
        boolean isEdit();
        void setList(List<UploadVideoListBean> list);
        String getIdsBySelectedAndNoRead();//用于设置为已读
    }
}

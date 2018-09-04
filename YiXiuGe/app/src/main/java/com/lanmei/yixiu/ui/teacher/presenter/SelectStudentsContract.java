package com.lanmei.yixiu.ui.teacher.presenter;

import com.lanmei.yixiu.bean.SelectStudentsBean;

import java.util.List;

/**
 * Created by xkai on 2018/8/24.
 */

public class SelectStudentsContract {

    public interface View {

        void showAllSelect(boolean isAllSelect);//是否全选或者取消

        void showInvertSelect(boolean isInvertSelect);//反向选择
    }

    public interface Presenter {

        boolean isAllSelect();//是否全部选中

        boolean isSelect();//是否有选中的

        void setAllSelect(boolean isAllSelect);//设置全选或全部取消

        void setInvertSelect(boolean isInvertSelect);//反选

        List<SelectStudentsBean> getSelectStudents();//获取选中的学生

        void onClickSingleItem();//点击item

        void setList(List<SelectStudentsBean> list);

        boolean isNull();//数据是否为空

        boolean isInvert();

        void setInvert(boolean isInvert);

    }
}

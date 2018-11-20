package com.medui.yixiu.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.AddQuestionnaireOptionBean;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by xkai on 2018/8/27.
 */

public class AddQuestionnaireOptionHelper {

    private Context context;
    LinearLayout root;
    List<AddQuestionnaireOptionBean> list;

    public List<AddQuestionnaireOptionBean> getList() {
        return list;
    }

    private int size;

    public AddQuestionnaireOptionHelper(Context context, LinearLayout root) {
        this.context = context;
        this.root = root;
        list = new ArrayList<>();
        list.add(new AddQuestionnaireOptionBean());
        list.add(new AddQuestionnaireOptionBean());
        setDate(list);
    }

    private void addView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_questionnaire_option, null);
        root.addView(view);
        new ViewHolder(view, position);
    }

    public class ViewHolder {

        @InjectView(R.id.option_et)
        EditText optionEt;
        @InjectView(R.id.option_minus_tv)
        ImageView optionMinusTv;
        @InjectView(R.id.option_plus_tv)
        ImageView optionPlusTv;
        AddQuestionnaireOptionBean bean;
        int position;

        /**
         * @param position
         */
        ViewHolder(View view, int position) {
            ButterKnife.inject(this, view);
            bean = list.get(position);
            bean.setId(position+1);
            this.position = position;
            setListener();
            setParameter();
        }

        public void setParameter() {
            optionEt.setText(bean.getText());
            L.d(L.TAG, size - 1 + "," + position);
            if (size - 1 == position) {
                optionPlusTv.setVisibility(View.VISIBLE);
            } else {
                optionPlusTv.setVisibility(View.INVISIBLE);
            }
        }

        private void setListener() {
            optionEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setText(s + "");
                }
            });
            optionPlusTv.setOnClickListener(new View.OnClickListener() {//加
                @Override
                public void onClick(View v) {
                    plusQuestionnaireOption();
                }
            });
            optionMinusTv.setOnClickListener(new View.OnClickListener() {//减
                @Override
                public void onClick(View v) {
                    if (size <= 2) {
                        UIHelper.ToastMessage(context, "至少两个选项");
                        return;
                    }
                    minusQuestionnaireOption(position);
                }
            });
        }
    }

    public void setDate(List<AddQuestionnaireOptionBean> list) {
        this.list = list;
        root.removeAllViews();
        if (StringUtils.isEmpty(this.list)) {
            this.list.add(new AddQuestionnaireOptionBean());
        }
        size = list.size();
        for (int i = 0; i < size; i++) {
            addView(i);
        }
    }

    public void plusQuestionnaireOption() {
        list.add(new AddQuestionnaireOptionBean());
        setDate(list);
    }

    public void minusQuestionnaireOption(int position) {
        list.remove(position);
        setDate(list);
    }

    //是否填完选项
    public boolean isComplete() {
        for (int i = 0; i < size; i++) {
            AddQuestionnaireOptionBean bean = list.get(i);
            if (StringUtils.isEmpty(bean.getText())){
                return false;
            }
        }
        return true;
    }

}

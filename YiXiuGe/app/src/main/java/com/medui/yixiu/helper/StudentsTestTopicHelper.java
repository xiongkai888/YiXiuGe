package com.medui.yixiu.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.StudentTestAnswerAdapter;
import com.medui.yixiu.bean.StudentTestAnswerBean;
import com.medui.yixiu.bean.StudentTestBean;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xkai on 2018/12/12.
 * 学生考试（测试）
 */

public class StudentsTestTopicHelper {


    private Context context;
    private LinearLayout root;
    private List<StudentTestBean> list;
    private Map<Integer, ViewHolder> map;
    private StudentTestAnswerAdapter answerAdapter;//答案
    private List<StudentTestAnswerBean> beanList;//答案列表

    public void setBeanList(List<StudentTestAnswerBean> beanList) {
        this.beanList = beanList;
    }

    public void setAnswerAdapter(StudentTestAnswerAdapter answerAdapter) {
        this.answerAdapter = answerAdapter;
    }

    public StudentsTestTopicHelper(Context context, LinearLayout root) {
        map = new HashMap<>();
        this.context = context;
        this.root = root;
    }

    private boolean isEmpty() {
        return StringUtils.isEmpty(list);
    }

    public void setData(List<StudentTestBean> list) {
        this.list = list;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            addView(i);
        }
    }


    private void addView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_text_topic, null);
        root.addView(view, position);
        map.put(position, new ViewHolder(view, position));
    }


    public class ViewHolder {

        @InjectView(R.id.title_tv)
        TextView titleTv;//题目

        @InjectView(R.id.right_tv)
        TextView rightTv;//对
        @InjectView(R.id.wrong_tv)
        TextView wrongTv;//错
        @InjectView(R.id.ll_judge)
        LinearLayout llJudge;//判断题
        @InjectView(R.id.score_tv)
        TextView scoreTv;//打分题分数
        @InjectView(R.id.score_range_tv)
        TextView scoreRangeTv;//打分题分数范围

        @InjectView(R.id.ll_right)
        LinearLayout ll_right;//
        @InjectView(R.id.ll_wrong)
        LinearLayout ll_wrong;//

        @InjectView(R.id.ll_mark)
        LinearLayout llMark;//打分题
        @InjectView(R.id.ll_remark)
        LinearLayout llRemark;//备注
        @InjectView(R.id.remark_et)
        EditText remarkEt;//备注
        private StudentTestBean bean;

        /**
         * @param position 考试题目
         */
        public ViewHolder(View view, int position) {
            ButterKnife.inject(this, view);
            setParameter(position);
        }

        public void setParameter(final int position) {
            bean = list.get(position);
            boolean isJudge = StringUtils.isSame(CommonUtils.isTwo, bean.getType());
            String title = isJudge ? bean.getTitle1() + bean.getTitle2() + bean.getTitle() : bean.getTitle();
            llJudge.setVisibility(isJudge ? View.GONE : View.VISIBLE);
            llMark.setVisibility(isJudge ? View.VISIBLE : View.GONE);
            titleTv.setText(String.format(context.getString(R.string.topic_title1), String.valueOf((position + 1)), title));
            StudentTestAnswerBean answerBean = beanList.get(position);
            String answer = answerBean.getScore();
            if (!isJudge) {//判断题
                if (StringUtils.isEmpty(answer)) {
                    rightTv.setBackgroundResource(R.drawable.circle_topic_off);
                    wrongTv.setBackgroundResource(R.drawable.circle_topic_off);
                } else {
                    setRightOrWrong(StringUtils.isSame(CommonUtils.isOne, answer) ? rightTv : wrongTv, answer,position);
                }
            } else {//打分题
//                initPicker(getMarkList(bean.getMin_mark(), bean.getMax_mark()));
                scoreTv.setText(StringUtils.isEmpty(answer) ? String.format(context.getString(R.string.score), bean.getMin_mark()) : String.format(context.getString(R.string.score), answer));
                scoreRangeTv.setText(String.format(context.getString(R.string.score_range), bean.getMax_mark(), bean.getMin_mark()));
                scoreTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pickListener !=  null){
                            pickListener.setMarkList(bean.getMin_mark(),bean.getMax_mark(),position);
                        }
                    }
                });
            }
            remarkEt.setText(answerBean.getText());

            remarkEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!StringUtils.isEmpty(list)) {
                        beanList.get(position).setText(s + "");
                    }
                }
            });
            ll_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRightOrWrong(rightTv,CommonUtils.isOne,position);
                }
            });
            ll_wrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRightOrWrong(wrongTv, CommonUtils.isZero,position);
                }
            });
        }

        //判断题
        private void setRightOrWrong(TextView view, String answer,int position) {
            rightTv.setBackgroundResource(R.drawable.circle_topic_off);
            wrongTv.setBackgroundResource(R.drawable.circle_topic_off);

            view.setBackgroundResource(R.drawable.circle_topic_on);
            setAnswer(answer,position);
        }

        public void setAnswer(String answer,int position) {
            StudentTestAnswerBean answerBean = beanList.get(position);
            if (StringUtils.isSame(answer, answerBean.getScore())) {
                return;
            }
            answerBean.setScore(answer);
            answerAdapter.notifyItemChanged(position);
        }
    }

    private PickListener pickListener;

    public interface PickListener{
        void setMarkList(String min_mark, String max_mark,int position);
    }

    public void setPickListener(PickListener pickListener){
        this.pickListener = pickListener;
    }

    public void setDataByPosition(String answer,int position){
        ViewHolder viewHolder = map.get(position);
        viewHolder.scoreTv.setText(String.format(context.getString(R.string.score), answer));
        viewHolder.setAnswer(answer,position);
        beanList.get(position).setScore(answer);
    }

}

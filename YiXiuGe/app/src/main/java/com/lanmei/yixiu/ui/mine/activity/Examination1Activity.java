package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.YiXiuApp;
import com.lanmei.yixiu.adapter.ExaminationAnswerAdapter;
import com.lanmei.yixiu.adapter.ExaminationTopicAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.ExaminationAnswerBean;
import com.lanmei.yixiu.bean.ExaminationBean;
import com.lanmei.yixiu.bean.ExaminationListBean;
import com.lanmei.yixiu.event.ExaminationFinishEvent;
import com.lanmei.yixiu.helper.ClickAnswerListener;
import com.lanmei.yixiu.helper.ExaminationContract;
import com.lanmei.yixiu.helper.ExaminationPresenter;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * (学生考试)随堂测试
 */
public class Examination1Activity extends BaseActivity implements ExaminationContract.View {


    @InjectView(R.id.toolbar_name_tv)
    TextView toolbarNameTv;
    @InjectView(R.id.menu_tv)
    TextView menuTv;
    @InjectView(R.id.next_bt)
    Button nextBt;//下一题
    @InjectView(R.id.title_tv)
    TextView titleTv;//题目
    @InjectView(R.id.ll_bt)
    LinearLayout llBt;//上一题下一题按钮
    @InjectView(R.id.recyclerView_topic)
    RecyclerView recyclerViewTopic;//试题
    @InjectView(R.id.recyclerView_answer)
    RecyclerView recyclerViewAnswer;//答案
    ExaminationTopicAdapter examinationTopicAdapter;//选择题
    ExaminationAnswerAdapter examinationAnswerAdapter;//答案

    private String id;//考试id
    private ExaminationBean bean;//考试信息

    private List<ExaminationBean.ExamBean> list;//考试题目列表
    private List<ExaminationAnswerBean> beanList;//答案列表
    private int number;//总的题目数量
    private int position;//答题下标
    private ExaminationPresenter presenter;
    private ExaminationListBean examinationListBean;//
    private boolean isSubmit;//是否提交过
    private boolean isSave;//保存的周期和应用的生命周期一样长（暂时先这样）


    @Override
    public int getContentViewId() {
        return R.layout.activity_examination;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            examinationListBean = (ExaminationListBean) bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        if (examinationListBean != null) {
            id = examinationListBean.getId();
            //1|2=>综合考试|随堂考试
            toolbarNameTv.setText((StringUtils.isSame(examinationListBean.getType(), CommonUtils.isTwo)) ? R.string.quiz : R.string.comprehensive_examination);
        }
        presenter = new ExaminationPresenter(this, this);
        //试题
        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(this));
        examinationTopicAdapter = new ExaminationTopicAdapter(this);
        examinationTopicAdapter.setPresenter(presenter);
        recyclerViewTopic.setAdapter(examinationTopicAdapter);
//        recyclerViewTopic.setNestedScrollingEnabled(false);


        //试题答案
        recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 4));
        examinationAnswerAdapter = new ExaminationAnswerAdapter(this);
        recyclerViewAnswer.setNestedScrollingEnabled(false);
        examinationAnswerAdapter.setClickAnswerListener(new ClickAnswerListener() {
            @Override
            public void onClick(int position) {
                setAnswerPosition(position);
            }
        });

        bean = YiXiuApp.getInstance().getExaminationBean(id);
        if (!StringUtils.isEmpty(bean)) {
            isSave = true;
            setData();
            return;
        }

        YiXiuGeApi api = new YiXiuGeApi("app/examdetails");
        api.addParams("uid", api.getUserId(this)).addParams("id", id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<ExaminationBean>>() {
            @Override
            public void onResponse(DataBean<ExaminationBean> response) {
                if (isFinishing()) {
                    return;
                }
                bean = response.data;
                if (StringUtils.isEmpty(bean)) {
                    return;
                }
                setData();
            }
        });
    }

    private void setData() {
        list = bean.getExam();
        if (StringUtils.isEmpty(list)) {
            return;
        }
        number = list.size();
        if (!isSave) {
            Gson gson = new Gson();
            for (int i = 0; i < number; i++) {
                ExaminationBean.ExamBean examBean = list.get(i);
                String options = examBean.getOptions();
                Map<String, String> map = new TreeMap<>(
                        new Comparator<String>() {
                            public int compare(String obj1, String obj2) {
                                // 降序排序
                                return obj2.compareTo(obj1);
                            }
                        });
                map = gson.fromJson(options, map.getClass());
                examBean.setMap(map);
            }
            beanList = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                beanList.add(new ExaminationAnswerBean());
            }
        } else {
            beanList = bean.getBeanList();
        }
        setAnswerPosition(position);
        examinationAnswerAdapter.setData(beanList);
        recyclerViewAnswer.setAdapter(examinationAnswerAdapter);
        llBt.setVisibility(View.VISIBLE);
    }

    //
    private void setAnswerPosition(int position) {
        this.position = position;
        nextBt.setText((position + 1 == number) ? R.string.submit : R.string.next_topic);
        menuTv.setText(String.format(getString(R.string.examination_num), String.valueOf(position + 1), String.valueOf(number)));
        ExaminationBean.ExamBean examBean = list.get(position);
        examinationTopicAdapter.setData(examBean.getList());
        boolean model = StringUtils.isSame(examBean.getModel(), CommonUtils.isTwo);//2多选1单选
        titleTv.setText(String.format(getString(R.string.topic_title), String.valueOf((position + 1)), examBean.getTitle(), model ? getString(R.string.multiple_choice) : getString(R.string.single_choice)));
        examinationTopicAdapter.setMultiterm(model);//是否是多选题
        examinationTopicAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.on_a_bt, R.id.next_bt, R.id.back_iv})
    public void onViewClicked(View view) {
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        switch (view.getId()) {
            case R.id.on_a_bt:
                if (position == 0) {
                    break;
                }
                position -= 1;
                setAnswerPosition(position);
                break;
            case R.id.next_bt:
                if (position == number - 1) {
                    presenter.getAnswerList(list);
                    break;
                }
                position += 1;
                setAnswerPosition(position);//nextBt
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }


    @Override
    public void submit(List<String> list) {
        if (list.size() != number) {
            UIHelper.ToastMessage(this, "请答完所有的题目后再提交");
            return;
        }
//        for (String s:stringList){
//            L.d(L.TAG,s);
//        }
        YiXiuGeApi api = new YiXiuGeApi("app/examsub");
        api.addParams("uid", api.getUserId(this)).addParams("id", id)
                .addParams("result", JsonUtil.getJSONArrayByList(list));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getMsg());
                EventBus.getDefault().post(new ExaminationFinishEvent(examinationListBean.getType()));
                IntentUtil.startActivity(getContext(), ExaminationResultActivity.class, id);
                isSubmit = true;
                finish();
            }
        });
    }

    @Override
    public void setAnswer(String answer) {
        examinationAnswerAdapter.getData().get(position).setTopic(answer);
        examinationAnswerAdapter.notifyItemChanged(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isSubmit) {
            L.d(L.TAG, "saveExamination");
            bean.setExam(list);
            bean.setBeanList(beanList);
            YiXiuApp.getInstance().saveExamination(id, bean);
        } else {
            YiXiuApp.getInstance().removeExamination(id);
            L.d(L.TAG, "removeExamination");
        }
    }
}

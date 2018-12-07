package com.medui.yixiu.ui.home;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.medui.yixiu.R;
import com.medui.yixiu.adapter.HomeAdAdapter;
import com.medui.yixiu.adapter.HomeAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.AdBean;
import com.medui.yixiu.bean.CourseClassifyListBean;
import com.medui.yixiu.event.CourseOperationEvent;
import com.medui.yixiu.event.KaoQinEvent;
import com.medui.yixiu.event.SetUserEvent;
import com.medui.yixiu.event.UnreadEvent;
import com.medui.yixiu.ui.home.activity.ConversationListActivity;
import com.medui.yixiu.ui.home.activity.NewsSubActivity;
import com.medui.yixiu.ui.home.activity.TeacherActivity;
import com.medui.yixiu.ui.mine.activity.ExaminationActivity;
import com.medui.yixiu.ui.mine.activity.MyClassScheduleActivity;
import com.medui.yixiu.ui.scan.ScanActivity;
import com.medui.yixiu.ui.teacher.activity.ClassHourActivity;
import com.medui.yixiu.ui.teacher.activity.QuestionnaireManagementActivity;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by xkai on 2018/7/13.
 * 首页
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.kao_shi_tv)
    TextView kaoShiTv;//考试
    @InjectView(R.id.kaoqin_tv)
    TextView kaoQinTv;//考勤
    @InjectView(R.id.questionnaire_tv)
    TextView questionnaireTv;//问卷
    HomeAdapter mAdapter;

    @InjectView(R.id.banner)
    ConvenientBanner banner;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;//下拉刷新

    private HomeActionProvider provider;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setHasOptionsMenu(true);//在Fragment中加上这句菜单才能显示

        toolbar.setTitle(R.string.home);
        ((com.medui.yixiu.MainActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, ScanActivity.class);
            }
        });
        toolbar.setNavigationIcon(R.drawable.home_saoyisao);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.color1593f0, R.color.color33cea6, R.color.red, R.color.colorPrimary, R.color.color1593f0, R.color.black);

        mAdapter = new HomeAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        loadAd();//加载首页的轮播图
        loadHome();
        setUser();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_message, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.action_home_info);
        provider = (HomeActionProvider) MenuItemCompat.getActionProvider(menuItem);
        provider.setOnClickListener(new HomeActionProvider.OnClickListener() {
            @Override
            public void onClick() {
                if (!CommonUtils.isLogin(getContext())) {
                    return;
                }
//                IntentUtil.startActivity(context, MainActivity.class);
                IntentUtil.startActivity(context, ConversationListActivity.class);
            }
        });// 设置点击监听。
        if (!UserHelper.getInstance(context).hasLogin()) {
            provider.setCount(0);
        } else {
            provider.setCount(((com.medui.yixiu.MainActivity)getActivity()).getUnreadMsgCountTotal());
//            provider.setCount(102);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadHome() {
        YiXiuGeApi api = new YiXiuGeApi("app/video_index");
        api.addParams("uid", api.getUserId(context));
        api.addParams("recommend", 1);

        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<CourseClassifyListBean>>() {
            @Override
            public void onResponse(NoPageListBean<CourseClassifyListBean> response) {
                if (toolbar == null) {
                    return;
                }
                List<CourseClassifyListBean> list = response.data;
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    UIHelper.ToastMessage(context, error.getMessage());
                }

            }
        });
    }

    private void loadAd() {
        YiXiuGeApi api = new YiXiuGeApi("app/adpic");
        api.addParams("classid", 1);
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<NoPageListBean<AdBean>>() {
            @Override
            public void onResponse(NoPageListBean<AdBean> response) {
                if (toolbar == null) {
                    return;
                }
                List<AdBean> list = response.data;
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                banner.setPages(new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        return new HomeAdAdapter();
                    }
                }, list);
                banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
                banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                banner.startTurning(3000);
            }
        });
    }

    //教程详情点赞是调用
    @Subscribe
    public void courseOperationEvent(CourseOperationEvent event) {
        String id = event.getId();
        List<CourseClassifyListBean> list = mAdapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CourseClassifyListBean bean = list.get(i);
            if (StringUtils.isSame(id, bean.getId())) {
                bean.setLiked(event.getLiked());
                bean.setView(event.getViewNum());
                bean.setFavoured(event.getFavoured());
                bean.setReviews(event.getReviews());
                bean.setLike(event.getLike());
                bean.setFavour(event.getFavour());
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    //
    @Subscribe
    public void setUserEvent(SetUserEvent event) {
        setUser();
    }

    private void setUser() {
        boolean isStudent = CommonUtils.isStudent(context);
//        kaoShiTv.setVisibility(isStudent ? View.VISIBLE : View.GONE);
//        kaoQinTv.setVisibility(isStudent ? View.VISIBLE : View.GONE);
//        questionnaireTv.setVisibility(isStudent ? View.VISIBLE : View.GONE);
    }

    //
    @Subscribe (sticky = true)
    public void unreadEvent(UnreadEvent event){
        if (provider != null){
            provider.setCount(event.getCount());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        loadHome();
    }


    @OnClick({R.id.ke_cheng_tv, R.id.zi_xun_tv, R.id.jiao_cheng_tv, R.id.kao_shi_tv, R.id.questionnaire_tv, R.id.kaoqin_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ke_cheng_tv://课程
                if (CommonUtils.isStudent(context)) {//学生是跳到课程表，老师跳到我的课时
                    IntentUtil.startActivity(context, MyClassScheduleActivity.class);
                } else {
                    IntentUtil.startActivity(context, ClassHourActivity.class);
                }
                break;
            case R.id.zi_xun_tv://资讯
                IntentUtil.startActivity(context, NewsSubActivity.class);
                break;
            case R.id.jiao_cheng_tv://教师
                IntentUtil.startActivity(context, TeacherActivity.class);
                break;
            case R.id.kao_shi_tv://考试
                IntentUtil.startActivity(context, ExaminationActivity.class);
                break;
            case R.id.questionnaire_tv://问卷
//                IntentUtil.startActivity(context, QuestionnaireActivity.class);
                IntentUtil.startActivity(context, QuestionnaireManagementActivity.class);
                break;
            case R.id.kaoqin_tv://考勤
                EventBus.getDefault().post(new KaoQinEvent());
                break;
        }
    }

}

package com.lanmei.yixiu;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.yixiu.adapter.MainPagerAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.AddCourseEvent;
import com.lanmei.yixiu.event.KaoQinEvent;
import com.lanmei.yixiu.event.LogoutEvent;
import com.lanmei.yixiu.helper.TabHelper;
import com.lanmei.yixiu.update.UpdateAppConfig;
import com.lanmei.yixiu.utils.AKDialog;
import com.lanmei.yixiu.utils.BaiduLocation;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    private ProgressHUD mProgressHUD;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        new TabHelper(this, mTabLayout);

        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

//        mViewPager.setNoScroll(true);
        EventBus.getDefault().register(this);
        CommonUtils.loadUserInfo(this, null);
        UpdateAppConfig.requestStoragePermission(this);

        initPermission();//百度定位权限
    }

    private boolean initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //点击考勤的时候调用
    @Subscribe
    public void kaoQinEvent(KaoQinEvent event) {
        if (!initPermission()) {
            return;
        }
        mProgressHUD = ProgressHUD.show(this, getString(R.string.in_attendance), true, true, null);
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (isFinishing()) {
                    return;
                }
                if (location != null) {
                    loadLocation(location);
                } else {
                    notice("定位失败，请确认网络和GPS是否开启");
                }
                locationClient.stop();
                locationClient = null;
            }
        });
    }


    private void loadLocation(BDLocation location) {
        YiXiuGeApi api = new YiXiuGeApi("app/sign");
        api.addParams("uid", api.getUserId(this)).addParams("lat", location.getLatitude()).addParams("lon", location.getLongitude());
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                notice(response.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                notice(error.getMessage());
            }
        });
    }


    private void dismiss() {
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }

    private void notice(String info) {
        dismiss();
        AKDialog.getMessageDialog(this, "", info, R.string.i_know, null).show();
    }

    //退出登录时调用
    @Subscribe
    public void logoutEvent(LogoutEvent event) {
        finish();
    }

    //添加视频教程后调用
    @Subscribe
    public void AddCourseEvent(AddCourseEvent event) {
        mViewPager.setCurrentItem(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

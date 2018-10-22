package com.lanmei.yixiu.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.sdk.android.oss.common.utils.OssUserInfo;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.YiXiuApp;
import com.lanmei.yixiu.adapter.BannerHolderView;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.SetUserEvent;
import com.lanmei.yixiu.ui.login.LoginActivity;
import com.lanmei.yixiu.webviewpage.PhotoBrowserActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import oss.ManageOssUpload;

public class CommonUtils {

    public final static String isZero = "0";
    public final static String isOne = "1";
    public final static String isTwo = "2";
    public final static String isThree = "3";
    public static final String HX_USER_HEAD = "b_";

    public static int quantity = 3;


    /**
     * 获取TextView 字符串
     *
     * @param textView
     * @return
     */
    public static String getStringByTextView(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 获取EditText 字符串
     *
     * @param editText
     * @return
     */
    public static String getStringByEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isLogin(Context context) {
        if (!UserHelper.getInstance(context).hasLogin()) {
            IntentUtil.startActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }

    public static void developing(Context context) {
        UIHelper.ToastMessage(context, R.string.developing);
    }


    /**
     * String 逗号隔开 0,2,3
     *
     * @param list
     * @return
     */
    public static String getStringByList(List<String> list) {
        if (StringUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            builder.append(((size - 1) != i) ? list.get(i) + "," : list.get(i));
        }
        return builder.toString();
    }

    /**
     * String 逗号隔开 0,2,3
     *
     * @param list
     * @return
     */
    public static JSONArray getJSONArrayByList(List<String> list) {
        JSONArray array = new JSONArray();
        if (StringUtils.isEmpty(list)) {
            return array;
        }
        for (String s : list) {
            array.add(s);
        }
        return array;
    }
    /**
     *
     * @param list
     * @return
     */
    public static String[] toArray(List<String> list) {
        return list.toArray(new String[StringUtils.isEmpty(list)?0:list.size()]);
    }

    /**
     * 获取年月日
     */
    public static String getData() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }

    public static String getUserId(Context context) {
        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (StringUtils.isEmpty(bean)) {
            return "";
        }
        return bean.getId();
    }

    //获取 用户类型
    public static String getUserType(Context context) {
        UserBean bean = UserHelper.getInstance(context).getUserBean();
        if (StringUtils.isEmpty(bean)) {
            return "";
        }
        return bean.getUser_type();
    }

    //获取 用户类型 true 学生 false 老师
    public static boolean isStudent(Context context) {
        return !StringUtils.isSame(isOne,getUserType(context));
    }

    public static UserBean getUserBean(Context context) {
        return UserHelper.getInstance(context).getUserBean();
    }

    /**
     * @param banner
     * @param list
     * @param isTurning 防止重复  banner.startTurning(3000);
     */
    public static void setBanner(ConvenientBanner banner, List<String> list, boolean isTurning) {
        //初始化商品图片轮播
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolderView();
            }
        }, list);
        banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        if (list.size() == 1) {
            return;
        }
        if (!isTurning) {
            return;
        }
        banner.startTurning(3000);
    }


    //获取用户信息
    public static void loadUserInfo(final Context context, final UserInfoListener l) {
        YiXiuGeApi api = new YiXiuGeApi("app/getuser");
        api.addParams("uid", api.getUserId(context));
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<DataBean<UserBean>>() {
            @Override
            public void onResponse(DataBean<UserBean> response) {
                if (context == null) {
                    return;
                }
                UserBean bean = response.data;
                if (bean != null) {
                    if (l != null) {
                        l.userInfo(bean);
                    }
                    UserHelper.getInstance(context).saveBean(bean);
                    EventBus.getDefault().post(new SetUserEvent());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (l != null) {
                    l.error(error.getMessage());
                }
            }
        });
    }


    public interface UserInfoListener {
        void userInfo(UserBean bean);

        void error(String error);
    }


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (StringUtils.isEmpty(cardId) || cardId.startsWith(isZero)) {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';//如果传的不是数据返回N
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 浏览图片
     *
     * @param context
     * @param arry     图片地址数组
     * @param imageUrl 点击的图片地址
     */
    public static void showPhotoBrowserActivity(Context context, List<String> arry, String imageUrl) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", (Serializable) arry);
        intent.putExtra("curImageUrl", imageUrl);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }

    public static String getFileName(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        return url.substring(url.lastIndexOf("/") + 1, url.length());

    }

    public static String getObjectKey(String url) {
        if (StringUtils.isEmpty(url) || !url.contains(OssUserInfo.endpoint)) {
            return "";
        }
        return url.substring(url.indexOf("/", 35) + 1, url.length());
    }

    /**
     * 删除OSS文件
     *
     * @param url
     */
    public static void deleteOssObject(String url) {
        String objectKey = getObjectKey(url);
        if (StringUtils.isEmpty(objectKey)) {
            return;
        }
        ManageOssUpload manageOssUpload = new ManageOssUpload(YiXiuApp.applicationContext);
        manageOssUpload.deleteObject(new DeleteObjectRequest(OssUserInfo.testBucket, objectKey));
        manageOssUpload.logAyncListObjects();
    }

    /**
     * 删除OSS文件(批量)
     *
     * @param paths
     */
    public static void deleteOssObjectList(final List<String> paths) {
        if (StringUtils.isEmpty(paths)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ManageOssUpload manageOssUpload = new ManageOssUpload(YiXiuApp.applicationContext);
                manageOssUpload.logAyncListObjects();
                int size = paths.size();
                for (int i = 0; i < size; i++) {
                    String objectKey = getObjectKey(paths.get(i));
                    if (!StringUtils.isEmpty(objectKey)) {
                        manageOssUpload.deleteObject(new DeleteObjectRequest(OssUserInfo.testBucket, objectKey));
                    }
                }
                manageOssUpload.logAyncListObjects();
            }
        }).start();
    }

    /**
     * 设置TabLayout tab 左右边距
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setTabLayoutIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

}

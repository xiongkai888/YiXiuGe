package com.xson.common.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.umeng.analytics.MobclickAgent;
import com.xson.common.R;
import com.xson.common.utils.L;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean isAttached;
    private ArrayList<DispatchTouchEventListener> dispatchTouchEventListeners;

    /**
     * @return contentViewId   布局Id
     */
    public abstract int getContentViewId();

    /**
     * @param savedInstanceState onCreate()中的参数,Bundle类型
     */
    protected abstract void initAllMembersView(Bundle savedInstanceState);

    public interface DispatchTouchEventListener {
        void onDispatchTouchEvent(MotionEvent event);
    }

    public void initIntent(Intent intent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ((BaseApp)getApplication()).watch(this);
        fixInputMethodManagerLeak(this);
        ButterKnife.reset(this);
    }

    public void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        String [] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field filed;
        Object filedObject;

        for (String view:viewArray) {
            try{
                filed = inputMethodManager.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(inputMethodManager);
                if (filedObject != null && filedObject instanceof View) {
                    View fileView = (View) filedObject;
                    if (fileView.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        filed.set(inputMethodManager, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initIntent(getIntent());
        ButterKnife.inject(this, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.white_selectable_bg));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initAllMembersView(savedInstanceState);
    }


    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public boolean isDetached() {
        return !isAttached;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEventListeners != null) {
            for (DispatchTouchEventListener l : dispatchTouchEventListeners) {
                l.onDispatchTouchEvent(ev);
            }
        }
        //fix PhotoViewAttacher bug
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            L.e(e);
            return true;
        }
    }

    public void addDispatchTouchEventListener(DispatchTouchEventListener l) {
        if (dispatchTouchEventListeners == null)
            dispatchTouchEventListeners = new ArrayList<>();
        dispatchTouchEventListeners.add(l);
    }

    public void removeDispatchTouchEventListener(DispatchTouchEventListener l) {
        if (dispatchTouchEventListeners == null)
            return;
        dispatchTouchEventListeners.remove(l);
    }

}

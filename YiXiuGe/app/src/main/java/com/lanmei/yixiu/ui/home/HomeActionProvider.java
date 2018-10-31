package com.lanmei.yixiu.ui.home;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;


/**
 * Created by xka on 2018/10/31.
 */

public class HomeActionProvider extends ActionProvider {

    private ImageView mIvIcon;
    private TextView mTvBadge;

    @Override
    public View onCreateActionView() {
        int size = getContext().getResources().getDimensionPixelSize(
                android.support.design.R.dimen.abc_action_bar_default_height_material);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.menu_home_badge_provider, null, false);

        view.setLayoutParams(layoutParams);
        mIvIcon = (ImageView) view.findViewById(R.id.iv_icon);
        mTvBadge = (TextView) view.findViewById(R.id.unread_msg_number);
        setBadge();
        view.setOnClickListener(onViewClickListener);
        return view;
    }


    // 点击处理。
    private View.OnClickListener onViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onClickListener != null)
                onClickListener.onClick();
        }
    };

    OnClickListener onClickListener;

    // 外部设置监听。
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick();
    }

    public HomeActionProvider(Context context) {
        super(context);
    }

    // 设置图标。
    public void setIcon(@DrawableRes int icon) {
        mIvIcon.setImageResource(icon);
    }

    // 设置显示的数字。
    public void setBadge(int i) {
        mTvBadge.setText(Integer.toString(i));
    }

    // 设置文字。
    public void setTextInt(@StringRes int i) {
        mTvBadge.setText(i);
    }

    // 设置显示的文字。
    public void setText(CharSequence i) {
        mTvBadge.setText(i);
    }
    int count;
    //设置商品的数量
    public void setCount(int count) {
        this.count = count;
        setBadge();
    }

    public void setBadge(){
        if (mTvBadge != null){
            if (count == 0){
                mTvBadge.setVisibility(View.GONE);
            }else{
                mTvBadge.setVisibility(View.VISIBLE);
                mTvBadge.setText(String.valueOf(count));
            }
        }
    }

}

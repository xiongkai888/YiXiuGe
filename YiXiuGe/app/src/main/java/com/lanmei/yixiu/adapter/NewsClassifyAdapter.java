package com.lanmei.yixiu.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.yixiu.bean.NewsClassifyBean;
import com.lanmei.yixiu.ui.home.fragment.NewsClassifyListFragment;

import java.util.List;


/**
 * 资讯分类
 */
public class NewsClassifyAdapter extends FragmentPagerAdapter {

    private List<NewsClassifyBean> list;

    public void setList(List<NewsClassifyBean> list) {
        this.list = list;
    }

    public NewsClassifyAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        NewsClassifyListFragment fragment = new NewsClassifyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",list.get(position).getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return com.xson.common.utils.StringUtils.isEmpty(list)?0:list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return list.get(position).getName();
    }


}

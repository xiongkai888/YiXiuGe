package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.ClassSelectBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 班级选择
 */
public class ClassSelectAdapter extends SwipeRefreshAdapter<ClassSelectBean> {

    public ClassSelectAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_class_select, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ClassSelectBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.grade_name_tv)
        TextView gradeNameTv;
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(ClassSelectBean bean) {
            gradeNameTv.setText(bean.getName());
            ClassSelectSubAdapter adapter = new ClassSelectSubAdapter(context);
            adapter.setData(bean.getXiaji());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
        }
    }

}

package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.SelectQuestionStudentsBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 选择问卷学生
 */
public class SelectQuestionStudentsAdapter extends SwipeRefreshAdapter<SelectQuestionStudentsBean> {

    public SelectQuestionStudentsAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_qusetion_students, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final SelectQuestionStudentsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bean.setSelect(!bean.isSelect());
//                notifyDataSetChanged();
//            }
//        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.select_iv)
        ImageView selectIv;
        @InjectView(R.id.ll_unfold)
        LinearLayout llUnfold;
        @InjectView(R.id.recyclerView)
        RecyclerView recyclerView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final SelectQuestionStudentsBean bean) {
//            selectIv.setImageResource(bean.isSelect()?R.drawable.pay_on :R.drawable.pay_off);
            nameTv.setText(bean.getParent_name()+bean.getName());
            final SelectQuestionStudentsSubAdapter adapter = new SelectQuestionStudentsSubAdapter(context);
            recyclerView.setNestedScrollingEnabled(false);
            adapter.setData(bean.getStudent());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            adapter.setAllSelectListener(new SelectQuestionStudentsSubAdapter.AllSelectListener() {
                @Override
                public void setAll(boolean isAll) {
                    selectIv.setImageResource(isAll?R.drawable.pay_on:R.drawable.pay_off);
                    bean.setAll(isAll);
                }
            });
            selectIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<SelectQuestionStudentsBean.StudentBean> list = bean.getStudent();
                    if (com.xson.common.utils.StringUtils.isEmpty(list)){
                        return;
                    }
                    boolean isAll = !bean.isAll();
                    for (SelectQuestionStudentsBean.StudentBean studentBean:list){
                        studentBean.setSelect(isAll);
                    }
                    bean.setAll(isAll);
                    selectIv.setImageResource(isAll?R.drawable.pay_on:R.drawable.pay_off);
                    adapter.notifyDataSetChanged();
                }
            });
            llUnfold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isUnfold = bean.isUnfold();
                    recyclerView.setVisibility(isUnfold?View.GONE:View.VISIBLE);
                    bean.setUnfold(!isUnfold);
                }
            });
        }
    }

}

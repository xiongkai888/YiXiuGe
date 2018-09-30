package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.ClassSelectBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 班级选择(sub)
 */
public class ClassSelectSubAdapter extends SwipeRefreshAdapter<ClassSelectBean.XiajiBean> {

    public ClassSelectSubAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_class_select_sub, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ClassSelectBean.XiajiBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.choose_iv)
        ImageView chooseIv;
        @InjectView(R.id.class_name_tv)
        TextView classNameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final ClassSelectBean.XiajiBean bean) {
            chooseIv.setImageResource(bean.isChoose()?R.drawable.pay_on :R.drawable.pay_off);
            classNameTv.setText(bean.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setChoose(!bean.isChoose());
                    notifyDataSetChanged();
                }
            });
        }
    }

}

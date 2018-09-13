package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.ClassSelectBean;
import com.lanmei.yixiu.event.ClassSelectEvent;
import com.xson.common.adapter.SwipeRefreshAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 班级选择(sub)
 */
public class ClassSelectSubAdapter extends SwipeRefreshAdapter<ClassSelectBean.XiajiBean> {

    private int fatherPosition;

    public ClassSelectSubAdapter(Context context,int fatherPosition) {
        super(context);
        this.fatherPosition = fatherPosition;
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
        viewHolder.setParameter(bean,position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.class_name_tv)
        TextView classNameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(ClassSelectBean.XiajiBean bean,final int position) {
            classNameTv.setText(bean.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ClassSelectEvent(fatherPosition,position));
                }
            });
        }
    }

}

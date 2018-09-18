package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.TeacherFiltrateBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 老师(下拉筛选)
 */
public class TeacherFiltrateAdapter extends SwipeRefreshAdapter<TeacherFiltrateBean> {


    public TeacherFiltrateAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teacher_filtrate, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final TeacherFiltrateBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isSelect()){
                    return;
                }
                List<TeacherFiltrateBean> list = getData();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    TeacherFiltrateBean examinationTopicBean = list.get(i);
                    if (examinationTopicBean.isSelect()) {
                        examinationTopicBean.setSelect(false);
                    }
                }
                bean.setSelect(!bean.isSelect());
                notifyDataSetChanged();
                if (teacherFiltrateListener != null){
                    teacherFiltrateListener.onFiltrate(bean);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(TeacherFiltrateBean bean) {
            nameTv.setTextColor(bean.isSelect()?context.getResources().getColor(R.color.color1593f0):context.getResources().getColor(R.color.color666));
            nameTv.setText(bean.getName());
        }
    }

    private TeacherFiltrateListener teacherFiltrateListener;

    public void setTeacherFiltrateListener(TeacherFiltrateListener teacherFiltrateListener){
        this.teacherFiltrateListener = teacherFiltrateListener;
    }

    public interface TeacherFiltrateListener{
        void onFiltrate(TeacherFiltrateBean bean);
    }

}

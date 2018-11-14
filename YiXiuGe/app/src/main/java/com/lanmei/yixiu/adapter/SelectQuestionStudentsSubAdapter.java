package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class SelectQuestionStudentsSubAdapter extends SwipeRefreshAdapter<SelectQuestionStudentsBean.StudentBean> {

    public SelectQuestionStudentsSubAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_students, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final SelectQuestionStudentsBean.StudentBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setSelect(!bean.isSelect());
                notifyDataSetChanged();
                if (listener != null) {
                    if (!bean.isSelect()){
                        listener.setAll(bean.isSelect());
                    }else {
                        listener.setAll(isAll(getData()));
                    }
                }
            }
        });
    }

    private boolean isAll(List<SelectQuestionStudentsBean.StudentBean> list){
        for (SelectQuestionStudentsBean.StudentBean bean:list){
            if (!bean.isSelect()){
                return false;
            }
        }
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.select_iv)
        ImageView selectIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final SelectQuestionStudentsBean.StudentBean bean) {
            selectIv.setImageResource(bean.isSelect() ? R.drawable.pay_on : R.drawable.pay_off);
            nameTv.setText(bean.getRealname());

        }
    }

    public AllSelectListener listener;

    public interface AllSelectListener {
        void setAll(boolean isAll);
    }

    public void setAllSelectListener(AllSelectListener listener) {
        this.listener = listener;
    }

}

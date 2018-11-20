package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.SelectStudentsBean;
import com.medui.yixiu.ui.teacher.presenter.SelectStudentsContract;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 选择考试学生
 */
public class SelectStudentsAdapter extends SwipeRefreshAdapter<SelectStudentsBean> {

    private SelectStudentsContract.Presenter presenter;

    public SelectStudentsAdapter(Context context) {
        super(context);
    }

    public void setPresenter(SelectStudentsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_students, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final SelectStudentsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setSelect(!bean.isSelect());
                presenter.onClickSingleItem();
                notifyDataSetChanged();
            }
        });
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


        public void setParameter(final SelectStudentsBean bean) {
            selectIv.setImageResource(bean.isSelect()?R.drawable.pay_on :R.drawable.pay_off);

            nameTv.setText(bean.getName());

        }
    }

}

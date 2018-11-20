package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.NotesBean;
import com.medui.yixiu.ui.mine.activity.NoteDetailsActivity;
import com.medui.yixiu.utils.CommonUtils;
import com.medui.yixiu.utils.FormatTime;
import com.medui.yixiu.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的笔记
 */
public class MyNoteAdapter extends SwipeRefreshAdapter<NotesBean> {

    private FormatTime formatTime;

    public MyNoteAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        NotesBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;
        @InjectView(R.id.title_et)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final NotesBean bean) {
            titleTv.setText(bean.getTitle());
            formatTime.setTime(bean.getAddtime());
            timeTv.setText(formatTime.formatterTime());
            sudokuView.setListData(bean.getPic());
            sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
                @Override
                public void onClick(int positionSub) {
//                    UIHelper.ToastMessage(context,""+positionSub);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    bundle.putString("type",CommonUtils.isOne);
                    IntentUtil.startActivity(context, NoteDetailsActivity.class,bundle);
                }

                @Override
                public void onDoubleTap(int position) {
                    CommonUtils.showPhotoBrowserActivity(context, bean.getPic(), bean.getPic().get(position));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    bundle.putString("type",CommonUtils.isOne);
                    IntentUtil.startActivity(context, NoteDetailsActivity.class,bundle);
                }
            });
        }
    }

}

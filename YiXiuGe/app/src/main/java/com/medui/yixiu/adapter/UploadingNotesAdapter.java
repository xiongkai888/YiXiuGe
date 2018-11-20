package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.NotesBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 发笔记上传附件
 */
public class UploadingNotesAdapter extends SwipeRefreshAdapter<NotesBean.EnclosureBean> {

    public UploadingNotesAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_uploading_notes, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final NotesBean.EnclosureBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mCrossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData().remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        viewHolder.nameTv.setText(bean.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView mPicIv;
        @InjectView(R.id.cross_iv)
        ImageView mCrossIv;//删除图标
        @InjectView(R.id.name_tv)
        TextView nameTv;//

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}

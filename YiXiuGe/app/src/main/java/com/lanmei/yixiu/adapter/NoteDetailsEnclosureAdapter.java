package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.ui.mine.activity.ShowEnclosureActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 笔记详情附件列表
 */
public class NoteDetailsEnclosureAdapter extends SwipeRefreshAdapter<NotesBean.EnclosureBean> {

    public NoteDetailsEnclosureAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note_details_enclosure, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final NotesBean.EnclosureBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameTv.setText(bean.getName());
        viewHolder.checkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, ShowEnclosureActivity.class,bean.getUrl());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.check_tv)
        TextView checkTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}

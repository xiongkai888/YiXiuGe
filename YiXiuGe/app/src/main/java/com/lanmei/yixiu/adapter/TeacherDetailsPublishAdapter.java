package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanmei.yixiu.R;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 老师详情 发布列表
 */
public class TeacherDetailsPublishAdapter extends SwipeRefreshAdapter<String> {

    public TeacherDetailsPublishAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_teacher_details_publish, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        String url = getItem(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(url);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.image)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(String url) {
            ImageHelper.load(context,url,imageView,null,true,R.drawable.default_pic,R.drawable.default_pic);
        }
    }

}

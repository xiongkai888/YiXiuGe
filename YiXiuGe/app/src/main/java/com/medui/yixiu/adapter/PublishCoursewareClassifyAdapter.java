package com.medui.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.CourseClassifyBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 发布教程课时分类（多选）
 */
public class PublishCoursewareClassifyAdapter extends SwipeRefreshAdapter<CourseClassifyBean> {


    public PublishCoursewareClassifyAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_course_classify, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final CourseClassifyBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setChoose(!bean.isChoose());
                if (l != null){
                    l.chooseCourseClassify(getData());
                }
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.choose_iv)
        ImageView chooseIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(CourseClassifyBean bean) {
            nameTv.setText(bean.getName());
            chooseIv.setImageResource(bean.isChoose()?R.drawable.pay_on :R.drawable.pay_off);
        }
    }

    ChooseCoursewareClassifyListener l;


    public void setChooseCoursewareClassifyListener(ChooseCoursewareClassifyListener l){
        this.l = l;
    }

    public interface ChooseCoursewareClassifyListener{
        void chooseCourseClassify(List<CourseClassifyBean> list);
    }

}

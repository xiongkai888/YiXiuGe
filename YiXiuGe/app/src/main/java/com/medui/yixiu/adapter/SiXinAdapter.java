package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.SiXinBean;
import com.medui.yixiu.ui.mine.activity.ClassDetailsActivity;
import com.medui.yixiu.ui.mine.activity.ExaminationActivity;
import com.medui.yixiu.ui.teacher.activity.QuestionnaireManagementActivity;
import com.medui.yixiu.utils.FormatTime;
import com.othershe.calendarview.bean.DateBean;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 私信
 */
public class SiXinAdapter extends SwipeRefreshAdapter<SiXinBean> {

    FormatTime time;

    public SiXinAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_sixin, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        SiXinBean bean = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ll_content)
        LinearLayout llContent;
        @InjectView(R.id.title_et)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.delete_tv)
        TextView deleteTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final SiXinBean bean) {
            titleTv.setText(bean.getTitle());
            contentTv.setText(bean.getIntro());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.formatterTime());
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.delete(bean.getId(),getAdapterPosition());
                    }
                }
            });
            llContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getType()){
                        case "1":
                            time.setTime(bean.getStarttime());
                            DateBean date = new DateBean();
                            date.setSolar(time.getYear(),time.getMonth(),time.getDay());

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean", date);
                            IntentUtil.startActivity(context, ClassDetailsActivity.class, bundle);
                            break;
                        case "2":
                            IntentUtil.startActivity(context, ExaminationActivity.class);
                            break;
                        case "3":
                            IntentUtil.startActivity(context, QuestionnaireManagementActivity.class);
                            break;
                    }
                }
            });
        }
    }

    DeleteSiXinListener listener;

    public interface DeleteSiXinListener{
        void delete(String id,int position);
    }

    public void setDeleteSiXinListener(DeleteSiXinListener listener){
        this.listener = listener;
    }

}

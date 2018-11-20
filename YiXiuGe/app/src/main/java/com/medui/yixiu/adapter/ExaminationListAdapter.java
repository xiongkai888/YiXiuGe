package com.medui.yixiu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.bean.ExaminationListBean;
import com.medui.yixiu.ui.mine.activity.Examination1Activity;
import com.medui.yixiu.ui.mine.activity.ExaminationResultActivity;
import com.medui.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 随堂测试
 */
public class ExaminationListAdapter extends SwipeRefreshAdapter<ExaminationListBean> {


    FormatTime time;

    public ExaminationListAdapter(Context context) {
        super(context);
        time = new FormatTime(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_examination_sub, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ExaminationListBean bean = getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.status_tv)
        TextView statusTv;
        @InjectView(R.id.enter_examination_tv)
        TextView enterExaminationTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final ExaminationListBean bean) {
            enterExaminationTv.setVisibility(View.GONE);
            titleTv.setText(bean.getTitle());
            int status = bean.getStatus();
            switch (status) {
                case 1:
                    statusTv.setText(R.string.not_started);
                    break;
                case 2:
                    statusTv.setText(R.string.underway);
                    enterExaminationTv.setText(R.string.enter_into_examination);
                    enterExaminationTv.setVisibility(View.VISIBLE);
                    enterExaminationTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean",bean);
                            IntentUtil.startActivity(context, Examination1Activity.class,bundle);
                        }
                    });
                    break;
                case 3:
                    statusTv.setText(R.string.finished);
                    break;
                case 4:
                    statusTv.setText(R.string.submitted);
                    enterExaminationTv.setVisibility(View.VISIBLE);
                    enterExaminationTv.setText(R.string.achievements);
                    enterExaminationTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("bean",bean);
//                            IntentUtil.startActivity(context, Examination1Activity.class,bundle);
                            IntentUtil.startActivity(context, ExaminationResultActivity.class,bean.getId());
                        }
                    });
                    break;
            }
            timeTv.setText(String.format(context.getString(R.string.start_and_time), time.formatterTime(bean.getStarttime()), time.formatterTime(bean.getEndtime())));
        }
    }

}

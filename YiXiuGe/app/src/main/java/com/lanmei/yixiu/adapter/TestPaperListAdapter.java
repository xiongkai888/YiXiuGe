package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.TestPaperBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的试卷列表
 */
public class TestPaperListAdapter extends SwipeRefreshAdapter<TestPaperBean> {

    private FormatTime formatTime;

    public TestPaperListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_test_paper, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        TestPaperBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.type_iv)
        ImageView typeIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.status_tv)
        TextView statusTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(TestPaperBean bean) {
            titleTv.setText(bean.getTitle());
            timeTv.setText(String.format(context.getString(R.string.start_and_time),formatTime.formatterTime(bean.getStarttime()),formatTime.formatterTime(bean.getEndtime())));
            switch (bean.getStatus()){
                case 1:
                    statusTv.setText(R.string.not_started);
                    break;
                case 2:
                    statusTv.setText(R.string.underway);
                    break;
                case 3:
                    statusTv.setText(R.string.finished);
                    break;
            }
            typeIv.setImageResource(StringUtils.isSame(CommonUtils.isOne,bean.getType())?R.drawable.temp_teacher_sui:R.drawable.temp_teacher_z);
        }
    }

}

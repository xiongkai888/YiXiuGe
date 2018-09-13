package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.ClassHourBean;
import com.lanmei.yixiu.ui.teacher.activity.CheckingInManageActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的课时（教师端）
 */
public class ClassHourListAdapter extends SwipeRefreshAdapter<ClassHourBean> {

    private FormatTime formatTime;

    public ClassHourListAdapter(Context context) {
        super(context);
        formatTime = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_class_hour_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ClassHourBean bean  = getItem(position);
        if (bean == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.kec_tv)
        TextView kecTv;
        @InjectView(R.id.class_name_tv)
        TextView classNameTv;
        @InjectView(R.id.num_tv)
        TextView numTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.roomname_tv)
        TextView roomnameTv;
        @InjectView(R.id.remark_tv)
        TextView remarkTv;
        @InjectView(R.id.state_tv)
        TextView stateTv;
        @InjectView(R.id.checking_in_manage_tv)
        TextView checkingInManageTv;//考勤管理

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final ClassHourBean bean) {
            kecTv.setText(bean.getKec());
            classNameTv.setText(bean.getClassname());
            numTv.setText(String.format(context.getString(R.string.num_person),bean.getNum()));
            roomnameTv.setText(bean.getRoomname());
            remarkTv.setText(bean.getRemark());
            formatTime.setTime(bean.getStart_time());
            String startTime = formatTime.formatterTimeNoSeconds();
            formatTime.setTime(bean.getEnd_time());
            String endTime = formatTime.formatterTimeNoSeconds();
            timeTv.setText(String.format(context.getString(R.string.start_end_time),startTime,endTime));
            stateTv.setText(StringUtils.isSame(bean.getState(), CommonUtils.isZero)?context.getString(R.string.not_check):context.getString(R.string.checked));
            checkingInManageTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.startActivity(context, CheckingInManageActivity.class,bean.getId());
                }
            });
        }
    }

}

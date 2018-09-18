package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.ClassDetailsBean;
import com.lanmei.yixiu.utils.AKDialog;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.othershe.calendarview.bean.CalendarEvent;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.utils.SolarUtil;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 课程详情
 */
public class ClassDetailsAdapter extends SwipeRefreshAdapter<ClassDetailsBean> {

    private FormatTime formatTime;
    private int currentPosition;
    private DateBean date;

    public ClassDetailsAdapter(Context context, DateBean date,int currentPosition) {
        super(context);
        formatTime = new FormatTime();
        this.date = date;
        this.currentPosition = currentPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_class_details, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ClassDetailsBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.class_hour_evaluate_tv)
        TextView classHourEvaluateTv;
        @InjectView(R.id.teacher_evaluate_tv)
        TextView teacherEvaluateTv;
        @InjectView(R.id.schoolmate_evaluate_tv)
        TextView schoolmateEvaluateTv;

        @InjectView(R.id.pic_iv)
        CircleImageView picIv;
        @InjectView(R.id.realname_tv)
        TextView realnameTv;
        @InjectView(R.id.kec_tv)
        TextView kecTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.roomname_tv)
        TextView roomnameTv;
        @InjectView(R.id.remark_tv)
        TextView remarkTv;

        ClassDetailsBean bean;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick({R.id.class_hour_evaluate_tv, R.id.teacher_evaluate_tv, R.id.schoolmate_evaluate_tv})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.class_hour_evaluate_tv:
                    showEvaluateDialog(CommonUtils.getStringByTextView(classHourEvaluateTv),1);
                    break;
                case R.id.teacher_evaluate_tv:
                    showEvaluateDialog(CommonUtils.getStringByTextView(teacherEvaluateTv),2);
                    break;
                case R.id.schoolmate_evaluate_tv:
                    showEvaluateDialog(CommonUtils.getStringByTextView(schoolmateEvaluateTv),3);
                    break;
            }
        }


        private void showEvaluateDialog(String who,final int type) {
            AKDialog.evaluateDialog(context, who, new AKDialog.EvaluateListener() {
                @Override
                public void evaluate(String content, int rating) {
                    loadComments(content,rating,type,bean.getId(),bean);
                }
            });
        }

        public void setParameter(ClassDetailsBean bean) {
            this.bean = bean;
            ImageHelper.load(context,bean.getPic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
            realnameTv.setText(bean.getRealname());
            kecTv.setText(bean.getKec());

            formatTime.setTime(bean.getStart_time());
            String startTime = formatTime.formatterTime();
            formatTime.setTime(bean.getEnd_time());
            String endTime = formatTime.formatterTime();
            timeTv.setText(String.format(context.getString(R.string.start_end_time), startTime, endTime));

            roomnameTv.setText(bean.getRoomname());
            remarkTv.setText(bean.getRemark());

            classHourEvaluateTv.setVisibility(StringUtils.isSame(bean.getKeshi(),CommonUtils.isZero)?View.VISIBLE:View.GONE);
            teacherEvaluateTv.setVisibility(StringUtils.isSame(bean.getLaoshi(),CommonUtils.isZero)?View.VISIBLE:View.GONE);
            schoolmateEvaluateTv.setVisibility(StringUtils.isSame(bean.getTongxue(),CommonUtils.isZero)?View.VISIBLE:View.GONE);

        }
    }

    //评论
    private void loadComments(String content, int rating,final int type,String pid,final ClassDetailsBean bean){
        YiXiuGeApi api = new YiXiuGeApi("app/add_comment");
        api.addParams("content",content);
        api.addParams("grade",rating);
        api.addParams("uid",api.getUserId(context));
        api.addParams("pid",pid);
        api.addParams("type",type);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (formatTime == null){
                    return;
                }
                UIHelper.ToastMessage(context,response.getMsg());
                if (type == 1){
                    bean.setKeshi(CommonUtils.isOne);
                }else if (type == 2){
                    bean.setLaoshi(CommonUtils.isOne);
                }else {
                    bean.setTongxue(CommonUtils.isOne);
                }
                notifyDataSetChanged();
                int year = date.getSolar()[0];
                int month = date.getSolar()[1];
                int monthDays = SolarUtil.getMonthDays(year, month);
                EventBus.getDefault().post(new CalendarEvent(year,month,monthDays,currentPosition));
//                L.d("AyncListObjects",year+"-"+month+"-"+monthDays+"天"+currentPosition);
            }
        });
    }

}

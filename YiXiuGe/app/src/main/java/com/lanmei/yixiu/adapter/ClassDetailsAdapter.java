package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.utils.AKDialog;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 课程详情
 */
public class ClassDetailsAdapter extends SwipeRefreshAdapter<CourseClassifyBean> {


    public ClassDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_class_details, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(null);
    }


    @Override
    public int getCount() {
        return CommonUtils.quantity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.class_hour_evaluate_tv)
        TextView classHourEvaluateTv;
        @InjectView(R.id.teacher_evaluate_tv)
        TextView teacherEvaluateTv;
        @InjectView(R.id.schoolmate_evaluate_tv)
        TextView schoolmateEvaluateTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick({R.id.class_hour_evaluate_tv, R.id.teacher_evaluate_tv, R.id.schoolmate_evaluate_tv})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.class_hour_evaluate_tv:
                    showEvaluateDialog(CommonUtils.getStringByTextView(classHourEvaluateTv));
                    break;
                case R.id.teacher_evaluate_tv:
                    showEvaluateDialog(CommonUtils.getStringByTextView(teacherEvaluateTv));
                    break;
                case R.id.schoolmate_evaluate_tv:
                    showEvaluateDialog(CommonUtils.getStringByTextView(schoolmateEvaluateTv));
                    break;
            }
        }


        private void showEvaluateDialog(String who){
            AKDialog.evaluateDialog(context, who, new AKDialog.EvaluateListener() {
                @Override
                public void evaluate(String content, int rating) {
                    UIHelper.ToastMessage(context, R.string.developing);
                }
            });
        }

        public void setParameter(final CourseClassifyBean bean) {

        }
    }

}

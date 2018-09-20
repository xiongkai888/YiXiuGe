package com.lanmei.yixiu.ui.teacher.uploadvideo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.utils.FormatTime;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 上传视频
 */
public class UploadVideoListAdapter extends SwipeRefreshAdapter<UploadVideoBean> {

    FormatTime formatTime;
    UploadVideoListContract.Presenter presenter;

    public UploadVideoListAdapter(Context context, UploadVideoListContract.Presenter presenter) {
        super(context);
        this.presenter = presenter;
        formatTime = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_upload_video_list, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final UploadVideoBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isEdit()) {//编辑状态
                    bean.setEdit(!bean.isEdit());
                    presenter.showAllSelect(presenter.isAllSelect());
                    notifyDataSetChanged();
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.compile_iv)
        ImageView compileIv;
        @InjectView(R.id.pic_iv)
        ImageView picIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.progress_bar)
        ProgressBar progressBar;
        @InjectView(R.id.status_tv)
        TextView statusTv;
        @InjectView(R.id.progress_tv)
        TextView progressTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(UploadVideoBean bean) {
//            progressBar.setProgress(bean.getProgress());
//            ImageHelper.load(context,bean.getPic(),picIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
//            titleTv.setText(bean.getTitle());
//            progressTv.setText(bean.getProgress()+"%");
//            statusTv.setText(bean.getStatus());

            if (presenter.isEdit()) {
                compileIv.setVisibility(View.VISIBLE);
                if (bean.isEdit()) {
                    compileIv.setImageResource(R.drawable.pay_on);
                } else {
                    compileIv.setImageResource(R.drawable.pay_off);
                }
            } else {
                compileIv.setVisibility(View.GONE);
            }
        }
    }

    ImmediatelyCheckListener listener;

    public interface ImmediatelyCheckListener {
        /**
         * @param oid       订单id
         * @param id        通知id
         * @param see_state 0未读，1已读
         */
        void immediatelyCheck(String oid, String id, String see_state);

        void seeState(String id);
    }

    public void setImmediatelyCheckListener(ImmediatelyCheckListener listener) {
        this.listener = listener;
    }

}

package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.lanmei.yixiu.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 教程课件
 */
public class TutoralCoursewareAdapter extends SwipeRefreshAdapter<NotesBean> {

    private FormatTime formatTime;
    private List<String> list;

    public TutoralCoursewareAdapter(Context context) {
        super(context);
        formatTime = new FormatTime();
        list = new ArrayList<>();
        list.add("http://photocdn.sohu.com/20090401/Img263140056.jpg");
        list.add("http://photocdn.sohu.com/20090401/Img263140056.jpg");
//        list.add("http://image.so.com/v?q=%E5%9B%BE%E7%89%87&cmsid=1927672450ec8c31fc73ec76b13d9d68&cmran=0&cmras=0&i=0&cmg=42369ccf38a418a5aede2e4c9b99524f&src=360pic_strong&z=1#id=752fba009ee264de0fd66d4c360f5305&itemindex=0&currsn=0&jdx=1&gsrc=1&fsn=60&multiple=0&dataindex=1&prevsn=0");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
//        NotesBean bean = getItem(position);
//        if (bean == null){
//            return;
//        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(null);
    }


    @Override
    public int getCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;
//        @InjectView(R.id.title_et)
//        TextView titleTv;
//        @InjectView(R.id.time_tv)
//        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final NotesBean bean) {
//            titleTv.setText(bean.getTitle());
//            formatTime.setTime(bean.getAddtime());
//            timeTv.setText(formatTime.formatterTime());
            sudokuView.setListData(list);
            sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
                @Override
                public void onClick(int positionSub) {
                    CommonUtils.developing(context);
//                    UIHelper.ToastMessage(context,""+positionSub);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("bean",bean);
//                    IntentUtil.startActivity(context, NoteDetailsActivity.class,bundle);
                }

                @Override
                public void onDoubleTap(int position) {
//                    CommonUtils.showPhotoBrowserActivity(context, bean.getPic(), bean.getPic().get(position));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("bean",bean);
//                    IntentUtil.startActivity(context, NoteDetailsActivity.class,bundle);
                }
            });
        }
    }

}

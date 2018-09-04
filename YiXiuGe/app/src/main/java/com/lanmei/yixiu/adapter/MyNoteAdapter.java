package com.lanmei.yixiu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.ui.home.activity.NewsDetailsActivity;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.widget.SudokuView;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的笔记
 */
public class MyNoteAdapter extends SwipeRefreshAdapter<CourseClassifyBean> {


    List<String> list;

    public MyNoteAdapter(Context context) {
        super(context);
        list = new ArrayList<>();
        list.add("https://goss.veer.com/creative/vcg/veer/800water/veer-319821774.jpg");
        list.add("https://goss.veer.com/creative/vcg/veer/800water/veer-319823827.jpg");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setParameter(null);
    }


    @Override
    public int getCount() {
        return CommonUtils.quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.sudokuView)
        SudokuView sudokuView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }


        public void setParameter(final CourseClassifyBean bean) {

            sudokuView.setListData(list);
            sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
                @Override
                public void onClick(int positionSub) {
//                    startActivity(bean);
                    IntentUtil.startActivity(context, NewsDetailsActivity.class);
                }

                @Override
                public void onDoubleTap(int position) {
                    CommonUtils.showPhotoBrowserActivity(context, list, list.get(position));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.startActivity(context, NewsDetailsActivity.class);
                }
            });
        }
    }

}

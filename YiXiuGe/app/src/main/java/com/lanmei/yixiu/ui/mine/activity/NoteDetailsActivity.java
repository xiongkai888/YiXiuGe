package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.NoteDetailsEnclosureAdapter;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.lanmei.yixiu.widget.SudokuView;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 笔记详情
 */
public class NoteDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.content_tv)
    TextView contentTv;
    @InjectView(R.id.sudokuView)
    SudokuView sudokuView;//
    @InjectView(R.id.recyclerViewEn)
    RecyclerView recyclerViewEn;
    private NotesBean bean;//笔记内容

    @Override
    public int getContentViewId() {
        return R.layout.activity_note_details;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (NotesBean) bundle.getSerializable("bean");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.note_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        if (bean == null){
            return;
        }
        titleTv.setText(bean.getTitle());
        FormatTime time = new FormatTime(bean.getAddtime());
        timeTv.setText(time.formatterTime());
        contentTv.setText(bean.getContent());

        sudokuView.setListData(bean.getPic());
        sudokuView.setOnSingleClickListener(new SudokuView.SudokuViewClickListener() {
            @Override
            public void onClick(int positionSub) {
                CommonUtils.showPhotoBrowserActivity(getContext(), bean.getPic(), bean.getPic().get(positionSub));
            }

            @Override
            public void onDoubleTap(int position) {

            }
        });

        NoteDetailsEnclosureAdapter adapter= new NoteDetailsEnclosureAdapter(this);
        adapter.setData(bean.getEnclosure());
        recyclerViewEn.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEn.setNestedScrollingEnabled(false);
        recyclerViewEn.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_more) {
            CommonUtils.developing(this);
        }
        return super.onOptionsItemSelected(item);
    }
}

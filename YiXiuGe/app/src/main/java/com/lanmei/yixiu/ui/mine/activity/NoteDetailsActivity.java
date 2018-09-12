package com.lanmei.yixiu.ui.mine.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.NoteDetailsEnclosureAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.event.PublishNoteEvent;
import com.lanmei.yixiu.utils.AKDialog;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.FormatTime;
import com.lanmei.yixiu.widget.DetailsMoreView;
import com.lanmei.yixiu.widget.SudokuView;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.SysUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import oss.ManageOssUpload;

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


    private void popupWindow() {
        if (bean == null){
            return;
        }
        DetailsMoreView view = (DetailsMoreView) View.inflate(this, R.layout.view_details_more, null);
        int width = UIBaseUtils.dp2pxInt(this, 80);
        final PopupWindow window = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        int paddingRight = UIBaseUtils.dp2pxInt(this, 0);
        int xoff = SysUtils.getScreenWidth(this) - width - paddingRight;
        window.showAsDropDown(mToolbar, xoff, 2);
        view.setDetailsMoreListener(new DetailsMoreView.DetailsMoreListener() {
            @Override
            public void delete() {
                window.dismiss();
                AKDialog.getAlertDialog(getContext(), "确认要删除该笔记？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote();
                    }
                });
            }

            @Override
            public void shareMore() {
                window.dismiss();
                CommonUtils.developing(getContext());
            }
        });
    }

    private void deleteNote() {
        ManageOssUpload manageOssUpload = new ManageOssUpload(this);
        manageOssUpload.logAyncListObjects();
        YiXiuGeApi api = new YiXiuGeApi("app/del_note");
        api.addParams("id",bean.getId());
        api.addParams("uid",api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                EventBus.getDefault().post(new PublishNoteEvent());//刷新笔记列表
                UIHelper.ToastMessage(getContext(),response.getMsg());
                List<String> list = new ArrayList<>();
                if (!StringUtils.isEmpty(bean.getPic())){
                    list.addAll(bean.getPic());
                }
                List<NotesBean.EnclosureBean> enclosureBeanList = bean.getEnclosure();
                if (!StringUtils.isEmpty(enclosureBeanList)){
                    for (NotesBean.EnclosureBean bean:enclosureBeanList){
                        list.add(bean.getUrl());
                    }
                }
                if (!StringUtils.isEmpty(list)){
                    CommonUtils.deleteOssObjectList(list);//
                }
                finish();
            }
        });
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
            popupWindow();
        }
        return super.onOptionsItemSelected(item);
    }
}

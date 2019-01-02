package com.medui.yixiu.ui.mine.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.NoteDetailsEnclosureAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.NotesBean;
import com.medui.yixiu.event.PublishCoursewareEvent;
import com.medui.yixiu.event.PublishNoteEvent;
import com.medui.yixiu.utils.AKDialog;
import com.medui.yixiu.utils.CommonUtils;
import com.medui.yixiu.utils.FormatTime;
import com.medui.yixiu.widget.DetailsMoreView;
import com.medui.yixiu.widget.SudokuView;
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
 * 笔记详情(课件详情)
 */
public class NoteDetailsActivity extends BaseActivity  implements Toolbar.OnMenuItemClickListener{

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.title_et)
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
    private String type;//不为空是 笔记详情

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
            type = bundle.getString("type");
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {


        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_more);
        //toolbar的menu点击事件的监听
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!StringUtils.isSame(bean.getUid(), CommonUtils.getUserId(this))) {//
            toolbar.getMenu().clear();
        }

        if (!StringUtils.isEmpty(type)) {
            toolbar.setTitle(R.string.note_details);
        } else {
            toolbar.setTitle(R.string.courseware_details);
        }

        if (bean == null) {
            return;
        }
        titleTv.setText(bean.getTitle());
        FormatTime time = new FormatTime(this, bean.getAddtime());
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

        NoteDetailsEnclosureAdapter adapter = new NoteDetailsEnclosureAdapter(this);
        adapter.setData(bean.getEnclosure());
        recyclerViewEn.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEn.setNestedScrollingEnabled(false);
        recyclerViewEn.setAdapter(adapter);
//        adapter.setShareListener(new ShareListener() {
//            @Override
//            public void share(String url) {
//                shareHelper.share(url);
//            }
//        });
    }


    private void popupWindow() {
        if (bean == null) {
            return;
        }
        DetailsMoreView view = (DetailsMoreView) View.inflate(this, R.layout.view_details_more, null);
        view.setTextView("编辑");
        int width = UIBaseUtils.dp2pxInt(this, 80);
        final PopupWindow window = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        int paddingRight = UIBaseUtils.dp2pxInt(this, 0);
        int xoff = SysUtils.getScreenWidth(this) - width - paddingRight;
        window.showAsDropDown(toolbar, xoff, 2);
        view.setDetailsMoreListener(new DetailsMoreView.DetailsMoreListener() {
            @Override
            public void delete() {
                window.dismiss();
                String content = !StringUtils.isEmpty(type) ? getString(R.string.delete_note) : getString(R.string.delete_courseware);
                AKDialog.getAlertDialog(getContext(), content, new DialogInterface.OnClickListener() {
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
        api.addParams("id", bean.getId());
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                if (!StringUtils.isEmpty(type)) {
                    EventBus.getDefault().post(new PublishNoteEvent());//刷新笔记列表
                } else {
                    EventBus.getDefault().post(new PublishCoursewareEvent());//刷新课件列表
                }
                UIHelper.ToastMessage(getContext(), response.getMsg());
                List<String> list = new ArrayList<>();
                if (!StringUtils.isEmpty(bean.getPic())) {
                    list.addAll(bean.getPic());
                }
                List<NotesBean.EnclosureBean> enclosureBeanList = bean.getEnclosure();
                if (!StringUtils.isEmpty(enclosureBeanList)) {
                    for (NotesBean.EnclosureBean bean : enclosureBeanList) {
                        list.add(bean.getUrl());
                    }
                }
                if (!StringUtils.isEmpty(list)) {
                    CommonUtils.deleteOssObjectList(list);//
                }
                finish();
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                if (bean == null) {
                    break;
                }
                popupWindow();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

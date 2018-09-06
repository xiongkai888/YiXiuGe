package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.event.PublishNoteEvent;
import com.lanmei.yixiu.helper.BGASortableNinePhotoHelper;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.CompressPhotoUtils;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 发笔记
 */
public class PublishNoteActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;//拖拽排序九宫格控件
    BGASortableNinePhotoHelper mPhotoHelper;
    @InjectView(R.id.note_title_et)
    DrawClickableEditText noteTitleEt;
    @InjectView(R.id.note_content_et)
    DrawClickableEditText noteContentEt;
    private CompressPhotoUtils compressPhotoUtils;

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_note;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.publish_note);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        initPhotoHelper();
        compressPhotoUtils = new CompressPhotoUtils();
    }


    private void initPhotoHelper() {
        mPhotosSnpl.setVisibility(View.VISIBLE);
        mPhotoHelper = new BGASortableNinePhotoHelper(this, mPhotosSnpl);
        // 设置拖拽排序控件的代理
        mPhotoHelper.setDelegate(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            submitNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitNote() {
        final String title = CommonUtils.getStringByEditText(noteTitleEt);
        if (StringUtils.isEmpty(title)){
            UIHelper.ToastMessage(this,R.string.input_note_title);
            return;
        }
        final String content = CommonUtils.getStringByEditText(noteContentEt);
        if (StringUtils.isEmpty(content)){
            UIHelper.ToastMessage(this,R.string.input_note_content);
            return;
        }

        if (StringUtils.isEmpty(mPhotoHelper.getData())){
            UIHelper.ToastMessage(this,R.string.please_select_picture);
            return;
        }
        compressPhotoUtils.compressPhoto(this, mPhotoHelper.getData(), new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
            @Override
            public void success(List<String> list) {
                if (isFinishing()) {
                    return;
                }
                loadSubmitNote(title,content,list);
            }
        }, "1");
    }

    private void loadSubmitNote(String title,String content,List<String> list){
        YiXiuGeApi api = new YiXiuGeApi("app/add_note");
        api.addParams("uid",api.getUserId(this));
        api.addParams("title",title);
        api.addParams("enclosure","附件是要传什么东东啦？");
        api.addParams("content",content);
        api.addParams("pic",CommonUtils.getStringByList(list));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(getContext(),response.getMsg());
                EventBus.getDefault().post(new PublishNoteEvent());//
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compressPhotoUtils.cancelled();
    }

    @OnClick({R.id.file_iv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.file_iv2:
                CommonUtils.developing(this);
//                new LFilePicker()
//                        .withActivity(this)
//                        .withRequestCode(100)
//                        .withIconStyle(Constant.ICON_STYLE_YELLOW)
//                        .withTitle("上传文件")//标题文字
//                        .withMutilyMode(true)//多选
//                        .withFileFilter(new String[]{".txt", ".png", "jpeg"})//过滤！
//                        .withBackIcon(Constant.BACKICON_STYLEONE)
//                        .withBackgroundColor("#1593f0")//标题背景颜色
//                        .start();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
            } else {
                mPhotoHelper.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        mPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
    }

}

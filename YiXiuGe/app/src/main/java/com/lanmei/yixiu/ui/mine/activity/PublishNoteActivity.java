package com.lanmei.yixiu.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lanmei.yixiu.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 发笔记
 */
public class PublishNoteActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            UIHelper.ToastMessage(this, R.string.developing);
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.file_iv1, R.id.file_iv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.file_iv1:
            case R.id.file_iv2:
                new LFilePicker()
                        .withActivity(this)
                        .withRequestCode(100)
                        .withIconStyle(Constant.ICON_STYLE_YELLOW)
                        .withTitle("上传文件")//标题文字
                        .withMutilyMode(true)//多选
                        .withFileFilter(new String[]{".txt", ".png","jpeg"})//过滤！
                        .withBackIcon(Constant.BACKICON_STYLEONE)
                        .withBackgroundColor("#1593f0")//标题背景颜色
                        .start();
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
            }
        }
    }
}

package com.lanmei.yixiu.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.yixiu.R;
import com.xson.common.app.BaseActivity;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 会话
 */
public class ConversationListActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_conversation_list;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(com.hyphenate.easeui.R.string.session);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

    }
}

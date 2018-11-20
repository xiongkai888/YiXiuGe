package com.medui.yixiu.ui.message.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.NewGroupActivity;
import com.hyphenate.exceptions.HyphenateException;
import com.medui.yixiu.R;
import com.medui.yixiu.adapter.GroupSubAdapter;
import com.medui.yixiu.event.GroupListEvent;
import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 消息-群聊
 */

public class QunFragment extends BaseFragment {


    @InjectView(R.id.list)
    ListView groupListView;
    @InjectView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    protected List<EMGroup> grouplist;
    private GroupSubAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    private boolean isStudent;

    @Override
    public int getContentViewId() {
//        return R.layout.fragment_single_listview;
        return R.layout.fragment_qun;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        isStudent = CommonUtils.isStudent(context);

        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        for (EMGroup group : grouplist) {
            L.d(L.TAG,group.getGroupName()+","+group.getGroupId());
        }
        //show group list
        groupAdapter = new GroupSubAdapter(context, 1, grouplist, isStudent);
        groupListView.setAdapter(groupAdapter);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isStudent) {
                    // enter group chat
                    Intent intent = new Intent(context, ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position - 1).getGroupId());
                    startActivityForResult(intent, 0);
                } else {
                    if (position == 1) {
                        // create a new group
                        startActivityForResult(new Intent(context, NewGroupActivity.class), 0);
                    } else {
                        // enter group chat
                        Intent intent = new Intent(context, ChatActivity.class);
                        // it is group chat
                        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        intent.putExtra("userId", groupAdapter.getItem(position - 2).getGroupId());
                        startActivityForResult(intent, 0);
                    }
                }

            }

        });
        groupListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        //pull down to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            EventBus.getDefault().post(new GroupListEvent(0));
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post(new GroupListEvent(1));
                        }
                    }
                }.start();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void groupListEvent(GroupListEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        if (event.getType() == 0) {
            refresh();
        } else {
            Toast.makeText(context, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupSubAdapter(context, 1, grouplist, CommonUtils.isStudent(context));
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

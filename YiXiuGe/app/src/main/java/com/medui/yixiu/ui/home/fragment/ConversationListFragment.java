package com.medui.yixiu.ui.home.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.ChatFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.medui.yixiu.R;
import com.medui.yixiu.event.UserBeanEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 环信会话列表
 */
public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);

    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        hideTitleBar();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                            if (group == null) {
                                Toast.makeText(getContext(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                                conversationListView.removeItem(position);
                                return;
                            }
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
        super.setUpView();
    }

    private boolean i;
    private boolean isFirst = true;

    @Subscribe
    public void userBeanEvent(UserBeanEvent event) {
        if (!i) {
            if (isFirst) {
                refresh();
                isFirst = !isFirst;
            }
            query.postDelayed(heartBeatRunnable, ChatFragment.HEART_BEAT_RATE);
            i = true;
        }
    }

    private Runnable heartBeatRunnable = new Runnable() {//心跳包请求位置信息
        @Override
        public void run() {
            if (query == null){
                return;
            }
            i = false;
            refresh();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        query.removeCallbacks(heartBeatRunnable);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }
}

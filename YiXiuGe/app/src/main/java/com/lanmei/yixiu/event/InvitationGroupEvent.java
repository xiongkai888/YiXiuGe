package com.lanmei.yixiu.event;

import com.hyphenate.chat.EMGroup;

/**
 * Created by xkai on 2018/11/1.
 * 自己被邀请进群
 */

public class InvitationGroupEvent {

    private EMGroup group;

    public EMGroup getGroup() {
        return group;
    }

    public InvitationGroupEvent(EMGroup group){
        this.group = group;
    }
}

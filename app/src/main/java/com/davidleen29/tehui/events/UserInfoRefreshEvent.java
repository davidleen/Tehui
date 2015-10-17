package com.davidleen29.tehui.events;

import com.huiyou.dp.service.protocol.User;

/**
 *
 * 用户信息改变  包括我的统计数据
 * Created by davidleen29 on 2015/7/27.
 */
public class UserInfoRefreshEvent {

    public User.UserSetUserInfo userSetUserInfo;
    public UserInfoRefreshEvent(User.UserSetUserInfo userSetUserInfo) {

        this.userSetUserInfo=userSetUserInfo;
    }
}

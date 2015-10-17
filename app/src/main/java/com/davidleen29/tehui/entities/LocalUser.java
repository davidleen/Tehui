package com.davidleen29.tehui.entities;

import com.huiyou.dp.service.protocol.User;

/**
 * Created by davidleen29 on 2015/7/22.
 */
public class LocalUser {




    public LocalUser(User.UserInfo userInfo)
    {
        userInfo.getEmail();
        userInfo.getLastLoginDevice() ;
        userInfo.getLastLoginIp();
        userInfo.getLastLoginTime();
        userInfo.getPhone();
        userInfo.getLoginCount();
        userInfo.getRegisterTime();
    }
}

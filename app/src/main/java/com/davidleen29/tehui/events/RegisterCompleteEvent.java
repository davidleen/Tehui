package com.davidleen29.tehui.events;

import com.huiyou.dp.service.protocol.Common;

/**
 * Created by davidleen29 on 2015/7/25.
 */
public class RegisterCompleteEvent {

    public String phoneNumber;
    public String password;
    public
    Common.SmsSendType type;

    public RegisterCompleteEvent(String phoneNumber,String password,Common.SmsSendType type) {
        this.phoneNumber = phoneNumber;
        this.password=password;
        this.type=type;
    }
}

package com.davidleen29.tehui.events;

import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.List;
import java.util.Set;

/**
 * Created by davidleen29 on 2015/8/20.
 */
public class PeriodSetEvent {

    public Common.EffectivePeriodType periodType;
    public List<Integer> items;
}

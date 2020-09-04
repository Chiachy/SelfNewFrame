package com.common.base.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by  HSK° on 2017/10/12.
 *
 * @ function :  EventBus工具类
 */

public class EventBusUtils {

    /**
     *
     * @param subscriber 反注册
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     *
     * @param subscriber 注册
     */
    public static void unregister(Object subscriber) {
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     *
     * @param event 发送普通事件
     */
    public static void sendEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     *
     * @param event 发送粘性事件
     */
    public static void sendStickyEvent(Object event) {
        EventBus.getDefault().postSticky(event);
    }

}
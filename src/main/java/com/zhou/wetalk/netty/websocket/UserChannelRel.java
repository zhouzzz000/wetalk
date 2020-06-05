package com.zhou.wetalk.netty.websocket;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:58
 * @ClassName UserChannelRel
 * @see
 */
public class UserChannelRel {
    public static HashMap<String, Channel> manage = new HashMap<>();

    public static void put(String sendUserId, Channel channel)
    {
        manage.put(sendUserId,channel);
    }

    public static Channel get(String sendUserId)
    {
        return manage.get(sendUserId);
    }

    public static void printRel()
    {
        for (HashMap.Entry<String,Channel> entry : manage.entrySet())
        {
            System.out.println("userId：" + entry.getKey() + ",channelId：" + entry.getValue().id().asLongText());
        }
    }
}
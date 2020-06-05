package com.zhou.wetalk;

import com.zhou.wetalk.netty.websocket.WssServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/8
 * @Time 17:27
 * @ClassName NettyBoot
 * @see
 */
@Component
public class NettyBoot  implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private WssServer wssServer;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null)
        {
           try {
               wssServer.start();
           }catch (Exception e)
           {
               e.printStackTrace();
           }
        }
    }
}
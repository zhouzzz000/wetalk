package com.zhou.wetalk.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/27
 * @Time 16:35
 * @ClassName KeepAliveHandler
 * @see
 */
public class KeepAliveHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲）
       if (evt instanceof IdleStateEvent){
           //强制类型转换
           IdleStateEvent event = (IdleStateEvent)evt;
           if (event.state().equals(IdleState.READER_IDLE))
           {
//               System.out.println("进入读空闲");
           }else if (event.state().equals(IdleState.WRITER_IDLE))
           {
//               System.out.println("进入写空闲");
           }else if(event.state().equals(IdleState.ALL_IDLE))
           {
               // 读写空闲，需要处理
               Channel channel = ctx.channel();
               ctx.flush();
               // 关闭无用的channel
               channel.close();
               ChatHandler.users.remove(channel);
           }
       }
    }


}
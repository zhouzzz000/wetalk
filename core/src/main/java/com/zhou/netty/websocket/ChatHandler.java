package com.zhou.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 16:04
 * @ClassName ChatHandler
 * @see
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     *  用户记录和管理所有客户端的channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected ChatHandler() {
        super();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        System.out.println("新建连接："+ctx.channel().remoteAddress() + ": " +ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当初发handlerRemoved，客户端会自动触发clienrs.remove
        // clients.remove(ctx.channel());
        System.out.println("client off,channel long id is: " + ctx.channel().id().asLongText());
        System.out.println("client off,channel short id is: " + ctx.channel().id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        System.out.println("recive message：" + content);
        Channel currentChannel = channelHandlerContext.channel();
        for (Channel channel: clients)
        {
//            if(currentChannel.id().asShortText().equals(channel.id().asShortText()))
//            {
//                continue;
//            }
            channel.writeAndFlush(new TextWebSocketFrame("server at "+ LocalDateTime.now() + " ,user " + currentChannel.id().asShortText() + ": "+content));
        }
    }
}
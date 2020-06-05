package com.zhou.wetalk.netty.websocket;

import com.zhou.utils.JsonUtils;
import com.zhou.wetalk.dto.DataContentDto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 16:04
 * @ClassName ChatHandler
 * @see
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     *  用户记录和管理所有客户端的channel
     */
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected ChatHandler() {
        super();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
//        System.out.println("新建连接："+ctx.channel().remoteAddress() + ": " +ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("websocket exception:" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
        users.remove(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        users.remove(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 1.获取客户端发来的消息
        // 2.判断消息类型，根据不同的类型来处理不同的业务
        // 2.1 当websocket第一次open的时候，初始化channel，把channel和userId关联起来
        // 2.2 聊天消息类型，把聊天记录保存到数据库，同时标记消息的签收状态【未签收】
        // 2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态【已签收】
        // 2.4 心跳类型的消息
        Channel curChannel = channelHandlerContext.channel();
        String content = textWebSocketFrame.text();
        DataContentDto dataContent = JsonUtils.jsonToPojo(content,DataContentDto.class);
        ActionFactory.handleActionFactory(dataContent,users,curChannel);
        channelHandlerContext.flush();
    }
}
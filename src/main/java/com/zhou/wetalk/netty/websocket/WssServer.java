package com.zhou.wetalk.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 15:45
 * @ClassName WssServer
 * @see
 */
@Component
@Scope("singleton")
public class WssServer {
    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ChannelFuture channelFuture;
    private ServerBootstrap server;
    @PostConstruct
    public void init()
    {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup,subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }

    public void start(){
        this.channelFuture = server.bind(8088);
        System.err.println("netty websocket server start completed...");
    }
}
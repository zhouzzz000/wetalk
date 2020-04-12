package com.zhou.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 15:45
 * @ClassName WssServer
 * @see
 */
public class WssServer {
    public static void main(String[] args) {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup, subGroup)
                       .channel(NioServerSocketChannel.class)
                       .childHandler(new WSServerInitializer());
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
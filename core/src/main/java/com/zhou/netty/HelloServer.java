package com.zhou.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 14:33
 * @ClassName HelloServer
 * @see
 */
public class HelloServer {
    public static void main(String[] args) {
        // 主线程组
        // 用于接受客户端的连接，但是不做任何业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组
        // 处理客户端的请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // netty服务器的创建，ServerBootstrap是一个启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
                        //设置主从线程组
        serverBootstrap.group(bossGroup,workerGroup)
                        // 设置nio双向通道
                       .channel(NioServerSocketChannel.class)
                        // 子处理器，用于处理workerGroup
                       .childHandler(new ServerInitalizer());
        try {
            // 启动server，绑定端口，并设置为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
package com.zhou.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 14:49
 * @ClassName ServerInitalizer
 * @see
 */
public class ServerInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 通过SocketChannel取获取对应的管道
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // 通过管道添加netty自带Handler
        channelPipeline.addLast("HttpServerCodec", new HttpServerCodec());
        // 通过管道添加自定义的Handler
        channelPipeline.addLast("CustomHandler",new CustomHandler());
    }
}
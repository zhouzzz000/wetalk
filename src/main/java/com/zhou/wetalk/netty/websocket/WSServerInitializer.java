package com.zhou.wetalk.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ProjectName zhou
 * @Author zhouzzz
 * @Date 2020/4/4
 * @Time 15:53
 * @ClassName WSServerInitializer
 * @see
 */

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对HttpMessage进行聚合，聚合成FullRequest或HttpResponse
        // 几乎在netty中的编程，都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //====================以上用于支持http协议====================//
        /** websocket服务器处理的协议，用于指定给客户端访问的路由
         * 会帮忙处理握手动作：handshaking（close，ping， pong）
         * ping+pong = 心跳
         * 对于websocket来讲，都是以frams进行传输的，不同的数据类型对应得frames也不同
         */
        /*---------------------增加心跳支持start-------------------*/
        // 如果是客户端10分钟没有向服务端发送读写心跳（All）,则主动断开
        // 读写空闲不做处理
        pipeline.addLast(new IdleStateHandler(60,60,60));
        // 自定义的空闲状态检测
        pipeline.addLast(new KeepAliveHandler());
        /*---------------------增加心跳支持end-------------------*/
        pipeline.addLast(new WebSocketServerProtocolHandler("/wss"));
        // 自定义handler，用户接受客户端消息和返回客户端消息
        pipeline.addLast(new ChatHandler());


    }
}
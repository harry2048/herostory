package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;

/**
 * @Auther: gengwei
 * @Date: 2019-12-20 23:19
 * @Description:
 */
public class ServerMain {
    // 日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    // 应用主函数
    public static void main(String[] args) {
        /**
         * 启动时，所有的handler就注册到map中
         *
         */
        CmdHandlerFactory.init();
        /**
         * 初始化消息识别器
         */
        GameMsgRecognizer.init();

        /**
         * 处理客户端连接，客户端有连接的时候，建立SocketChannel交给workGroup
         * 负责连接，通过socketChannel来连接
         */
        EventLoopGroup bassGroup = new NioEventLoopGroup();

        // 处理消息，负责消息的读写，将消息放到pipeline中，负责业务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bassGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new HttpServerCodec(),// Http编解码器
                        new HttpObjectAggregator(65535),// 内容长度限制
                        new WebSocketServerProtocolHandler("/websocket"),// webSocket 协议处理器，在这里处理握手、ping pong等消息
                        new GameMsgDecoder(),// 自定义的消息解码器
                        new GameMsgEncoder(),// 自定义的消息编码器
                        new GameMsghandler()// 自定义的消息处理器
                );
            }
        });

        try {
            ChannelFuture f = b.bind(12345).sync();

            if (f.isSuccess()) {
                LOGGER.info("服务器启动成功！");
            }

            f.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

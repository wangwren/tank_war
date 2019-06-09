package com.wangwren.tank.net;

import java.util.UUID;

import com.wangwren.tank.Dir;
import com.wangwren.tank.Group;
import com.wangwren.tank.Tank;
import com.wangwren.tank.TankFrame;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {

	private Channel channel = null;

	public void connect() {
		//线程池
		EventLoopGroup group = new NioEventLoopGroup(1);

		Bootstrap b = new Bootstrap();

		try {
			ChannelFuture f = b.group(group).channel(NioSocketChannel.class).handler(new ClientChannelInitializer())
					.connect("localhost", 8888);

			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						System.out.println("not connected!");
					} else {
						System.out.println("connected!");
						// initialize the channel
						channel = future.channel();
					}
				}
			});

			f.sync();
			// wait until close
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public void send(String msg) {
		ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
		channel.writeAndFlush(buf);
	}

	public void closeConnect() {
		//this.send("_bye_");
		//channel.close();
	}
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
			//加上编码，需要加在逻辑前
			.addLast(new TankJoinMsgEncoder())
			//还需要解码
			.addLast(new TankJoinMsgDecoder())
			.addLast(new ClientHandler());
	}

}

/**
 * 改用SimpleChannelInboundHandler 这个类可以对接收来的消息加泛型
 * @author wwr
 *
 */
class ClientHandler extends SimpleChannelInboundHandler<TankJoinMsg> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//ctx.writeAndFlush(TankFrame.INSTANCE.getMainTank());
		//是通过，TankJoinMsg来向服务端发送消息，不能发别的
		ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) throws Exception {
		//如果服务端返回的信息，坦克已经存在，那么不加
		//这个返回的信息是对每一个客户端都发，如果是自己，那么就不会加，但是对于别的客户端，就是另一个坦克，就会加
		if(msg.id.equals(TankFrame.INSTANCE.getMainTank().getId()) ||
				TankFrame.INSTANCE.findByUUID(msg.id) != null) {
			return;
		}
		//创建一个新的坦克，是别的客户端的坦克
		Tank tank = new Tank(msg);
		//加入到自己的主界面中
		TankFrame.INSTANCE.addTank(tank);
		
		//如果加了一个新的坦克，那么对于别的客户端，也要把自己也加上，所以要向服务端发自己的坦克信息，使别的客户端能看见自己
		ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
	}

}

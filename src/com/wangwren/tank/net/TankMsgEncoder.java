package com.wangwren.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 自定义协议，编码。加在Client端
 * @author wwr
 *
 */
public class TankMsgEncoder extends MessageToByteEncoder<TankMsg>{

	@Override
	protected void encode(ChannelHandlerContext ctx, TankMsg msg, ByteBuf buf) throws Exception {
		buf.writeInt(msg.x);
		buf.writeInt(msg.y);
	}
	

}

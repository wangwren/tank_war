package com.wangwren.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 自定义协议，编码。加在Client端
 * @author wwr
 *
 */
public class TankJoinMsgEncoder extends MessageToByteEncoder<TankJoinMsg>{

	@Override
	protected void encode(ChannelHandlerContext ctx, TankJoinMsg msg, ByteBuf buf) throws Exception {
		//因为已经转成字节数组了，直接写字节数组就行
		buf.writeBytes(msg.toBytes());
	}
	

}

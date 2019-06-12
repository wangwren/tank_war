package com.wangwren.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 自定义协议，编码。加在Client端
 * @author wwr
 *
 */
public class MsgEncoder extends MessageToByteEncoder<Msg>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf buf) throws Exception {
		//编码方式做了修改
		//前8个字节：前4个字节存放消息类型；后4个字节存放toBytes方法得到的数组的长度，即真正的消息长度
		buf.writeInt(msg.getMsgType().ordinal());
		byte[] bytes = msg.toBytes();
		buf.writeInt(bytes.length);
		
		//因为已经转成字节数组了，直接写字节数组就行
		buf.writeBytes(msg.toBytes());
	}
	

}

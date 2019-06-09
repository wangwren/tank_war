package com.wangwren.tank.net;

import java.util.List;
import java.util.UUID;

import com.wangwren.tank.Dir;
import com.wangwren.tank.Group;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 自定义协议，解码。加在服务器端
 * @author wwr
 *
 */
public class TankJoinMsgDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//这个值是需要算的
		if(in.readableBytes() < 33) return; //TCP 拆包 粘包
		
		//in.markReaderIndex();
		
		TankJoinMsg msg = new TankJoinMsg();

		msg.x = in.readInt();
		msg.y = in.readInt();
		msg.dir = Dir.values()[in.readInt()];
		msg.moving = in.readBoolean();
		msg.group = Group.values()[in.readInt()];
		msg.id = new UUID(in.readLong(), in.readLong());

		out.add(msg);
	}

}

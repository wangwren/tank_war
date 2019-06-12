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
		//先判断是否够8个字节
		if(in.readableBytes() < 8) return; //TCP 拆包 粘包
		
		//从byteBuf可以读的位置开始
		in.markReaderIndex();
		
		//先获取消息类型，通过in读取前四个字节，即写的时候也是先写入的消息类型
		MsgType msgType = MsgType.values()[in.readInt()];
		//再读消息长度
		int length = in.readInt();
		if(in.readableBytes() < length) {
			//再读取就是获取到具体消息了，如果消息长度小于length，说明读的不全，重新标置in，重读
			in.resetReaderIndex();
			return;
		}
		
		//如果以上都符合，可以创建消息对象了
		byte[] bytes = new byte[length];
		//读数据进bytes数组
		in.readBytes(bytes);
		
		switch (msgType) {
		case TankJoin:
			TankJoinMsg msg = new TankJoinMsg();
			msg.parse(bytes);
			out.add(msg);
			break;

		}
	}

}

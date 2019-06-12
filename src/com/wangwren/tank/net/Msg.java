package com.wangwren.tank.net;

/**
 * 所有的消息类都需要继承该类，并实现其中方法
 * @author wwr
 *
 */
public abstract class Msg {

	/**
	 * 对消息的处理
	 */
	public abstract void handle();
	
	/**
	 * 将当前消息类的信息转成二进制数组
	 * @return
	 */
	public abstract byte[] toBytes();
	
	/**
	 * 将字节数组转成当前对象
	 * @param bytes
	 */
	public abstract void parse(byte[] bytes);
	
	/**
	 * 获取当前消息类的类型
	 * @return
	 */
	public abstract MsgType getMsgType();
}

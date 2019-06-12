package com.wangwren.tank.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.wangwren.tank.Dir;
import com.wangwren.tank.Group;
import com.wangwren.tank.Tank;
import com.wangwren.tank.TankFrame;

/**
 * 坦克加入的消息
 * @author wwr
 *
 */
public class TankJoinMsg extends Msg {
	public int x, y;
	public Dir dir;
	public boolean moving;
	public Group group;
	//UUID是128位
	public UUID id;

	public TankJoinMsg() {}
	
	public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.moving = moving;
		this.group = group;
		this.id = id; 
	}
	
	public TankJoinMsg(Tank tank) {
		this.x = tank.getX();
		this.y = tank.getY();
		this.dir = tank.getDir();
		this.moving = tank.isMoving();
		this.group = tank.getGroup();
		this.id = tank.getId();
	}
	
	/**
	 * 将字节数组中的数据一个个读出来，赋值给当前对象中的属性
	 * 注意读取的顺序，需要和写入的顺序一样
	 * @param bytes
	 */
	@Override
	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			this.x = dis.readInt();
			this.y = dis.readInt();
			this.dir = Dir.values()[dis.readInt()];
			this.moving = dis.readBoolean();
			this.group = Group.values()[dis.readInt()];
			this.id = new UUID(dis.readLong(), dis.readLong());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 将坦克加入信息转成字节数组，方便网络传输
	 * @return
	 */
	@Override
	public byte[] toBytes() {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			//通过dos倒一下手，dos好用一些
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			//开始向流中写数据，注意顺序
			dos.writeInt(x);
			dos.writeInt(y);
			//enum枚举类型的下标值
			dos.writeInt(dir.ordinal());
			dos.writeBoolean(moving);
			dos.writeInt(group.ordinal());
			//UUID是128位，也不是基本数据类型，所以分开写，一个long类型是64位，写两次正好
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			dos.flush();
			//给byte数组
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(dos != null) {
					dos.close();
				}
				if(baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName())
			   .append("[")
			   .append("uuid=" + id + " | ")
			   .append("x=" + x + " | ")
			   .append("y=" + y + " | ")
			   .append("moving=" + moving + " | ")
			   .append("dir=" + dir + " | ")
			   .append("group=" + group + " | ")
			   .append("]");
		return builder.toString();
	}

	@Override
	public void handle() {
		//如果加入的坦克是自己或者已经在自己的客户端中存在该坦克，那么不加
		if(this.id.equals(TankFrame.INSTANCE.getMainTank().getId()) || TankFrame.INSTANCE.findByUUID(this.id) != null) {
			return;
		}
		
		//否则创建一个Tank，并加入自己的客户端中，并把自己也发送出去，让新加入的客户端能看见
		Tank tank = new Tank(this);
		TankFrame.INSTANCE.addTank(tank);
		
		//把自己当做消息发出去
		Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
	}

	@Override
	public MsgType getMsgType() {
		return MsgType.TankJoin;
	}
}

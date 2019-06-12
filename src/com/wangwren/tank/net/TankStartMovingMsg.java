package com.wangwren.tank.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.wangwren.tank.Dir;
import com.wangwren.tank.Tank;
import com.wangwren.tank.TankFrame;

/**
 * 坦克移动消息处理
 * @author wwr
 *
 */
public class TankStartMovingMsg extends Msg {
	
	private int x,y;
	private UUID id;
	private Dir dir;
	
	public TankStartMovingMsg() {}
	
	public TankStartMovingMsg(int x, int y, UUID id, Dir dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.id = id;
	}
	
	public TankStartMovingMsg(Tank tank) {
		this.x = tank.getX();
		this.y = tank.getY();
		this.dir = tank.getDir();
		this.id = tank.getId();
	}

	@Override
	public void handle() {
		if(this.id.equals(TankFrame.INSTANCE.getMainTank().getId())) {
			//如果是自己，不处理
			return;
		}
		Tank tank = TankFrame.INSTANCE.findByUUID(this.id);
		if(tank != null) {
			tank.setMoving(true);
			tank.setX(this.x);
			tank.setY(this.y);
			tank.setDir(this.dir);
		}
	}

	@Override
	public byte[] toBytes() {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			
			
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeLong(id.getMostSignificantBits());;
			dos.writeLong(id.getLeastSignificantBits());;
			dos.writeInt(dir.ordinal());
			dos.flush();
			bytes = baos.toByteArray();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}

	@Override
	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			this.x = dis.readInt();
			this.y = dis.readInt();
			this.id = new UUID(dis.readLong(), dis.readLong());
			this.dir = Dir.values()[dis.readInt()];
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public MsgType getMsgType() {
		return MsgType.TankStartMoving;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Dir getDir() {
		return dir;
	}

	public void setDir(Dir dir) {
		this.dir = dir;
	}
}

package com.wangwren.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
import java.util.UUID;

import com.wangwren.tank.net.TankJoinMsg;

/**
 * 坦克类
 * @author wwr
 *
 */
public class Tank {
	
	//坦克位置
	private int x;
	private int y;
	//坦克方向
	private Dir dir;
	
	//UUID
	private UUID id = UUID.randomUUID();
	
	//坦克速度
	private static int SPEED = 5;
	
	//坦克是否移动，默认静止，false静止；true移动
	private boolean moving = false;
	
	//坦克图片大小
	public static int WIDTH = ResourceMgr.goodTankU.getWidth();
	public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
	
	//坦克状态
	private boolean living = true;
	
	//坦克分组，默认坏坦克
	private Group group = Group.BAD;
	
	private Random random = new Random();
	
	//拿一下TankFrame的引用，需要设置子弹
	private TankFrame tf = null;
	
	Rectangle rectTank = new Rectangle();
	
	public Tank(TankJoinMsg msg) {
		this.x = msg.x;
		this.y = msg.y;
		this.dir = msg.dir;
		this.moving = msg.moving;
		this.group = msg.group;
		this.id = msg.id;
	}

	public Tank(int x, int y, Dir dir, Group group, TankFrame tf) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.group = group;
		this.tf = tf;
		
		rectTank.x = x;
		rectTank.y = y;
		rectTank.width = WIDTH;
		rectTank.height = HEIGHT;
	}

	/**
	 * 画坦克
	 * @param g
	 */
	public void paint(Graphics g) {
		
		if(!this.living) {
			tf.tanks.remove(this);
		}
		
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString(id.toString(), this.x, this.y - 10);
		g.setColor(c);
		
		//画出坦克图片,根据方向，画出对应的图片
		switch (dir) {
			case LEFT : 
				g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL, x, y, null);
				break;
			case UP : 
				g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU : ResourceMgr.badTankU, x, y, null);
				break;
			case DOWN : 
				g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);
				break;
			case RIGHT : 
				g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
				break;
		}
		
		//通过方向来引导自己向哪移动
		//调用移动方法
		move();
	}

	/**
	 * 坦克移动方法
	 */
	private void move() {
		//如果坦克静止，直接返回，不移动了
		if(!moving) {
			return;
		}
		//判断坦克方向
		switch (dir) {
			case LEFT:
				x -= SPEED;
				break;
			case UP:
				y -= SPEED;
				break;
			case RIGHT:
				x += SPEED;
				break;
			case DOWN:
				y += SPEED;
				break;
		}
		
		//生成随机数，当随机数大于8时，就打出一颗子弹
		if(this.group == Group.BAD && random.nextInt(100) > 95) {
			fire();
		}
		
		//坏坦克随机移动位置,减少频繁换方向，也通过随机数概率来换方向
		if(this.group == Group.BAD && random.nextInt(100) > 95) {
			randomDir();
		}
		
		//坦克边界检测，防止坦克移到屏幕外面
		boundayCheck();
		
		rectTank.x = this.x;
		rectTank.y = this.y;
	}
	
	/**
	 * 坦克边界检测
	 */
	private void boundayCheck() {
		if(this.x < 2) {
			this.x = 2;
		}
		if(this.x > TankFrame.GAME_WIDTH - WIDTH - 2) {
			this.x = TankFrame.GAME_WIDTH - WIDTH - 2;
		}
		if(this.y < 28) {
			this.y = 28;
		}
		if(this.y > TankFrame.GAME_HEIGHT - HEIGHT - 2) {
			this.y = TankFrame.GAME_HEIGHT - HEIGHT - 2;
		}
	}

	/**
	 * 坏坦克随机移动位置,更换方向即可
	 */
	private void randomDir() {
		//获取到所有的方向
		Dir[] dirs = Dir.values();
		//通过生成四个随机数来改变方向
		this.dir = dirs[random.nextInt(4)];
	}

	/**
	 * 坦克开火
	 */
	public void fire() {
		//tf.b = new Bullet(x, y, dir);
		//计算出子弹位置
		int bX = x + WIDTH / 2 - Bullet.WIDTH / 2;
		int bY = y + HEIGHT / 2 - Bullet.HEIGHT / 2;
		//创建的子弹跟随坦克的方向和分组
		tf.bullets.add(new Bullet(bX, bY, this.dir,this.group,this.tf));
		
		//自己的坦克开火声音
		if(this.group == Group.GOOD) new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
	}
	
	/**
	 * 坦克死
	 */
	public void die() {
		this.living = false;
	}

	public Dir getDir() {
		return dir;
	}

	public void setDir(Dir dir) {
		this.dir = dir;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}	
	
}

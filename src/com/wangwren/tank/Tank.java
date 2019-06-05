package com.wangwren.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import com.wangwren.tank.strategy.FireStrategy;

/**
 * 坦克类
 * @author wwr
 *
 */
public class Tank extends GameObject {
	
	//坦克位置，父类中有
//	private int x;
//	private int y;
	
	//记录坦克的上一次移动位置
	private int oldX;
	private int oldY;
	
	//坦克方向
	private Dir dir;
	
	//坦克速度
	private static int SPEED = 5;
	
	//坦克是否移动，默认静止，false静止；true移动
	private boolean moving = true;
	
	//坦克图片大小
	public static int WIDTH = ResourceMgr.goodTankU.getWidth();
	public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
	
	//坦克状态
	private boolean living = true;
	
	//坦克分组，默认坏坦克
	private Group group = Group.BAD;
	
	private Random random = new Random();
	
	//拿一下GameModel的引用，需要设置子弹
	//public GameModel gm = null;
	
	//定义坦克开火策略
	private FireStrategy fs;
	
	private Rectangle rectTank = new Rectangle();

	public Tank(int x, int y, Dir dir, Group group) {
		this.x = x;
		this.y = y;
		
		this.dir = dir;
		this.group = group;
		//this.gm = gm;
		
		rectTank.x = x;
		rectTank.y = y;
		rectTank.width = WIDTH;
		rectTank.height = HEIGHT;
		
		//创建坦克时，根据分组指定开火策略
		if(this.getGroup() == Group.GOOD) {
			//通过配置文件指定开火策略
			String goodFS = (String) PropertyMgr.getProp("goodFS");
			try {
				//Class.forName指定类的全路径名，将该类加载到内存中，之后使用newInstance实例化该类
				fs = (FireStrategy) Class.forName(goodFS).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			String badFS = (String) PropertyMgr.getProp("badFS");
			try {
				fs = (FireStrategy) Class.forName(badFS).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.group != Group.GOOD) {
			GameModel.getInstance().addGameObject(this);
		}
		
	}

	/**
	 * 画坦克
	 * @param g
	 */
	@Override
	public void paint(Graphics g) {
		
		if(!this.living) {
			GameModel.getInstance().removeGameObject(this);
		}
		
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
		
		//移动之前，记录一下坦克上一次移动的位置
		this.oldX = x;
		this.oldY = y;
		
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
		fs.fire(this);
	}
	
	/**
	 * 坦克死
	 */
	public void die() {
		this.living = false;
	}
	
	/**
	 * 坦克停止
	 * @return
	 */
	public void tankStop() {
		this.moving = false;
	}
	
	/**
	 * 坦克退回到上一次移动位置
	 */
	public void back() {
		this.x = this.oldX;
		this.y = this.oldY;
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

	public Rectangle getRectTank() {
		return rectTank;
	}

	public int getOldX() {
		return oldX;
	}

	public void setOldX(int oldX) {
		this.oldX = oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public void setOldY(int oldY) {
		this.oldY = oldY;
	}	
	
}

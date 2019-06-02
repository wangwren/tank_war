package com.wangwren.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 子弹类
 * @author wwr
 *
 */
public class Bullet {

	//子弹位置
	private int x;
	private int y;
	
	//子弹方向，应该跟随坦克方向
	private Dir dir;
	//子弹的速度
	private static int SPEED = 6;
	
	//子弹大小
	public static int WIDTH = ResourceMgr.bulletL.getWidth();
	public static int HEIGHT = ResourceMgr.bulletL.getHeight();
	
	//TankFrame的引用
	private GameModel gm = null;
	
	//子弹状态，true表示活着；false表示屎了
	private boolean living = true;
	
	//子弹分组
	private Group group = Group.BAD;
	
	//维护一个碰撞检测使用到的类
	Rectangle rectBullet = new Rectangle();

	public Bullet(int x, int y, Dir dir, Group group, GameModel gm) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.group = group;
		this.gm = gm;
		
		rectBullet.x = x;
		rectBullet.y = y;
		rectBullet.width = WIDTH;
		rectBullet.height = HEIGHT;
		
		//重构代码，在创建子弹时加入子弹集合中
		gm.bullets.add(this);
	}

	/**
	 * 子弹把自己画出来
	 * @param g
	 */
	public void paint(Graphics g) {
		if(!living) {
			//清除当前子弹，消除内存泄露问题
			gm.bullets.remove(this);
		}
		//根据方向画出子弹图片
		switch (dir) { 
			case LEFT :
				g.drawImage(ResourceMgr.bulletL, x, y, null);
				break;
			case UP :
				g.drawImage(ResourceMgr.bulletU, x, y, null);
				break;
			case DOWN :
				g.drawImage(ResourceMgr.bulletD, x, y, null);
				break;
			case RIGHT :
				g.drawImage(ResourceMgr.bulletR, x, y, null);
				break;
		}
		move();
	}
	
	/**
	 * 子弹移动
	 */
	private void move() {
		//根据子弹方向
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
		
		rectBullet.x = this.x;
		rectBullet.y = this.y;
		
		//根据子弹位置，判断子弹状态
		if(x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
			living = false;
		}
	}

	public boolean isLiving() {
		return living;
	}

	/**
	 * 检测子弹与敌军坦克是否碰撞
	 * 如果碰撞了，那么坦克死，子弹死
	 * @param tank
	 */
	public void collideWith(Tank tank) {
		//好友互伤模式关闭
		if(this.group.equals(tank.getGroup())) {
			return;
		}
		//子弹
		//Rectangle rect1 = new Rectangle(this.x,this.y,WIDTH,HEIGHT);
		//坦克
		//Rectangle rect2 = new Rectangle(tank.getX(),tank.getY(),tank.WIDTH,tank.HEIGHT);
		
		if(rectBullet.intersects(tank.rectTank)) {
			//如果碰撞，那么子弹死，坦克死
			this.die();
			tank.die();
			
			//爆炸位置
			int eX = tank.getX() + tank.WIDTH / 2 - Explode.WIDTH / 2;
			int eY = tank.getY() + tank.HEIGHT / 2 - Explode.HEIGHT / 2;
			//碰撞了就创建爆炸
			gm.explodes.add(new Explode(eX,eY,gm));
		}
	}

	/**
	 * 子弹死
	 */
	private void die() {
		this.living = false;
	}
	
}

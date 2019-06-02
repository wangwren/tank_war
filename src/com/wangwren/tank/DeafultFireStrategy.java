package com.wangwren.tank;
/**
 * 默认开火策略，一下打出一发子弹
 * @author wwr
 *
 */
public class DeafultFireStrategy implements FireStrategy{

	@Override
	public void fire(Tank tank) {
		//tf.b = new Bullet(x, y, dir);
		//计算出子弹位置
		int bX = tank.getX() + tank.WIDTH / 2 - Bullet.WIDTH / 2;
		int bY = tank.getY() + tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
		//创建的子弹跟随坦克的方向和分组
		new Bullet(bX, bY, tank.getDir(),tank.getGroup(),tank.tf);
		
		//自己的坦克开火声音
		if(tank.getGroup() == Group.GOOD) new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
	}
}

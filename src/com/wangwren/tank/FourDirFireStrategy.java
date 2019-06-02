package com.wangwren.tank;

/**
 * 四个方向的开火策略
 * @author wwr
 *
 */
public class FourDirFireStrategy implements FireStrategy {

	@Override
	public void fire(Tank tank) {
		//计算出子弹位置
		int bX = tank.getX() + tank.WIDTH / 2 - Bullet.WIDTH / 2;
		int bY = tank.getY() + tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
		//获取方向，每个方向上都创建一个子弹
		Dir[] dirs = Dir.values();
		for(Dir dir : dirs) {
			new Bullet(tank.getX(), tank.getY(), dir, tank.getGroup(), tank.gm);
		}
				
		//自己的坦克开火声音
		if(tank.getGroup() == Group.GOOD) new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
	}

}

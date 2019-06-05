package com.wangwren.tank.cor;

import com.wangwren.tank.Bullet;
import com.wangwren.tank.Explode;
import com.wangwren.tank.GameObject;
import com.wangwren.tank.Tank;
import com.wangwren.tank.Wall;

/**
 * 坦克和墙的碰撞检测
 * @author wwr
 *
 */
public class TankWallCollider implements Collider {

	/**
	 * @return true:继续执行别的碰撞；false：不碰撞了，已经死了。
	 */
	@Override
	public boolean collide(GameObject go1, GameObject go2) {

		//判断传来的go1和go2是否是子弹和坦克
		if(go1 instanceof Tank && go2 instanceof Wall) {
			
			//将go1和go2强转成对应的对象
			Tank tank = (Tank)go1;
			Wall wall = (Wall)go2;
			
			//坦克和墙相撞，坦克回退
			if(tank.getRectTank().intersects(wall.getRect())) {
				tank.back();
			}
		}else if(go1 instanceof Wall && go2 instanceof Tank) {
			collide(go2, go1);
		}
		
		return true;
	}

}

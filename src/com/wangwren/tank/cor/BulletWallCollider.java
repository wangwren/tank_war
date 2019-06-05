package com.wangwren.tank.cor;

import com.wangwren.tank.Bullet;
import com.wangwren.tank.Explode;
import com.wangwren.tank.GameObject;
import com.wangwren.tank.Tank;
import com.wangwren.tank.Wall;

/**
 * 子弹和墙的碰撞检测
 * @author wwr
 *
 */
public class BulletWallCollider implements Collider {

	/**
	 * 就是之前在子弹类中的collideWith方法。
	 * @return true:继续执行别的碰撞；false：不碰撞了，已经死了。
	 */
	@Override
	public boolean collide(GameObject go1, GameObject go2) {

		//判断传来的go1和go2是否是子弹和坦克
		if(go1 instanceof Bullet && go2 instanceof Wall) {
			
			//将go1和go2强转成对应的对象
			Bullet bullet = (Bullet)go1;
			Wall wall = (Wall)go2;
			
			//子弹和墙相撞，子弹死
			if(bullet.getRectBullet().intersects(wall.getRect())) {
				bullet.die();
			}
		}else if(go1 instanceof Wall && go2 instanceof Bullet) {
			collide(go2, go1);
		}
		
		return true;
	}

}

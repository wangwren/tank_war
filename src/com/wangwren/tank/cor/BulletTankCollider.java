package com.wangwren.tank.cor;

import com.wangwren.tank.Bullet;
import com.wangwren.tank.Explode;
import com.wangwren.tank.GameModel;
import com.wangwren.tank.GameObject;
import com.wangwren.tank.Tank;

/**
 * 子弹和坦克的碰撞检测
 * @author wwr
 *
 */
public class BulletTankCollider implements Collider {

	/**
	 * 就是之前在子弹类中的collideWith方法。
	 * @return true:继续执行别的碰撞；false：不碰撞了，已经死了。
	 */
	@Override
	public boolean collide(GameObject go1, GameObject go2) {

		//判断传来的go1和go2是否是子弹和坦克
		if(go1 instanceof Bullet && go2 instanceof Tank) {
			
			//将go1和go2强转成对应的对象
			Bullet bullet = (Bullet)go1;
			Tank tank = (Tank)go2;
			
			//好友互伤模式关闭
			if(bullet.getGroup().equals(tank.getGroup())) {
				return true;
			}
			//子弹
			//Rectangle rect1 = new Rectangle(this.x,this.y,WIDTH,HEIGHT);
			//坦克
			//Rectangle rect2 = new Rectangle(tank.getX(),tank.getY(),tank.WIDTH,tank.HEIGHT);
			
			if(bullet.getRectBullet().intersects(tank.getRectTank())) {
				//如果碰撞，那么子弹死，坦克死
				tank.die();
				bullet.die();
				
				//爆炸位置
				int eX = tank.getX() + tank.WIDTH / 2 - Explode.WIDTH / 2;
				int eY = tank.getY() + tank.HEIGHT / 2 - Explode.HEIGHT / 2;
				//碰撞了就创建爆炸
				GameModel.getInstance().addGameObject(new Explode(eX,eY));
				
				return false;
			}
		}else if(go1 instanceof Tank && go2 instanceof Bullet) {
			collide(go2, go1);
		}
		
		return true;
	}

}

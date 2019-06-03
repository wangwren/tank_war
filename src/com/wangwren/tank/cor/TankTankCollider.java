package com.wangwren.tank.cor;

import com.wangwren.tank.GameObject;
import com.wangwren.tank.Tank;

/**
 * 坦克和坦克的碰撞检测
 * 
 * 如果坦克和坦克碰撞上了，坦克移动到碰撞之前的位置
 * @author wwr
 *
 */
public class TankTankCollider implements Collider {

	@Override
	public boolean collide(GameObject go1, GameObject go2) {
		if(go1 instanceof Tank && go2 instanceof Tank) {
			Tank tank1 = (Tank)go1;
			Tank tank2 = (Tank)go2;
			if(tank1.getRectTank().intersects(tank2.getRectTank())) {
				//tank1.tankStop();
				//坦克碰撞之后，移动到上一次的位置
				tank1.setX(tank1.getOldX());
				tank1.setY(tank1.getOldY());
				
				tank2.setX(tank2.getOldX());
				tank2.setOldY(tank2.getOldY());
			}
		}
		return true;
	}

}

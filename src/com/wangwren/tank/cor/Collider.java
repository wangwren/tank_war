package com.wangwren.tank.cor;

import com.wangwren.tank.GameObject;

/**
 * 碰撞检测接口
 *  定义一个碰撞检测方法，检测两个物体是否碰撞
 * @author wwr
 *
 */
public interface Collider {

	public boolean collide(GameObject go1, GameObject go2);
}

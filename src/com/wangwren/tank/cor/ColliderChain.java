package com.wangwren.tank.cor;

import java.util.LinkedList;
import java.util.List;

import com.wangwren.tank.GameObject;
import com.wangwren.tank.PropertyMgr;

/**
 * 责任链
 * 将所有的碰撞连在一起
 * @author wwr
 *
 */
public class ColliderChain implements Collider {

	private List<Collider> colliders = new LinkedList<Collider>();
	
	//初始化时，将所有的Collider添加到链中
	public ColliderChain() {
//		add(new BulletTankCollider());
//		add(new TankTankCollider());
		//使用配置文件，获取碰撞检测。用英隔开文逗号
		String colliders = (String) PropertyMgr.getProp("colliders");
		String[] collidersArr = colliders.split(",");
		for(String colliderStr : collidersArr) {
			try {
				Collider collider = (Collider) ColliderChain.class.forName(colliderStr).newInstance();
				add(collider);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void add(Collider collider) {
		colliders.add(collider);
	}
	
	@Override
	public boolean collide(GameObject go1, GameObject go2) {
		for(int i = 0; i < colliders.size(); i++) {
			if(!colliders.get(i).collide(go1, go2)) {
				return false;
			}
		}
		return true;
	}

}

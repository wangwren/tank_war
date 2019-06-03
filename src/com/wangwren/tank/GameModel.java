package com.wangwren.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * 门面，用来存放TankFrame中的物体，封装好直接给TankFrame画就行了
 * 
 * @author wwr
 *
 */
public class GameModel {

	// 创建一个坦克，在200,200位置，方向朝下
	Tank myTank = new Tank(200, 600, Dir.DOWN, Group.GOOD, this);
//	// 加入子弹
//	List<Bullet> bullets = new ArrayList<>();
//	// 加入敌军坦克
//	List<Tank> tanks = new ArrayList<Tank>();
//	// 加入爆炸
//	List<Explode> explodes = new ArrayList<Explode>();
	
	//只需要有一个物体的共同父类就可以了
	private List<GameObject> gameObjects = new ArrayList<>();

	public GameModel() {
		// 敌军坦克数量
		int initTankCount = Integer.parseInt((String) PropertyMgr.getProp("initTankCount"));

		// 创建敌军坦克
		for (int i = 0; i < initTankCount; i++) {
			addGameObject(new Tank(100 + i * 80, 300, Dir.DOWN, Group.BAD, this));
		}
	}
	
	/**
	 * 向gameObjects中添加物体
	 */
	public void addGameObject(GameObject gameObject) {
		this.gameObjects.add(gameObject);
	}
	
	public void removeGameObject(GameObject gameObject) {
		this.gameObjects.remove(gameObject);
	}

	public void paint(Graphics g) {
//		Color color = g.getColor();
//		g.setColor(Color.WHITE);
//		g.drawString("屏幕中子弹数量:" + bullets.size(), 10, 50);
//		g.drawString("屏幕坦克数量:" + tanks.size(), 10, 70);
//		g.drawString("屏幕中爆炸数量" + explodes.size(), 10, 90);
//		g.setColor(color);

		// 让坦克自己把自己画出来，只有自己知道应该画在哪
		myTank.paint(g);
		
		//调用GameObject的画方法
		for(int i = 0; i < gameObjects.size(); i ++) {
			gameObjects.get(i).paint(g);
		}
		
		//碰撞检测需要使用责任链模式，这里就是写策略模式，之后将其连接起来
		
		// b.paint(g);
		// 方式一：画出子弹，使用循环，注意循环的使用方式
//		for (int i = 0; i < bullets.size(); i++) {
//			bullets.get(i).paint(g);
//		}

		/*
		 * 方式二：使用Iterator迭代删除 不可以使用foreach迭代。因为调用了paint方法，该方法中有判断，调用了remove方法。会报错。
		 * for(Iterator<Bullet> iterator = bullets.iterator() ; iterator.hasNext() ;) {
		 * Bullet bullet = iterator.next(); bullet.paint(g); if(!bullet.isLiving()) {
		 * iterator.remove(); } }
		 */

		// 画出敌军坦克
//		for (int i = 0; i < tanks.size(); i++) {
//			tanks.get(i).paint(g);
//		}

		// 子弹与敌军坦克碰撞检测.
		// 每次重画都检测一下每一个子弹与敌军坦克是否碰撞了。
//		for (int i = 0; i < bullets.size(); i++) {
//			for (int j = 0; j < tanks.size(); j++) {
//				// 检测
//				bullets.get(i).collideWith(tanks.get(j));
//			}
//		}

		// 画爆炸
//		for (int i = 0; i < explodes.size(); i++) {
//			explodes.get(i).paint(g);
//		}
	}

	/**
	 * 获取主站坦克
	 * 
	 * @return
	 */
	public Tank getMainTank() {
		return myTank;
	}
}

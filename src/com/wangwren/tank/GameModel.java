package com.wangwren.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.wangwren.tank.cor.BulletTankCollider;
import com.wangwren.tank.cor.Collider;
import com.wangwren.tank.cor.ColliderChain;
import com.wangwren.tank.cor.TankTankCollider;

/**
 * 门面，用来存放TankFrame中的物体，封装好直接给TankFrame画就行了
 * 
 * @author wwr
 *
 */
public class GameModel {

	// 创建一个坦克，在200,200位置，方向朝下
	Tank myTank = null;
	
	//只需要有一个物体的共同父类就可以了
	private List<GameObject> gameObjects = new ArrayList<>();
	
	private ColliderChain colliderChain = new ColliderChain();
	
	private static final GameModel INSTANCE = new GameModel();
	
	static {
		//防止循环new，因为改成单例了，就直接获取GameModel对象就可以了，但是这样在new坦克的时候又要new INSTANCE即GameModel
		//所以单写一个方法，在类加载时初始化
		INSTANCE.init();
	}

	private GameModel() {
		
	}
	
	public static GameModel getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 初始化
	 */
	public void init() {
		//在坦克的构造方法中，会将坦克加入至GameObject中，主坦克也会加入，这样就会导致主坦克也会被打死
		myTank = new Tank(200, 600, Dir.DOWN, Group.GOOD);
		// 敌军坦克数量
		int initTankCount = Integer.parseInt((String) PropertyMgr.getProp("initTankCount"));

		// 创建敌军坦克
		for (int i = 0; i < initTankCount; i++) {
			new Tank(50 + i * 80, 300, Dir.DOWN, Group.BAD);
		}
				
		// 初始化墙
		new Wall(150, 150, 200, 50);
		new Wall(550, 150, 200, 50);
		new Wall(300, 300, 50, 200);
		new Wall(550, 300, 50, 200);
	}
	
	/**
	 * 向gameObjects中添加物体
	 */
	public void addGameObject(GameObject gameObject) {
		this.gameObjects.add(gameObject);
	}
	
	public void removeGameObject(GameObject gameObject) {
//		if(gameObject instanceof Tank) {
//			myTank = null;
//		}
		this.gameObjects.remove(gameObject);
	}

	public void paint(Graphics g) {
//		Color color = g.getColor();
//		g.setColor(Color.WHITE);
//		g.drawString("屏幕中子弹数量:" + bullets.size(), 10, 50);
//		g.drawString("屏幕坦克数量:" + tanks.size(), 10, 70);
//		g.drawString("屏幕中爆炸数量" + explodes.size(), 10, 90);
//		g.setColor(color);

		// 让坦克自己把自己画出来，只有自己知道应该画在哪,将GameModel改成单例后，主坦克可以不用自己画了。
		myTank.paint(g);
		
		//调用GameObject的画方法
		for(int i = 0; i < gameObjects.size(); i ++) {
			gameObjects.get(i).paint(g);
		}
		
		//碰撞检测需要使用责任链模式，这里就是写策略模式，之后将其连接
		for(int i = 0; i < gameObjects.size(); i ++) {
			for(int j = i + 1; j < gameObjects.size(); j++) {
//				collider.collide(gameObjects.get(i), gameObjects.get(j));
//				collider2.collide(gameObjects.get(i), gameObjects.get(j));
				
				//使用责任链，将每一个物体都走每个链
				//这里虽然使用的是责任链的collider方法，但是该方法又便利了链中的具体Collider类，所以每个链都会去检测
				colliderChain.collide(gameObjects.get(i), gameObjects.get(j));
			}
		}
		
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

package com.wangwren.tank.net;

public enum MsgType {

	/**
	 * 坦克加入
	 * 坦克方向改变
	 * 坦克停止
	 * 坦克开始移动
	 * 发射子弹
	 * 坦克死
	 */
	TankJoin, TankDirChanged, TankStop, TankStartMoving, BulletNew, TankDie
}

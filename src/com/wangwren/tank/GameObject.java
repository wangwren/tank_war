package com.wangwren.tank;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * 用于存放物体的共有属性和方法，所有的物体都需要继承该类，比如坦克、子弹等
 * 
 * 注意：并没有把我方坦克，即好坦克放进来
 * @author wwr
 *
 */
public abstract class GameObject implements Serializable {

	//位置,子类可见
	protected int x;
	protected int y;
	
	/**
	 * 共有的 画 方法，所有的物体都需要自己把自己画出来
	 * @param g
	 */
	public abstract void paint(Graphics g);
}

package com.wangwren.tank;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 爆炸类
 * @author wwr
 *
 */
public class Explode {

	//爆炸位置
	private int x;
	private int y;
	
	//爆炸大小
	public static int WIDTH = ResourceMgr.explodes[0].getWidth();
	public static int HEIGHT = ResourceMgr.explodes[0].getHeight();
	
	//GameModel的引用
	private GameModel gm = null;
	
	//爆炸状态，true表示活着；false表示屎了
	private boolean living = true;
	
	//标识爆炸图片画到第几个
	private int step = 0;

	public Explode(int x, int y, GameModel gm) {
		this.x = x;
		this.y = y;
		this.gm = gm;
	}

	/**
	 * 爆炸把自己画出来，每一次paint，step都+1
	 * @param g
	 */
	public void paint(Graphics g) {
		g.drawImage(ResourceMgr.explodes[step++], this.x, this.y, null);
		
		if(step >= 15) {
			//step = 0;
			//爆炸图片显示完成后，从集合中移除
			gm.explodes.remove(this);
		}
	}
	
}

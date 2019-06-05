package com.wangwren.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 墙物体
 * @author wwr
 *
 */
public class Wall extends GameObject {

	
	int w , h;
	public Rectangle rect;
	
	public Wall(int x,int y,int w,int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		rect = new Rectangle(x,y,w,h);
		
		GameModel.getInstance().addGameObject(this);
	}
	
	@Override
	public void paint(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, w, h);
		g.setColor(color);
	}

	public Rectangle getRect() {
		return rect;
	}

}

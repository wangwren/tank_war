package com.wangwren.tank;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TankFrame extends Frame {

	public static final int GAME_WIDTH = 1080;
	public static final int GAME_HEIGHT = 960;
	
	//创建一个坦克，在200,200位置，方向朝下
	Tank myTank = new Tank(200, 600, Dir.DOWN,Group.GOOD,this);
	//加入子弹
	List<Bullet> bullets = new ArrayList<>();
	//加入敌军坦克
	List<Tank> tanks = new ArrayList<Tank>();
	//加入爆炸
	List<Explode> explodes = new ArrayList<Explode>();
	
	
	public TankFrame() {
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		
		//不允许窗口改变大小
		this.setResizable(false);
		//设置标题
		this.setTitle("坦克大战");
		
		//键盘事件
		this.addKeyListener(new MyKeyListener());
		
		//关闭窗口事件
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
		//自己坦克默认不动
		myTank.setMoving(false);
	}
	
	/**
	 * 双缓冲解决闪烁问题，一般这种问题在游戏引擎中会内嵌。
	 * repaint方法会先执行update方法。
	 * 截获update方法，将Image中的画笔拿到，画出图形，并调用paint方法，并传入Image的画笔，此时将画出坦克和子弹
	 * 之后，再使用update中参数的画笔g，即系统中的画笔，将Image整个画出，显示在显存中。
	 */
	Image offScreenImage = null;
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/**
	 * 画
	 * 该方法只有在第一次出现和窗口重新加载时会被自动调用
	 * 比如窗口最小化之后又显示出来，就会被调用
	 * 或者，调用repaint()方法，该方法又会调用paint()方法
	 * 
	 * Graphics g 参数是画笔
	 */
	@Override
	public void paint(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("屏幕中子弹数量:" + bullets.size(), 10, 50);
		g.drawString("屏幕坦克数量:" + tanks.size(), 10, 70);
		g.drawString("屏幕中爆炸数量" + explodes.size(), 10, 90);
		g.setColor(color);
		
		//让坦克自己把自己画出来，只有自己知道应该画在哪
		myTank.paint(g);
		//b.paint(g);
		//方式一：画出子弹，使用循环，注意循环的使用方式
		for(int i = 0 ; i < bullets.size() ; i++) {
			bullets.get(i).paint(g);
		}
		
		/*
		 * 方式二：使用Iterator迭代删除
		 * 不可以使用foreach迭代。因为调用了paint方法，该方法中有判断，调用了remove方法。会报错。
		 * for(Iterator<Bullet> iterator = bullets.iterator() ; iterator.hasNext() ;) {
			Bullet bullet = iterator.next();
			bullet.paint(g);
			if(!bullet.isLiving()) {
				iterator.remove();
			}
		}*/
		
		//画出敌军坦克
		for(int i = 0 ; i < tanks.size() ; i ++) {
			tanks.get(i).paint(g);
		}
		
		//子弹与敌军坦克碰撞检测.
		//每次重画都检测一下每一个子弹与敌军坦克是否碰撞了。
		for(int i = 0 ; i < bullets.size() ; i ++) {
			for(int j = 0 ; j < tanks.size() ; j ++) {
				//检测
				bullets.get(i).collideWith(tanks.get(j));
			}
		}
		
		//画爆炸
		for(int i = 0 ; i < explodes.size() ; i ++) {
			explodes.get(i).paint(g);
		}
	}
	
	//内部类，键盘按下事件
	class MyKeyListener extends KeyAdapter{
		boolean bl = false;
		boolean bu = false;
		boolean br = false;
		boolean bd = false;
		
		//键盘按下事件
		@Override
		public void keyPressed(KeyEvent e) {
			//x += 10;
			//重画，该方法是Frame中的方法
			//repaint();
			
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			//按了方向键左
			case KeyEvent.VK_LEFT:
				bl = true;
				break;
			//按了方向键上
			case KeyEvent.VK_UP:
				bu = true;			
				break;
			//按了方向键右	
			case KeyEvent.VK_RIGHT:
				br = true;
				break;
			//按了方向键下
			case KeyEvent.VK_DOWN:
				bd = true;
				break;
			}
			
			//通过四个boolean值来确定坦克的方向
			setMainTankDir();
			
			//按下键盘，坦克移动的声音
			new Thread(()->new Audio("audio/tank_move.wav").play()).start();
		}

		//键盘松开事件,增加ctrl键打出炮弹
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
				//按了方向键左
				case KeyEvent.VK_LEFT:
					bl = false;
					break;
				//按了方向键上
				case KeyEvent.VK_UP:
					bu = false;			
					break;
				//按了方向键右	
				case KeyEvent.VK_RIGHT:
					br = false;
					break;
				//按了方向键下
				case KeyEvent.VK_DOWN:
					bd = false;
					break;
				//按下了ctrl键,抬起来时触发
				case KeyEvent.VK_CONTROL:
					myTank.fire();
					break;
			}
			
			//通过四个boolean值来确定坦克的方向
			setMainTankDir();
		}
		
		/**
		 * 通过四个Boolean值来确定坦克方向，和判断坦克是否静止
		 */
		private void setMainTankDir() {
			
			if(!bl && !bu && !br && !bd) {
				//如果四个键都没按，那么坦克静止
				myTank.setMoving(false);
			}else {
				//四个键中有一个为true，那么坦克就应该跑起来
				myTank.setMoving(true);
				//判断坦克方向
				if(bl) {
					myTank.setDir(Dir.LEFT);
				}
				if(bu) {
					myTank.setDir(Dir.UP);
				}
				if(br) {
					myTank.setDir(Dir.RIGHT);
				}
				if(bd) {
					myTank.setDir(Dir.DOWN);
				}
			}
		}
	}
	
}

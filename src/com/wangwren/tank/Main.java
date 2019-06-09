package com.wangwren.tank;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		TankFrame f = new TankFrame();	
		
		//敌军坦克数量
//		int initTankCount = Integer.parseInt((String)PropertyMgr.getProp("initTankCount"));
//		
//		//创建五个敌军坦克
//		for(int i = 0 ; i < initTankCount ; i ++) {
//			f.tanks.add(new Tank(100 + i * 80,300,Dir.DOWN,Group.BAD,f));
//		}
		
//		for(int i = 0 ; i < 10 ; i ++) {
//			new Thread(() -> {
//				for(int j = 0 ; j < 5 ; j ++) {
//					f.tanks.add(new Tank(100 + j * 80,300,Dir.DOWN,Group.BAD,f));
//				}
//			}).start();;
//		}
		
		//背景音乐
		new Thread(()->new Audio("audio/war1.wav").loop()).start();
		while(true) {
			//死循环，一直执行
			Thread.sleep(25);
			//一直调用重画方法
			f.repaint();
		}
	}
}

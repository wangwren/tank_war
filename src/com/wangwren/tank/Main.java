package com.wangwren.tank;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		TankFrame f = new TankFrame();	
		
		
		
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

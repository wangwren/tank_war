package com.wangwren.tank;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 加载图片
 * @author wwr
 *
 */
public class ResourceMgr {

	//好坦克图片
	public static BufferedImage goodTankL;
	public static BufferedImage goodTankU;
	public static BufferedImage goodTankD;
	public static BufferedImage goodTankR;
	
	//坏坦克图片
	public static BufferedImage badTankL;
	public static BufferedImage badTankU;
	public static BufferedImage badTankD;
	public static BufferedImage badTankR;
	
	//子弹图片
	public static BufferedImage bulletL;
	public static BufferedImage bulletU;
	public static BufferedImage bulletD;
	public static BufferedImage bulletR;
	
	//爆炸图片
	public static BufferedImage[] explodes = new BufferedImage[16];
	
	static {
		try {
			//读取classpath下的图片，通过classLoader来读取，ResourceMgr也可以换成别的类
			//ResourceMgr.class.getClassLoader 必须要调用getClassLoader，获取到类加载器，才能获取到资源文件
			goodTankU = ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/GoodTank1.png"));
			goodTankL = ImageUtil.rotateImage(goodTankU, -90);
			goodTankD = ImageUtil.rotateImage(goodTankU, 180);
			goodTankR = ImageUtil.rotateImage(goodTankU, 90);
			
			badTankU = ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/BadTank1.png"));
			badTankL = ImageUtil.rotateImage(badTankU, -90);
			badTankD = ImageUtil.rotateImage(badTankU, 180);
			badTankR = ImageUtil.rotateImage(badTankU, 90);
			
			bulletU = ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/bulletU.png"));
			bulletL = ImageUtil.rotateImage(bulletU, -90);
			bulletD = ImageUtil.rotateImage(bulletU, 180);
			bulletR = ImageUtil.rotateImage(bulletU, 90);
			
			for(int i = 0 ; i < explodes.length ; i ++) {
				explodes[i] = ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("images/e" + (i + 1) + ".gif"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

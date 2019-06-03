package com.wangwren.tank.strategy;

import com.wangwren.tank.Tank;

/**
 * 定义坦克开火的策略
 * @author wwr
 *
 */
public interface FireStrategy {
	void fire(Tank tank);
}

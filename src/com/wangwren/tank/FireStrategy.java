package com.wangwren.tank;

/**
 * 定义坦克开火的策略
 * @author wwr
 *
 */
public interface FireStrategy {
	void fire(Tank tank);
}

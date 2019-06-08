package com.wangwren.tank.strategy;

import java.io.Serializable;

import com.wangwren.tank.Tank;

/**
 * 定义坦克开火的策略
 * @author wwr
 *
 */
public interface FireStrategy extends Serializable {
	void fire(Tank tank);
}

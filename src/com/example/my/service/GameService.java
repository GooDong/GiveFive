package com.example.my.service;

import java.util.List;

import com.example.my.bead.Bead;


import android.graphics.Point;

/**
 * 游戏业务操作接口
 * @author 石广洞
 */
public interface GameService {
	/** 获取下一轮要显示的三个珠子 */
	List<Bead> getPreparedBeads();
	/**
	 * 根据用户点击的坐标获取相对应的珠子
	 * @param x 坐标
	 * @param y 坐标
	 * @return Bead
	 */
	Bead getSelectedBead(float x, float y);
	/**
	 * 获取两个珠子可走的线路
	 * @param selectedBead 选中的珠子
	 * @param targetBead 目标的珠子
	 * @return 可走线路点的集合
	 */
	List<Point> getPath(Bead selectedBead, Bead targetBead);
	/**
	 * 获取要显示的三个珠子
	 * @return 珠子集合
	 */
	List<Bead> getDisplayBeads();
	/**
	 * 扫描珠子的方法
	 * @return
	 */
	boolean scanBead(int scanType);
	/** 
	 * 获取分数(每次的得分) 
	 */
	int getPerScore();
	/** 
	 * 获取可消珠子的连接点 
	 */
	List<Point> getLinkPoints();
	/** 
	 * 设置游戏累计分数 
	 */
	void setTotalScore();
	/**
	 * 清除可消珠子
	 */
	void clearBead();
	/** 
	 * 获取珠盘上所有的空珠子 
	 */
	List<Bead> getEmptyBeads();
	/**
	 * 游戏重新开始的方法
	 */
	void reset();
	
}

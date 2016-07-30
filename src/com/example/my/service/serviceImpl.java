package com.example.my.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Point;

import com.example.my.arithmetic.PathArithmetic;
import com.example.my.arithmetic.ScanArithmetic;
import com.example.my.bead.Bead;
import com.example.my.bead.BeadBord;
import com.example.my.utils.BitMapUtil;
import com.example.my.utils.Constant;

/**
 * 游戏业务接口实现类
 * @author 石广洞
 * @since 2015-11-1
 * */
public class serviceImpl implements  GameService{
	/**Activity上下文*/
	private Context context;
	/**珠盘实体*/
	private BeadBord beadBord;
	/**定义随机对象*/
	private Random rand = new Random(47);
	/**定义下一轮要显示的三个珠子*/
	private List<Bead> preparedBeads = new ArrayList<Bead>();
	/**定义缓存分数的属性*/
	private int perScore = 0;
	/**定义可消珠子的点的集合*/
	private List<Point> linkPoints = new ArrayList<Point>();
	
	public serviceImpl(Context contextIn ,BeadBord  beadBordIn){
		this.context = contextIn;
		this.beadBord  = beadBordIn;
		//调用初始化方法，初始化珠盘上的五个珠子
		this.init();
	}
	/**获取下一轮要显示的三个珠子*/
	@Override
	public List<Bead> getPreparedBeads() {
		return preparedBeads;
	}
	private void setPreparedBeads(){
		preparedBeads.clear();
		for (int i = 0; i < Constant.PER_NUM; i++) {
			Bead bead = BitMapUtil.randomBead(context, beadBord.scale);
			preparedBeads.add(bead);
		}
	}
	/**
	 * 根据用户点击坐标获取相对应的珠子
	 * @param x 坐标，y 坐标、
	 * @return Bead;
	 * */
	@Override
	public Bead getSelectedBead(float x, float y) {
		if(y < beadBord.topImage.getHeight() + Constant.TOP_BUTTOM_SPACE*beadBord.scale){
			return null;
		}
		if(y > beadBord.topImage.getHeight() + beadBord.boardImage.getHeight()
				-Constant.TOP_BUTTOM_SPACE*beadBord.scale){
			return null;
		}
		//把x，y坐标转化为二维数组中的i与j
		int i = Float.valueOf((x -Constant.LEFT_RIGHT_SPACE*beadBord.scale)/beadBord.gridWidth).intValue();
		int j= Float.valueOf((y -beadBord.topImage.getHeight()-Constant.TOP_BUTTOM_SPACE*beadBord.scale)/beadBord.gridHeight).intValue();
		//对i的有效性放松
		if(i >= 9 ){
			i = Constant.BEAD_SIZE - 1;
		}
		if(i >= 0 && i < Constant.BEAD_SIZE && j >= 0 && j < Constant.BEAD_SIZE ){
			return beadBord.beads[i][j];
		}
		return null;
	}

	@Override
	public List<Point> getPath(Bead selectedBead, Bead targetBead) {
		if(selectedBead == null || targetBead == null){
			return null;
		}
		Point from = new Point(selectedBead.index_x,selectedBead.index_y);
		Point to = new Point(targetBead.index_x,targetBead.index_y);
		//从起点到终点
		List<Point> points = PathArithmetic.getInstance().getPath(from, to, beadBord.beads);
		if(points.size() <= 5){
			//反转集合中的元素
			//递归查询中，最后一个元素最先添加到集合中，因而需要翻转
			Collections.reverse(points);
			return points;
		}else{
			//从终点到起点
			List<Point> tempPoints = PathArithmetic.getInstance().getPath(to, from, beadBord.beads);
			if(points.size() < tempPoints.size() ){
				//反转集合中的元素
				//递归查询中，最后一个元素最先添加到集合中，因而需要翻转
				Collections.reverse(points);
				return points;
			}else{
				//递归查询中，最后一个元素最先添加到集合中。
				tempPoints.remove(from);
				tempPoints.add(to);
				return tempPoints;
			}
			
		}
		
	}
	/**
	 * 获取要显示的三个珠子
	 * @return 珠子集合
	 * */
	@Override
	public List<Bead> getDisplayBeads() {
		// 预加载的三个珠子（小珠盘上的三个珠子）
		List<Bead> preparedBeads = this.getPreparedBeads();
		//获取珠盘上所有的空珠子
		List<Bead> emptyBeads = this.getEmptyBeads();
		//判断珠盘上空珠子的数量（不能小于3）
		if(emptyBeads.size() < 3){
			return null;
		}
		List<Bead> lists = new ArrayList<Bead>();
		for (Bead prebead : preparedBeads) {
			int cursor = rand.nextInt(emptyBeads.size());
			Bead bead = emptyBeads.remove(cursor);
			//组合两个珠子
			prebead.index_x = bead.index_x;
			prebead.index_y = bead.index_y;
			lists.add(prebead);
		}
		//重新加载三个珠子（下一轮）
		this.setPreparedBeads();
		return lists;
	}
	/**
	 * 扫描珠子的方法
	 * @return
	 * */
	@Override
	public boolean scanBead(int scanType) {
		linkPoints.clear();
		perScore = 0;
		//从四个方位扫描
		List<List<Point>> lists = ScanArithmetic.scan(beadBord.beads);
		//判断是否珠子可消
		if(lists.isEmpty()){
			return false;
		}
		//统计一下一共有多少个珠子可以消
		int count = 0;
		//记录可消珠子的点
		for (List<Point> list : lists) {
			for (Point point : list) {
				if(!linkPoints.contains(point)){
					linkPoints.add(point);
				}
			}
			count += list.size();
		}
		//计算出分数（一个珠子2分）消的分数*方位
		if(scanType == Constant.CLEAR_SCAN_TYPE_1){
			//用户走珠子后消掉就算分数
			perScore = count * 2 * lists.size();
		}
		return true;
	}

	@Override
	public int getPerScore() {
		return this.perScore;
	}

	@Override
	public List<Point> getLinkPoints() {
		return this.linkPoints;
	}

	@Override
	public void setTotalScore() {
		beadBord.setTotleScore(this.perScore);
	}

	@Override
	public void clearBead() {
		for (Point p : linkPoints) {
			beadBord.beads[p.x][p.y].setBitmap(null);
		}
		linkPoints.clear();
	}

	@Override
	public List<Bead> getEmptyBeads() {
		List<Bead> emptyBeads = new ArrayList<Bead>();
		for (int i = 0; i < beadBord .beads.length; i++) {
			for (int j = 0; j < beadBord .beads.length; j++) {
				Bead bead = beadBord.beads[i][j];
				//只要珠子实体没有位图，那就是一个空珠
				if(bead.getBitmap() == null){
					emptyBeads.add(bead);
				}
			}
		}
		return emptyBeads;
	}
	/**游戏重新开始的方法*/
	@Override
	public void reset() {
		for (int i = 0; i < beadBord.beads.length; i++) {
			for (int j = 0; j < beadBord .beads.length; j++) {
				beadBord.beads[i][j].setBitmap(null);
			}
		}
		this.perScore = 0;
		this.setTotalScore();
		this.init();
	}
	private void init(){
		//获取所有的空珠子
		List<Bead> emptBeads = this.getEmptyBeads();
		//初始化五个珠子
		for (int i = 0; i < Constant.INIT_NUM; i++) {
			Bead temp = BitMapUtil.randomBead(context, beadBord.scale);
			//在珠盘上所有空珠子的地方随机索取一个珠子
			int cursor = rand.nextInt(emptBeads.size());
			Bead bead = emptBeads.remove(cursor);
			bead.setBitmap(temp.getBitmap());
			bead.beadColor = temp.beadColor;
		}
		//生成下一轮要显示的珠子
		this.setPreparedBeads();
	}
}









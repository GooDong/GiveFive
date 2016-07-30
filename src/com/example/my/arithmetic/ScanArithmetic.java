package com.example.my.arithmetic;

import java.util.ArrayList;
import java.util.List;

import com.example.my.bead.Bead;
import com.example.my.utils.Constant;

import android.graphics.Point;

/**
 * 珠子扫描工具类（横向、竖向、左斜、右斜）
 * @author 霜冻
 * @since 2015-11-2
 * */
public class ScanArithmetic {
	/**定义扫描的最终结果*/
	private static List<List<Point>> lists = new ArrayList<List<Point>>();
	
	/**
	 * 珠子四个方位的扫描方法
	 * @param
	 * @return
	 * */
	public static List<List<Point>> scan(Bead[][] beads){
		lists.clear();
		List<Point> points_1 = horizontalScan(beads);
		List<Point> points_2 = verticalScan(beads);
		List<Point> points_3 = leftSlantingScan(beads);
		List<Point> points_4 = rightSlantingScan(beads);
		if(points_1 != null && points_1.size() > 0){
			lists.add(points_1);
		}
		if(points_2 != null && points_2.size() > 0){
			lists.add(points_2);
		}
		if(points_3 != null && points_3.size() > 0){
			lists.add(points_3);
		}
		if(points_4 != null && points_4.size() > 0){
			lists.add(points_4);
		}
		return lists;
	} 
	/**
	 * 横向扫描
	 * @param beads
	 * @return
	 * */
	public static List<Point> horizontalScan(Bead[][] beads){
		for (int i = 0; i < beads.length; i++) {
			//定义变量来拼接这一行中的珠子的颜色
			StringBuilder color = new StringBuilder();
			//定义变量来缓存这一行中的珠子的对应的点的集合
			List<Point> points = new ArrayList<Point>();
			//扫描一行
			for (int j = 0; j < beads.length; j++) {
				Bead bead = beads[j][i];
				color.append(bead.beadColor);
				points.add(new Point(j,i));
			}
			//判断是否有五个或五个以上的同色珠子连在一起
			for(String str:Constant.FINAL_COLORS){
				//有五个同色珠子连在一起
				if(color.toString().contains(str)){
					//获取连在一起的五个或五个以上的珠子对应的点
					int begin = color.indexOf(str);
					int end = color.lastIndexOf(str)+str.length();
					List<Point> temps = new ArrayList<Point>();
					for(int k = begin ; k < end ;k++){
						temps.add(points.get(k));
					}
					return temps;
				}
			
			}
		}
		return null;
	}
	/**
	 * 纵向扫描
	 * @param
	 * @return
	 * */
	public static List<Point> verticalScan(Bead[][] beads){
		for (int i = 0; i < beads.length; i++) {
			//定义变量来拼接这一行中的珠子的颜色
			StringBuilder color = new StringBuilder();
			//定义变量来缓存这一行中的珠子的对应的点的集合
			List<Point> points = new ArrayList<Point>();
			//扫描一行
			for (int j = 0; j < beads.length; j++) {
				Bead bead = beads[i][j];
				color.append(bead.beadColor);
				points.add(new Point(i,j));
			}
			//判断是否有五个或五个以上的同色珠子连在一起
			for(String str : Constant.FINAL_COLORS){
				//有五个同色珠子连在一起
				if(color.toString().contains(str)){
					//获取连在一起的五个或五个以上的珠子对应的点
					int begin = color.indexOf(str);
					int end = color.lastIndexOf(str)+str.length();
					List<Point> temps = new ArrayList<Point>();
					for(int k = begin ; k < end ;k++){
						temps.add(points.get(k));
					}
					return temps;
				}
			}
		}
		return null;
	}
	/**
	 * 左斜扫描
	 * @param
	 * @return
	 * */
	public static List<Point> leftSlantingScan(Bead[][] beads){
		//找区间（0,4-0,0-4,0）
		for(int i = -4; i <= 4 ; i++){
			//定义变量来拼接这一行中珠子的颜色
			StringBuilder color = new StringBuilder();
			//定义变量来缓存这一行中的珠子对应的点的集合
			List<Point> points  = new ArrayList<Point>();
			if(i <= 0){
				//基于0,4点往右下扫描
				for(int j = 0; j < beads.length +i ;j++){
					Bead bead = beads[j][j-i];
					color.append(bead.beadColor);
					points.add(new Point(j,j-i));
				}
				
			}else{
				//基于8,7点往左扫描
				for (int j =  beads.length-1 ; j >= i; j--) {
					Bead bead =beads[j][j-i];
					color.append(bead.beadColor);
					points.add(new Point(j,j-i));
				}
			}
			// 判断是否有五个或五个以上的同色珠子连在一起
			for (String str : Constant.FINAL_COLORS){
				// 有五个同色的珠子连在一起
				if (color.toString().contains(str)){
					// 获取连在一起的五个或五个以上的珠子对应的点
					int begin = color.indexOf(str);
					int end = color.lastIndexOf(str) + str.length();
					List<Point> temps = new ArrayList<Point>();
					for (int k = begin; k < end; k++){
						temps.add(points.get(k));
					}
					return temps;
				}
			}
		}
		return null;
	}
	/**
	 * 右斜扫描
	 * @param beads
	 * @return
	 */
	private static List<Point> rightSlantingScan(Bead[][] beads){
		// 找区间(4,0-8,0-8,4)
		for (int i = 4; i <= 12; i++){
			
			// 定义变量来拼接这一行中珠子的颜色
			StringBuilder color = new StringBuilder();
			// 定义变量来缓存这一行中的珠子对应的点的集合
			List<Point> points = new ArrayList<Point>();
			if (i <= 8){ // 基于0,4点往右上扫
				for (int j = 0; j <= i; j++){
					Bead bead = beads[j][i - j];
					color.append(bead.beadColor);
					points.add(new Point(j, i - j));
				}
			}else{ // 基于8,1点往左下扫
				for (int j = beads.length - 1; j > i - beads.length; j--){
					// i = 12
					Bead bead = beads[j][i - j]; 
					color.append(bead.beadColor);
					points.add(new Point(j, i - j));
				}
			}
			// 判断是否有五个或五个以上的同色珠子连在一起
			for (String str : Constant.FINAL_COLORS){
				// 有五个同色的珠子连在一起
				if (color.toString().contains(str)){
					// 获取连在一起的五个或五个以上的珠子对应的点
					int begin = color.indexOf(str);
					int end = color.lastIndexOf(str) + str.length();
					List<Point> temps = new ArrayList<Point>();
					for (int k = begin; k < end; k++){
						temps.add(points.get(k));
					}
					return temps;
				}
			}
		}
		return null;
	}
	
}



























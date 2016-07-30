package com.example.my.utils;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.my.bead.Bead;



/**
 * 操作位图的工具类
 * @author 石广洞
 * @since 2015-11-1
 * */
public class BitMapUtil {
	//定义随机对象
	private static Random random = new Random();
	//定义Matrix矩阵类
	private static Matrix matrix = new Matrix();
	/**
	 * 根据资源文件获取位图
	 * @param resId 资源文件Id
	 * */
	public static Bitmap getBitmap(Context context,int resId){
		return BitmapFactory.decodeResource(context.getResources(), resId);
	}
	
	/**
	 * 随机生成一个珠子
	 * @param content 当前的Activity（上下文）
	 * @param scale 缩放比率
	 * @return 珠子的位图
	 * */
	public static Bead randomBead(Context context , float scale){
		//随机获取一个主意的图片资源id（0-5）
		int beadImageId = random.nextInt(Constant.BEAD_ICONS.length);
		//得到位图
		Bitmap source = getBitmap(context, Constant.BEAD_ICONS[beadImageId]);
		//对珠子按比例缩放
		matrix.setScale(scale,scale);
		//按比例缩放过后的位图
		source = Bitmap .createBitmap(source, 0, 0, source.getWidth(),source.getHeight(),matrix,true);
		Bead bead = new Bead();
		bead.setBitmap(source);
		bead.beadColor = Constant.BEAD_COLORS[beadImageId];
		return bead;
	}
	
	
	
}



















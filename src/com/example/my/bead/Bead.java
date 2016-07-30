package com.example.my.bead;

import android.graphics.Bitmap;



/**
 * 珠子实体
 * @author 石广洞
 * @since 2015-11-1
 * */
public class Bead {
	//在珠盘数组中坐标
	public int index_x;
	public int index_y;
	//珠子的颜色
	public String beadColor = "Z";
	//珠子的位图
	private Bitmap bitmapOfBead;
	/**
	 * 覆写equals方法,提供判断连个珠子相等的方法（只要两个珠子的index_x和index_y就代表同一个珠子）
	 * */
	@Override
	public boolean equals(Object beadIn) {
		if((beadIn == null) || !(beadIn instanceof Bead)){
			return false;
		}
		Bead beadTemp = (Bead)beadIn;
		//如果两个珠子的坐标一致，则代表同一颗珠子
		return beadTemp.index_x == this.index_x && beadTemp.index_y == this.index_y;
	}
	/**
	 * 位图的获得方法
	 * */
	public Bitmap getBitmap(){
		return bitmapOfBead;
	}
	/**
	 * 位图的设置方法,如果载入为空，则设置为默认值
	 * */
	public void setBitmap(Bitmap bitmapIn){
		if(bitmapIn == null){
			this.beadColor = "Z"; 
		}
		this.bitmapOfBead = bitmapIn;
	}
}




















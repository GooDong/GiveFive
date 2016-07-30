package com.example.my.utils;

import com.example.my.gonewithfive.R;


/**
 * 常量类
 * @author 石广洞
 * @since 2015-11-1
 * */
public class Constant {
	/**定义珠子的二维数组大小*/
	public static final int BEAD_SIZE = 9;
	/**定义网格左边、右边的间隙（一边）*/
	public static final float LEFT_RIGHT_SPACE = 6.5F;
	/**定义上边、下边的间隙（一边）*/
	public static final float TOP_BUTTOM_SPACE = 5.5F;
	/**定义初始化珠子的数量 */
	public static final int INIT_NUM = 5;
	
	/**定义六种珠子的资源文件的id*/
	public static final int[] BEAD_ICONS ={
		R.drawable.bang_1,
		R.drawable.bang_2,
		R.drawable.bang_3,
		R.drawable.bang_4,
		R.drawable.bang_5,
		R.drawable.bang_6
	};
	/**定义六种珠子的颜色*/
	public static final String[] BEAD_COLORS=
		{"A","B","C","D","E","F"};	
	/**定义六种珠子的可消颜色*/
	public static final String[] FINAL_COLORS=
		{"AAAAA","BBBBB","CCCCC","DDDDD","EEEEE","FFFFF"};
	/**定义每次生成的珠子的数量*/
	/**定义六种珠子的可消颜色*/
	public static final int PER_NUM = 3;
	/**定义缩放比率*/
	public static final float MATRIX_SCALE = 0.8F;
	
	/**定义珠子跳动的标识符与时长*/
	public static final long TRIP_TIMER_1 = 200;
	public static final int TRIP_FLAG_1=0X110;
	
	/**定义走珠子的标识符与时长*/
	public static final long MOVE_TIMER_2 = 100;
	public static final int MOVE_FLAG_2 = 0X111;
	
	/**定义显示珠子的标识符与时长*/
	public static final long SHOW_TIMER_3 = 100;
	public static final int SHOW_FLAG_3=0X112;
	
	/**定义消珠子的标识符与时长*/
	public static final long CLEAR_TIMER_4 = 300;
	public static final int CLEAR_FLAG_4=0X113;
	
	/**定义用户走完珠子要消珠子的标识符*/
	public static final int CLEAR_SCAN_TYPE_1 = 1; 
	/**定义系统生成了三个珠子要消除珠子的标识符*/
	public static final int CLEAR_SCAN_TYPE_2 = 2;
	/**定义音效的资源数组*/
	public static final int[] SOUNDS = {R.raw.selected,R.raw.error,R.raw.clear};
}























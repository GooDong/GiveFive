package com.example.my.bead;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.WindowManager;

import com.example.my.gonewithfive.R;
import com.example.my.utils.BitMapUtil;
import com.example.my.utils.Constant;

/**
 *珠盘实体类
 *@author 石广洞
 *@since 2015-11-1 
 * */
public class BeadBord {
	//网格宽度
	public float gridWidth;
	//网格高度
	public float gridHeight;
	//珠盘背景图
	public Bitmap boardImage;
	//珠盘图片与手机之间的缩放率
	public float scale;
	//珠子二维数组（珠盘上所有的珠子）
	public Bead[][] beads = new Bead[Constant.BEAD_SIZE][Constant.BEAD_SIZE];
	//小珠盘（放置下一轮要显示的三个珠子）
	public Bitmap topImage;
	//历史分数
	private int histScore;
	//本次游戏的总分数
	private int totalScore;
	
	public BeadBord(Context context){
		//通过context获取窗口管理器
		@SuppressWarnings("static-access")
		WindowManager wm = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
		//获取手机屏幕的宽度
		@SuppressWarnings("deprecation")
		int width = wm.getDefaultDisplay().getWidth();
		//把珠盘图片转化为位图
		Bitmap source = BitMapUtil.getBitmap(context, R.drawable.board);
		//计算出珠盘图片与手机之间的缩放率
		this.scale = Float.valueOf(width)/Float.valueOf(source.getWidth());
		//对珠盘图片进行缩放（按比率）
		Matrix  matrix  =  new Matrix();
		//设置缩放比率
		matrix.setScale(this.scale,this.scale);
		//得到缩放后的珠盘的位图
		this.boardImage = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),matrix,true);
		//计算网格的宽度
		this.gridWidth =(Float.valueOf(source.getWidth()-Constant.LEFT_RIGHT_SPACE*2)/Constant.BEAD_SIZE)*this.scale;
		this.gridHeight =(Float.valueOf(source.getHeight()-Constant.TOP_BUTTOM_SPACE*2)/Constant.BEAD_SIZE)*this.scale;
		//获取小珠盘对应的位图，小珠盘——>显示下一轮三个珠子的珠盘
		
		source = BitMapUtil.getBitmap(context, R.drawable.little_bord);
		//获得缩放过后的小珠盘的位图，小珠盘——>显示下一轮三个珠子的珠盘
		matrix.setScale(this.scale*Constant.MATRIX_SCALE,this.scale*Constant.MATRIX_SCALE);
		this.topImage = Bitmap.createBitmap(source, 0, 0, source.getWidth(),source.getHeight(), matrix,true);
		this.init();
	}
	/**
	 * 初始化珠盘上所有的珠子
	 * */
	public void init(){
		for (int i = 0; i < beads.length; i++) {
			for (int j = 0; j < beads.length; j++) {
				Bead bead = new Bead();
				bead.index_x = i;
				bead.index_y = j;
				beads[i][j] = bead;
			}
		}
	}
	public int getHistScore(){
		return histScore;
	}
	public void setHistScore(int histScoreIn){
		this.histScore = histScoreIn;
	}
	public int getTotleScore(){
		return totalScore;
	}
	public void setTotleScore(int totleScoreIn){
		if(totleScoreIn == 0){
			this.totalScore = 0;
		}
		this.totalScore  += totleScoreIn;
	}
	
}












































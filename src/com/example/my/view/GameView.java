package com.example.my.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.example.my.bead.Bead;
import com.example.my.bead.BeadBord;
import com.example.my.gonewithfive.R;
import com.example.my.service.GameService;
import com.example.my.utils.Constant;
import com.example.my.utils.FileUtils;

public class GameView  extends View{
	/**定义珠盘*/
	private BeadBord beadBord;
	/**帝业业务层对象*/
	private GameService gameService;
	/**定义Matrix*/
	private Matrix matrix = new Matrix();
	/**定义选中的珠子*/
	private Bead selecetedBead;
	//定义目的地的珠子
	private Bead targetBead;
	/**定义选中的珠子放大或者缩小的标识符*/
	private boolean isFlag = true;
	/**定义一个临时的珠子来缓存选中的种子*/
	private Bead tempBead;
	/**定义一个属性缓存上一个点*/
	private Point upPoint;
	
	public GameView(Context context , AttributeSet attrs){
		super(context,attrs);
		this.beadBord = new BeadBord(context);
		this.beadBord.setHistScore(FileUtils.readScore(context));
		matrix.setScale(Constant.MATRIX_SCALE , Constant.MATRIX_SCALE);
	}
	public void draw(Canvas canvas){
		super.draw(canvas);
		Paint paint = new Paint();
		paint.setTextSize(28);
		paint.setStrokeWidth(2);
		paint.setColor(Color.YELLOW);
		int histScore = beadBord.getHistScore();
		int totalScore = beadBord.getTotleScore();
		int topImagHeight =  beadBord.topImage.getHeight();
		int topImagWidth = beadBord.topImage.getWidth();
		float bWidth = beadBord.gridWidth;
		float bHeight = beadBord.gridHeight;
		float scale = beadBord.scale;
		//float dropDestance = Math.abs(getResources().getDrawable(R.drawable.background_water0).getIntrinsicHeight()*(scale-1));
		float dropDestance = 34*scale;
		//计算出左边的位置
		float left = this.getWidth()/2 - topImagWidth/2;
		//绘制最好的分数（左边最中间）
		canvas.drawText(this.getResources().getString(R.string.hist_score)+histScore, 
				left/4,topImagHeight/2+paint.getTextSize()/2, paint);
		//绘制中间的小珠盘
		canvas.drawBitmap(beadBord.topImage,left,0, paint);
		//绘制下一轮要显示的三个珠子
		List<Bead> lists = gameService.getPreparedBeads();
		for (int i = 0; i < lists.size(); i++) {
			Bitmap source = lists.get(i).getBitmap();
			source = Bitmap.createBitmap(source, 0, 0, source.getWidth(),source.getHeight(), matrix,true);
			canvas.drawBitmap(source, left+i*topImagWidth/3+2.0f,0, paint);
		}
		
		//绘制当前的分数（右边最中间）
		canvas.drawText(this.getResources().getString(R.string.total_score)+totalScore,
						left+topImagWidth+ left / 4,
						topImagHeight/2+paint.getTextSize()/2, 
						paint);
		
		//绘制珠盘
		canvas.drawBitmap(beadBord.boardImage,0,topImagHeight+dropDestance, paint);
		//绘制珠子
		for (int i = 0; i < beadBord.beads.length ; i++) {
			for (int j = 0; j < beadBord.beads.length; j++) {
				Bead bead = beadBord.beads[i][j];
				if(bead.getBitmap() != null){
					List<Point> points  = gameService.getLinkPoints();
					//判断是不是用选中的珠子
					if(bead.equals(selecetedBead) || points.contains(new Point(bead.index_x,bead.index_y))){
						if(isFlag){
							//正常
							canvas.drawBitmap(bead.getBitmap(), 
									i*bWidth+Constant.LEFT_RIGHT_SPACE*scale,
									j*bHeight+Constant.TOP_BUTTOM_SPACE*scale
									+topImagHeight+dropDestance,paint);
						}else{
							//缩小
							Bitmap source= bead.getBitmap();
							Bitmap temp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),matrix,true);
							canvas.drawBitmap(temp, 
									i*bWidth+Constant.LEFT_RIGHT_SPACE*scale
									+(source.getWidth()-temp.getWidth())/2,
									j*bHeight+Constant.TOP_BUTTOM_SPACE*scale
									+topImagHeight
									+(source.getHeight()-temp.getHeight())/2+dropDestance,paint);
						}
					}else{
						//不是用户选择的珠子
						canvas.drawBitmap(bead.getBitmap(), 
								i * bWidth + Constant.LEFT_RIGHT_SPACE * scale, 
								j * bHeight + Constant.TOP_BUTTOM_SPACE * scale
								+topImagHeight+dropDestance, 
								paint);
					}
				}
				
			}
		}
		
	}
	/**获取珠盘实体的方法*/
	public BeadBord getBeadBord(){
		return beadBord;
	}
	public void setGameService(GameService gameService){
		this.gameService = gameService;
	}
	public void setSelectedBead(Bead selectedBead){
		this.selecetedBead = selectedBead;
	}
	public Bead getSelectedBead(){
		return this.selecetedBead;
	}
	public void setIsFlag(){
		this.isFlag = !isFlag;
		this.postInvalidate();
	}
	//移动珠子
	public void moveBead(Point point) {
		//upPoint存储了上一bead所处的位置
		if (upPoint != null){
			beadBord.beads[upPoint.x][upPoint.y].setBitmap(null);
		}
		if (!point.equals(new Point(targetBead.index_x, targetBead.index_y))){
			beadBord.beads[point.x][point.y].setBitmap(tempBead.getBitmap());
			upPoint = point;
		}else{
			// 目的点
			beadBord.beads[targetBead.index_x][targetBead.index_y].setBitmap(tempBead.getBitmap());
			beadBord.beads[targetBead.index_x][targetBead.index_y].beadColor = tempBead.beadColor;
			upPoint = null;
			targetBead = null;
		}
		this.postInvalidate();
	}
	//设置目的珠子
	public void setTargetBead(Bead targetBead) {
		this.targetBead = targetBead;
		// 创建tempBead来缓存selectedBead
		tempBead = new Bead();
		tempBead.setBitmap(selecetedBead.getBitmap());
		tempBead.beadColor = selecetedBead.beadColor;
		// 将选中的珠子设置为空
		selecetedBead.setBitmap(null);
		selecetedBead = null;
	}
	/**显示一个珠子*/
	public void disPlayBead(Bead bead){
		beadBord.beads[bead.index_x][bead.index_y].setBitmap(bead.getBitmap());
		beadBord.beads[bead.index_x][bead.index_y].beadColor = bead.beadColor;
		this.postInvalidate();
	}
}



























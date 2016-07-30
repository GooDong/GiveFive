package com.example.my.gonewithfive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class BirdDemo extends View{
	Bitmap[] bird = new Bitmap[3];
	Paint paint;
	int imageWidth;
	private int state = 0;
	
	private int toLeft = 0;
	
	long time = 0;
	int screenWidth;
	public BirdDemo(Context context) {
		super(context);
		
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		
		paint = new Paint();
		
		bird[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.bird_right_fly0);
		bird[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.bird_right_fly1);
		bird[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.bird_right_fly2);
		
		imageWidth = bird[0].getWidth();
		
		time = System.currentTimeMillis();
		
		new Thread(new GameThread()).start();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawBitmap(bird[state], toLeft, 30, null);
	}
	
	class GameThread implements Runnable{
		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(500);
					if(state <= 1 ){
						state++;
					}else{
						state = 0;
					}
					
					if(toLeft <= screenWidth ){
						toLeft += 5;
					}else{
						Thread.sleep(10000);
						toLeft = 0;
					}
					postInvalidateDelayed(100);
				} catch (Exception e) {
					time = System.currentTimeMillis() - time;
					Log.i("---", "====="+time);
					Log.i("---", e.getMessage());
				}
			}
		}
	}
}



















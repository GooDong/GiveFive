package com.example.my.gonewithfive;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.KeyEvent;
import android.view.View;

public class PixOperation extends View implements Runnable {
	int BW;
	int BH;
	short[] buf2;
	short[] buf1;
	int[] Bitmap2;
	int[] Bitmap1;
	int y = 0;
	int BitmapId;
	int timeRecoder;
	public static Random rand = new Random(47);
	final static String conunts = "-0$8-0$16-0$10-1$4-1$9-1$13-2$7-2$10-2$16-3$8-3$14-3$13-4$7-4$9-4$14-4$18-";;

	public PixOperation(Context context, int bitmapid) {
		super(context);
		this.BitmapId = bitmapid;
		Matrix matrix = new Matrix();
		matrix.setScale(1.6f, 1.5f);
		Bitmap image1 = BitmapFactory.decodeResource(this.getResources(),
				BitmapId);
		Bitmap image = Bitmap.createBitmap(image1, 0, 0, image1.getWidth(),
				image1.getHeight(), matrix, true);

		BW = image.getWidth();

		BH = image.getHeight();

		buf2 = new short[BW * BH];
		buf1 = new short[BW * BH];
		Bitmap2 = new int[BW * BH];
		Bitmap1 = new int[BW * BH];

		image.getPixels(Bitmap1, 0, BW, 0, 0, BW, BH);
		new Thread(this).start();
	}

	// 以x，y为中心，stonesize为半径，将圆内的数值赋值为-stoneweiht
	void DropStone(int x, int y, int stonesize, int stoneweight) {

		for (int posx = x - stonesize; posx < x + stonesize; posx++) {
			for (int posy = y - stonesize; posy < y + stonesize; posy++) {
				if ((posx - x) * (posx - x) + (posy - y) * (posy - y) < stonesize
						* stonesize) {
					// 不断更改圆心位置，初始化多个区域
					buf1[BW * posy + posx] = (short) -stoneweight;

					for (int i = 1; i <= 18; i++) {
						for (int j = 0; j <= 5; j++) {
							if (conunts.contains("-" + j + "$" + i + "-")) {
								buf1[BW * (posy + j * 50) + (posx + i * 50)] = (short)-stoneweight;
							} else {

							}
						}
					}
				}
			}
		}

	}

	void RippleSpread() {
		for (int i = BW; i < (BW * BH - BW); i++) {// 从第二行第一个元素开始取值，直到倒数第二行最后一个元素
			// 取出各个元素点的上下左右四个点的值之和，右移约等于除以2并略大于除2后的商
			// 注意到数组中的值为负数，因而差减小了
			buf2[i] = (short) (((buf1[i - 1] + buf1[i + 1] + buf1[i - BW] + buf1[i
					+ BW]) >> 1) - buf2[i]);
			// >>,移位操作符，按照操作符右侧指定的位数将操作符左边的数向右移动
			// <<,移位操作符，按照操作符右侧指定的位数将操作符左边的数向左移动（低位补0）
			//
			buf2[i] -= buf2[i] >> 5;
		}
		short[] ptemp = buf1;
		buf1 = buf2;
		buf2 = ptemp;
	}

	void render() {
		int xoff, yoff;
		int k = BW;
		for (int i = 1; i < BH - 1; i++) {
			for (int j = 0; j < BW; j++) {
				xoff = buf1[k - 1] - buf1[k + 1];
				yoff = buf1[k - BW] - buf1[k + BW];
				if ((i + yoff) < 0) {
					k++;
					continue;
				}
				if ((i + yoff) > BH) {
					k++;
					continue;
				}
				if ((j + xoff) < 0) {
					k++;
					continue;
				}
				if ((j + xoff) > BW) {
					k++;
					continue;
				}
				int pos1, pos2;
				pos1 = BW * (i + yoff) + (j + xoff);
				pos2 = BW * i + j;
				Bitmap2[pos2++] = Bitmap1[pos1++];
				k++;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		DropStone(BW / 17, BH / 5, 22, 50);
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(Bitmap2, 0, BW, 0, 0, BW, BH, false, null);
	}

	@Override
	public void run() {
		DropStone(BW / 17, BH / 5, 22, 50);
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(100);
				timeRecoder++;
			} catch (Exception e) {
			}
			RippleSpread();
			render();
			postInvalidate();
			if (timeRecoder > 90) {
				 buf2 = new short[BW*BH];
				 buf1 = new short[BW*BH];
				timeRecoder = 0;
				int k1 = rand.nextInt(4) + 15;
				int k2 = rand.nextInt(3) + 4;
				DropStone(BW / k1, BH / k2, 22, 60);
			}

		}
	}
}

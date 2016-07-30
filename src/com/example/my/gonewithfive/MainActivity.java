package com.example.my.gonewithfive;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.bead.Bead;
import com.example.my.service.GameService;
import com.example.my.service.serviceImpl;
import com.example.my.utils.Constant;
import com.example.my.utils.DialogUtil;
import com.example.my.utils.FileUtils;
import com.example.my.view.GameView;
/**
 * 
 * 五子连珠 游戏活动的主窗口
 * @author 石广洞
 * @since 2015-11-2
 * */
public class MainActivity extends Activity {
	// 定义游戏的业务对象
	private GameService gameService;
	// 定义GameView
	private GameView gameView;
	//定义小鸟视图
	private BirdDemo bird;
	//定义定时器
	private Timer timer;
	//定义可走的点
	private List<Point> points;
	//定义要显示的珠子
	private List<Bead> displayBeads;
	//定义消珠子的次数
	private int count = 0;
	//定义一个锁
	private boolean lock = true;
	//定义手机振动器
	private Vibrator vibrator;
	//定义音乐播放器
	private MediaPlayer musicPlayer;
	//定义音效播放数组
	private MediaPlayer[] soundPlayer = new MediaPlayer[Constant.SOUNDS.length];
	//定义音效是否播放的标识符
	private boolean isSoundPlay = false;
	// 定义音乐是否播放的标识符
	private boolean isMusicPlay = false;
	//定义底部波纹布局文件
	LinearLayout lin_in;
	//定义一个波纹对象
	PixOperation pixOp;
	//定义一个时间点，获取用户连续两次按下返回键的间隔，判断是否退出游戏
	long timeRecoder = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActionBar().show();
		setContentView(R.layout.activity_main);
		//获取游戏主界面
		this.gameView = (GameView)findViewById(R.id.gameView);
		//创建业务对象
		gameService = new serviceImpl(this, gameView.getBeadBord());
		//设置业务对象
		bird = new BirdDemo(this);//添加小鸟
		gameView.setGameService(gameService);
		RelativeLayout rel = (RelativeLayout)findViewById(R.id.rel);
		rel.addView(bird);
		//为gameView绑定触摸事件
		gameView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//按下
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					//根据用户点击的坐标获取相对应的珠子
					Bead bead = gameService.getSelectedBead(event.getX(), event.getY());
					//用户选中的珠子（要走 珠子）
					if(bead != null && bead.getBitmap() != null && lock ){
						//为gameView设置选中的珠子
						gameView.setSelectedBead(bead);
						//要让选中的珠子跳动
						startAnim(Constant.TRIP_FLAG_1,Constant.TRIP_TIMER_1);
						//播放音效
						if(isSoundPlay){
							soundPlayer[0].start();
							Log.i("---","  "+(soundPlayer[0].isPlaying()));
						}
					}else{
							//珠子存在但是没有位图
							if(bead != null && gameView.getSelectedBead() != null){
								//获取两个珠子之间的线路
								points = gameService.getPath(gameView.getSelectedBead(), bead);
								if(!points.isEmpty()){
									//为gameView设置目的的珠子
									gameView.setTargetBead(bead);
									//加锁
									lock = false;
									//开启动画效果走珠子
									startAnim(Constant.MOVE_FLAG_2,Constant.MOVE_TIMER_2);
								}else{
									//播放音效
									if(isSoundPlay){
										soundPlayer[1].start();
										Log.i("---","  "+(soundPlayer[1].isPlaying()));
									}
								}
							}
						}
					}
				return true;
			}
		});
		//获取手机震动器
		vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
		//初始化音乐播放器
		musicPlayer = MediaPlayer.create(this, R.raw.game);
		//设置音频流的类型
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//设置循环播放
		musicPlayer.setLooping(true);
		//初始化音频流的类型
		for (int i = 0; i < Constant.SOUNDS.length; i++) {
			soundPlayer[i] = MediaPlayer.create(this, Constant.SOUNDS[i]);
			//设置音频流的类型
			soundPlayer[i].setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		
		
		//绘制底部波纹动画
		//找到布局文件
		lin_in = (LinearLayout)findViewById(R.id.lin_in);
		
		pixOp = new PixOperation(this,R.drawable.background_water0);
		lin_in.addView(pixOp);
	
	
	}
	
	
	
	
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what){
				case Constant.TRIP_FLAG_1: // 代表选中珠子跳动
					gameView.setIsFlag();
					break;
				case Constant.MOVE_FLAG_2: // 代表走珠子
					Point point = (Point)msg.obj;
					if (point != null){
						// 走一点
						gameView.moveBead(point);
					}else{
						// 代表走完了珠子 --->消珠子或者显示三个珠子
						autoScan(Constant.CLEAR_SCAN_TYPE_1);
					}
					break;
				case Constant.SHOW_FLAG_3: // 代表依次显示珠子
					Bead bead = (Bead)msg.obj;
					if (bead != null){
						// 显示一个珠子
						gameView.disPlayBead(bead);
					}else{
						// 代表系统生成了三个珠子 --->消珠子
						autoScan(Constant.CLEAR_SCAN_TYPE_2);
					}
					break;
				case Constant.CLEAR_FLAG_4: // 代表消珠子
					if (msg.obj != null){
						// 让珠子同时闪烁
						gameView.setIsFlag();
					}else{
						// 获取分数
						int score = gameService.getPerScore();
						if (score > 0){
							// 提示得分
							Toast.makeText(MainActivity.this, "+" + score, Toast.LENGTH_SHORT).show();
							// 设置分数累加
							gameService.setTotalScore();
						}
						// 消除珠子
						gameService.clearBead();
						// 组件重绘
						gameView.postInvalidate();
						// 解锁
						lock = true;
					}
					break;
			}
			return true;
		}
	});
	
	
	/**
	 * 开启动画的方法
	 * @param flag 标识符
	 * @param time 时长
	 * */
private void startAnim(final int flag, long time){
		
		if (timer != null){
			timer.cancel(); // 关闭定时器
		}
		
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = flag;
				switch (flag){
					case Constant.TRIP_FLAG_1: // 代表选中珠子跳动
						handler.sendMessage(msg);
						break;
					case Constant.MOVE_FLAG_2: // 代表走珠子
						if (!points.isEmpty()){
							// 每次删除第一个
							Point point = points.remove(0);
							msg.obj = point;
						}else{
							msg.obj = null;
							timer.cancel(); // 取消定时器
						}
						handler.sendMessage(msg);
						break;
					case Constant.SHOW_FLAG_3: // 代表依次显示珠子
						if (!displayBeads.isEmpty()){
							// 每次删除第一个
							Bead bead = displayBeads.remove(0);
							msg.obj = bead;
						}else{
							msg.obj = null;
							timer.cancel(); // 取消定时器
						}
						handler.sendMessage(msg);
						break;
					case Constant.CLEAR_FLAG_4: // 代表消珠子
						if (count++ < Constant.PER_NUM){
							msg.obj = true;
						}else{
							msg.obj = null;
							count = 0;
							timer.cancel(); // 取消定时器
						}
						handler.sendMessage(msg);
						break;
				}
			}
		}, 0, time);
	}
	/**
	 * 自动扫描珠子
	 * @param scanType 1:用户走完珠子。2：系统生成三个珠子
	 * */
	private void autoScan(int scanType) {
		// 消珠子(有珠子可消，就不要显示三个珠子)
		if (gameService.scanBead(scanType)){
			// 开启消珠子动画效果
			startAnim(Constant.CLEAR_FLAG_4, Constant.CLEAR_TIMER_4);
			// 播放音效
			if(isSoundPlay){
				soundPlayer[2].start();
				Log.i("---","  "+(soundPlayer[2].isPlaying()));
			}
		}else{// 没有珠子可消，就生成三个珠子
			if (scanType == 1){
				// 显示珠子
				displayBeads = gameService.getDisplayBeads();
				if (displayBeads != null){
					// 开启动画效果依次显示珠子
					startAnim(Constant.SHOW_FLAG_3, Constant.SHOW_TIMER_3);
				}else{
					// 游戏结束
					gameover();
					// 解锁
					lock = true;
				}
				
			}else{
				// 判断珠盘上是否有空珠子
				if (gameService.getEmptyBeads().size() == 0){
					// 游戏结束
					gameover();
				}
				// 解锁
				lock = true;
			}
		}
	}
	/**
	 * 游戏结束
	 */
	private void gameover(){
		//手机震动(1000毫秒)
		vibrator.vibrate(1000);
		//获取本次游戏总得分
		int totalScore = gameView.getBeadBord().getTotleScore();
		//获取历史成绩
		int histScore = gameView.getBeadBord().getHistScore();
		// 提示本次游戏总得分
		Toast.makeText(this, "本次游戏总得分: " + totalScore, Toast.LENGTH_SHORT).show();
		//记录最好成绩
		if(totalScore > histScore){
			//记录下来（写到xml文件中）
			FileUtils.writeScore(this, totalScore);
			//设置历史分数
			gameView.getBeadBord().setHistScore(totalScore);
		}
		//游戏重新开始
		gameService.reset();
		gameView.postInvalidate();
	}
	/**
	 * 监听是不是按返回键
	 * */
	@SuppressWarnings("deprecation")
	public void onBackPressed(){
		
		timeRecoder = System.currentTimeMillis() - timeRecoder;
		if(timeRecoder < 2000){
			timeRecoder = 0;
			System.exit(0);
		}else{
			TextView tv = new TextView(this);
			tv.setBackground(getResources().getDrawable(R.drawable.toast_text_background));
			tv.setText("再按一次退出");
			tv.setTextColor(Color.WHITE);
			Toast toast = new Toast(this);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(tv);
			toast.show();
			timeRecoder = System.currentTimeMillis();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_start:
			//开启音乐
			item.setChecked(true);
			musicPlayer.start();
			isMusicPlay = true;
			break;
		case R.id.m_close:
			item.setChecked(true);
			musicPlayer.pause();
			isMusicPlay = false;
			break;
		case R.id.s_start:
			item.setChecked(true);
			isSoundPlay = true;
			break;
		case R.id.s_close:
			item.setChecked(true);
			isSoundPlay = false;
			break;
		case R.id.menu_close:
			DialogUtil.createDialog(this, "您确定要退出游戏吗？").show();
			break;
		case R.id.menu_restart:
			gameService.reset();
			gameView.postInvalidate();
			break;
		}
		return true;
	}
	/**
	 * 当前窗口不处于活跃状态
	 * */
	@Override
	protected void onPause() {
		if(isMusicPlay && musicPlayer.isPlaying()){
			musicPlayer.pause();
		}
		super.onPause();
	}
	@Override
	protected void onResume() {
		if(isMusicPlay){
			musicPlayer.start();
		}
		super.onResume();
	}
	/**当Activity销毁时*/
	@Override
	protected void onDestroy() {
		if(musicPlayer.isPlaying()){
			musicPlayer.stop();
			musicPlayer.release();
		}
		for(MediaPlayer sound : soundPlayer){
			if(sound.isPlaying()){
				sound.stop();
			}
			sound.release();
			
		}
		super.onDestroy();
	}
}


















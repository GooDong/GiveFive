package com.example.my.gonewithfive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.my.gonewithfive.R;

public class Welcom extends Activity{
	//定义Handler消息监听器
	private Handler  handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == 0x1314){
				Intent intent = new Intent(Welcom.this,MainActivity.class);
				startActivity(intent);
			}
			return true;
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
		//延迟3000毫秒发送消息
		handler.sendEmptyMessageDelayed(0x1314,3000);
	}
	@Override
	protected void onPause() {
		this.finish();
		super.onPause();
	}
}



















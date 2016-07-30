package com.example.my.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FileUtils {
	/**
	 * 写分数
	 * @param context，score
	 * */	
	public static void writeScore(Context context,int score){
		//第一个参数:生成xml的文件名
		//第二个参数：以什么样的方式操作这个文件
		SharedPreferences shared = context.getSharedPreferences("gongwithfive_score" , Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		editor.putInt("score" , score);
		editor.commit();//提交数据
	}
	public static int readScore(Context context){
		SharedPreferences shared = context.getSharedPreferences("gongwithfive_score", Context.MODE_PRIVATE);
		return shared.getInt("score",0);
	}
	
}

package com.example.my.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**弹出窗体工具类*/
public class DialogUtil {
	/**
	 * 创建弹出窗体的方法
	 * @param context 上下文
	 * @param message 提示消息
	 * */
	public static Dialog createDialog(final Context context , String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示：");
		builder.setMessage(message);
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((Activity)context).finish();
			}
		});
		builder.setNegativeButton("取消",null);
		return builder.create();
	}
	
	
}

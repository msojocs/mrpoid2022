package com.mrpoid.apps;

import android.content.Intent;
import android.util.Log;

import com.mrpoid.app.EmulatorActivity;
import com.mrpoid.app.EmulatorService;

public class AppService0 extends EmulatorService {
	private static String TAG = AppService0.class.getSimpleName();
	static {
		EmulatorActivity.APP_ACTIVITY_NAME = "com.mrpoid.apps.AppActivity0";
		EmulatorActivity.APP_SERVICE_NAME = "com.mrpoid.apps.AppService0";
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "创建 onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "启动 onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
}

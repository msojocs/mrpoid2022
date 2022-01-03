package com.mrpoid.apps;

import android.os.Bundle;
import android.util.Log;

import com.mrpoid.app.EmulatorActivity;

public class AppActivity0 extends EmulatorActivity {
	static final String TAG = AppActivity0.class.getSimpleName();
	static {
		APP_ACTIVITY_NAME = "com.mrpoid.apps.AppActivity0";
		APP_SERVICE_NAME = "com.mrpoid.apps.AppService0";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "create创建");
		super.onCreate(savedInstanceState);
	}
}

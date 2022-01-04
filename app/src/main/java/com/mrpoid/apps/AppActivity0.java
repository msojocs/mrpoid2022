package com.mrpoid.apps;

import android.os.Bundle;
import android.util.Log;

import com.mrpoid.app.EmulatorActivity;
import com.mrpoid.mrplist.utils.Test;

public class AppActivity0 extends EmulatorActivity {
	static final String TAG = AppActivity0.class.getSimpleName();
	static {
		APP_ACTIVITY_NAME = AppActivity0.class.getCanonicalName();
		APP_SERVICE_NAME = "com.mrpoid.apps.AppService0";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "create创建");
		Test.hello();
		super.onCreate(savedInstanceState);
	}
}

package com.mrpoid.mrplist.app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.mrpoid.mrpliset.R;
import com.mrpoid.mrplist.view.BrowserFragment;

public class BrowserActivity extends BaseActivity {

	private static String TAG = BrowserActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle arg0) {

		setTheme(R.style.AppTheme);
		setContentView(R.layout.fragment_browser);

		super.onCreate(arg0);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, new BrowserFragment(), "main").commit();
	}

	public boolean isLightTheme() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "选项创建");

		return true;
	}
}

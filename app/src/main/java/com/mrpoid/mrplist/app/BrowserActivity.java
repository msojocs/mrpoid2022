package com.mrpoid.mrplist.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import com.mrpoid.mrplist.view.BrowserFragment;

public class BrowserActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, new BrowserFragment(), "main").commit();
	}
}

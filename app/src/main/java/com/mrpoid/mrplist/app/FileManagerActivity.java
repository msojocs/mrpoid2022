package com.mrpoid.mrplist.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import com.mrpoid.mrplist.view.ExplorerFragment;

/**
 * 文件管理界面
 *
 */
public class FileManagerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, new ExplorerFragment(), "main").commit();

		if(getSupportActionBar() != null)
			getSupportActionBar().setElevation(0);
		
//		getActionBar()
//		getWindow().setBackgroundDrawableResource(R.drawable.wp5);
//		getWindow().setBackgroundDrawable(new ColorDrawable(0xffffffff));
		
//		setTranslucentNavigation(true);
	}
}

package com.mrpoid.mrplist.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.mrpoid.mrplist.R;
import com.mrpoid.mrplist.view.ExplorerFragment;

/**
 * 文件管理界面
 *
 */
public class FileManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // 设置显示用的布局
        setContentView(R.layout.file_browser);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 替换file_list 为 ExplorerFragment
        ft.replace(R.id.file_list, new ExplorerFragment(), "main").commit();

        // 设置顶部栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setElevation(0);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //		getActionBar()
        //		getWindow().setBackgroundDrawableResource(R.drawable.wp5);
        //		getWindow().setBackgroundDrawable(new ColorDrawable(0xffffffff));

        //		setTranslucentNavigation(true);
    }
}

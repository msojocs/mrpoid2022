package com.mrpoid.mrplist.moduls;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;


/**
 * mrp加载器
 *
 * @author Yichou 2013-11-23
 *
 */
public class FileListLoader extends AsyncTaskLoader<List<MrpFile>> {
    private File mPath;
    private boolean mIsRoot;
    private final FileFilter mFilter;


    public FileListLoader(Context context, String path, boolean isRootPath, FileFilter fileFilter) {
        super(context);

        mPath = new File(path);
        mIsRoot = isRootPath;
        mFilter = fileFilter;
    }

    public void reload(String path, boolean isRoot) {
        mPath = new File(path);
        mIsRoot = isRoot;

        onContentChanged();
    }

    @Override
    public List<MrpFile> loadInBackground() {
        List<MrpFile> list = findMrpFiles();
        Collections.sort(list);
        return list;
    }

    /**
     * Called when there is new data to deliver to the client. The super
     * class will take care of delivering it; the implementation here just
     * adds a little more logic.
     */
    @Override
    public void deliverResult(List<MrpFile> list) {
        super.deliverResult(list);
    }

    @Override
    protected void onStartLoading() {
        // If we currently have a result available, deliver it
        // immediately.
        //		deliverResult(mCacheList);
        //		if (takeContentChanged()) {
        //			// If the data has changed since the last time it was loaded
        //			// or is not currently available, start a load.
        //			forceLoad();
        //		}

        forceLoad();
    }

    //	/**
    //	 * 外部请求停止加载
    //	 */
    //	@Override
    //	protected void onStopLoading() {
    //		System.err.println("-------- onStopLoading");
    //
    //		// Attempt to cancel the current load task if possible.
    //		cancelLoad();
    //	}

    public List<MrpFile> findMrpFiles() {
        if (mPath == null)
            return null;

        List<MrpFile> mCacheList = new ArrayList<MrpFile>();
        File[] files = mPath.listFiles(mFilter);
        if (!mIsRoot)
            mCacheList.add(new MrpFile()); //..

        if (files != null && files.length > 0) {
            for (File f : files) {
                mCacheList.add(new MrpFile(f));
            }
        }

        return mCacheList;
    }
}

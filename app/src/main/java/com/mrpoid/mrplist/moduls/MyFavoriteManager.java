package com.mrpoid.mrplist.moduls;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.edroid.common.utils.FileUtils;

import android.content.Context;

/**
 * 我的收藏管理
 */
public class MyFavoriteManager {
    public final static MyFavoriteManager INSTANCE = new MyFavoriteManager();

    private final List<MpFile> list;

    public MyFavoriteManager() {
        list = new ArrayList<>();
    }


    public void remove(Context ctx, int i) {
        list.remove(i);
        save(ctx);
    }

    public void add(Context ctx, String path) {
        for (MpFile file : list) {
            if (file.getPath().equals(path))
                return;
        }

        list.add(new MpFile(path));

        save(ctx);
    }

    public List<MpFile> getAll() {
        return list;
    }

    public void read(Context ctx) {
        list.clear();
        try {
            JSONArray array = new JSONArray(
                    FileUtils.fileToString(ctx.getFileStreamPath("favorate.list")));
            for (int i = 0; i < array.length(); i++) {
                list.add(new MpFile(array.getString(i)));
            }
        } catch (Exception e) {
            Common.log.e("read faorate.list fail!" + e.getMessage());
        }
    }

    public void save(Context ctx) {
        try {
            JSONArray array = new JSONArray();
            for (MpFile file : list) {
                array.put(file.getPath());
            }

            FileUtils.stringToFile(ctx.getFileStreamPath("favorate.list"),
                    array.toString());
        } catch (Exception e) {
            Common.log.e("save faorate.list fail!" + e.getMessage());
        }
    }
}

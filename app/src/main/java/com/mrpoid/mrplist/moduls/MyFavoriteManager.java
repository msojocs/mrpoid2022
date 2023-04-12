package com.mrpoid.mrplist.moduls;

import android.content.Context;

import com.edroid.common.utils.FileUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏管理
 */
public class MyFavoriteManager {
    public final static MyFavoriteManager INSTANCE = new MyFavoriteManager();

    private final List<MrpFile> list;

    public MyFavoriteManager() {
        list = new ArrayList<>();
    }

    /**
     * 移除收藏
     * @param ctx 上下文
     * @param i 下标
     */
    public void remove(Context ctx, int i) {
        list.remove(i);
        save(ctx);
    }

    /**
     * 添加收藏
     * @param ctx 上下文
     * @param path 路径
     */
    public void add(Context ctx, String path) {
        for (MrpFile file : list) {
            if (file.getPath().equals(path))
                return;
        }

        list.add(new MrpFile(path));

        save(ctx);
    }

    public List<MrpFile> getAll() {
        return list;
    }

    public void read(Context ctx) {
        list.clear();
        try {
            JSONArray array = new JSONArray(
                    FileUtils.fileToString(ctx.getFileStreamPath("favorate.list")));
            for (int i = 0; i < array.length(); i++) {
                list.add(new MrpFile(array.getString(i)));
            }
        } catch (Exception e) {
            Common.log.e("read faorate.list fail!" + e.getMessage());
        }
    }

    public void save(Context ctx) {
        try {
            JSONArray array = new JSONArray();
            for (MrpFile file : list) {
                array.put(file.getPath());
            }

            FileUtils.stringToFile(ctx.getFileStreamPath("favorate.list"),
                    array.toString());
        } catch (Exception e) {
            Common.log.e("save faorate.list fail!" + e.getMessage());
        }
    }
}

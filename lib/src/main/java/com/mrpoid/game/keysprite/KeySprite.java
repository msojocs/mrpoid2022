package com.mrpoid.game.keysprite;

import java.io.File;
import java.util.ArrayList;

public interface KeySprite {
	ArrayList<Sprite> getSpriteList();
	
	void add(Sprite sprite) ;
	
	void remove(int index);

	Sprite get(int index);
	
	int count();
	
	/**
	 * 运行精灵
	 *
	 */
	void run(KeyEventListener l);
	
	/**
	 * 停止运行
	 */
	void stop();

	/**
	 * 写入文件
	 *
	 */
	void toXml(File file) throws Exception;
	
	/**
	 * 从文件读取
	 *
	 */
	void fromXml(File file) throws Exception;
}

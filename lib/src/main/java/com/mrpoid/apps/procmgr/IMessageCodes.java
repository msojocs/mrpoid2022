package com.mrpoid.apps.procmgr;

public interface IMessageCodes {
	int MSG_HELLO = 0x1001;
	int MSG_HEART = 0x1002; // 心跳消息
	
	int MSG_START = 0x1011;
	int MSG_PAUSE = 0x1012;
	int MSG_RESUME = 0x1013;
	int MSG_EXIT = 0x1014;
	
	int MSG_LAUNCH_FINISH = 0x1021;
	
	int MSG_BYBY = 0x1fff;
}

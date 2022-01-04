package com.mrpoid.apps.procmgr;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.mrpoid.core.EmuLog;
import com.edroid.common.utils.Logger;


/**
 * 多开管理器
 * 
 * @author Yichou 2013-12-19
 *
 */
public class AppProcessManager {
	public static final Logger log = Logger.create(EmuLog.isShowLog, AppProcessManager.class.getSimpleName());
	private static String TAG = AppProcessManager.class.getSimpleName();
	private enum ItemState {
		IDLE,
		WAITING,
		CONNECTED,
		RUNNING
	}
	
	private static final class Item {
		/**
		 * 进程状态
		 */
		ItemState state;
		long readyTime; //开始等待连接的时刻
		long connectedTime; //连接上的时刻
		/**
		 * 进程标志
		 */
		String mark;
		ServiceConnection conn;
		AppProcess app;
		
		public Item() {
			reset();
		}
		
		public void reset() {
			this.mark = null;
			this.app = null;
			this.readyTime = 0;
			this.state = ItemState.IDLE;
		}
		
		@Override
		public String toString() {
			return mark;
		}
	}

	/**
	 * 最大进程数
	 */
	private final int MAX_PROC_COUNT;
	/**
	 * 进程列表
	 */
	private final Item[] mProcList;
	/**
	 * 服务名前缀
	 */
	private final String SERVICE_NAME_PREFIX;
	private final Context mContext;

	
	public AppProcessManager(Context context, String serviceNamePrefix, int maxCount) {
		Log.i(TAG, "init");
		mContext = context.getApplicationContext();
		SERVICE_NAME_PREFIX = serviceNamePrefix;
		MAX_PROC_COUNT = maxCount;
		mProcList = new Item[MAX_PROC_COUNT];

		for(int i=0; i < MAX_PROC_COUNT; i++)
			mProcList[i] = new Item();
	}

	/**
	 * 绑定进程索引与mrp路径
	 *
	 * @param procIndex 进程索引
	 * @param mark MRP路径
	 * @param cb 回调
	 */
	private void binProc(int procIndex, String mark, RequestCallback cb) {
		String service = SERVICE_NAME_PREFIX + procIndex;

		// 创建一个链接
		MrpConnection connection = new MrpConnection(procIndex, cb);
		
		Intent intent = new Intent();
		intent.setClassName(mContext, service);
		//使用全局 context绑定
		mContext.bindService(intent, connection, Service.BIND_AUTO_CREATE);
		
		synchronized (mProcList) {
			mProcList[procIndex].conn = connection;
			mProcList[procIndex].mark = mark;
			mProcList[procIndex].readyTime = System.currentTimeMillis();
			mProcList[procIndex].state = ItemState.WAITING;
			
			Log.i(TAG, "proc" + procIndex + " wait service connection cb!");
		}
	}

	/**
	 * 添加进程到指定索引，并标记为连接状态
	 * @param index 进程索引
	 * @param process 进程
	 */
	private void markAsConnected(int index, AppProcess process) {
		synchronized (mProcList) {
			mProcList[index].app = process;
			mProcList[index].connectedTime = System.currentTimeMillis();
			mProcList[index].state = ItemState.CONNECTED;
			
			Log.i(TAG, "proc" + index + " connected time="
					+ (System.currentTimeMillis() - mProcList[index].readyTime));
		}
	}

	/**
	 * 标记指定进程为运行状态
	 * @param procIndex 进程索引
	 * @return 成功true | 失败false
	 */
	protected boolean markAsRunning(int procIndex) {
		synchronized (mProcList) {
			if(mProcList[procIndex].state == ItemState.RUNNING) { //被人抢先了一步，你回去吧
				log.e("proc " + procIndex + " has Preemptived by" + mProcList[procIndex].toString());
				return false;
			}
			
			mProcList[procIndex].state = ItemState.RUNNING;

			log.i("proc" + procIndex + " running time=" 
					+ (System.currentTimeMillis() - mProcList[procIndex].connectedTime));
		}
		
		return true;
	}

	/**
	 * 解绑指定进程的服务
	 * @param procIndex 进程索引
	 */
	private void unbindProc(int procIndex) {
		synchronized (mProcList) {
			if (mProcList[procIndex].conn != null) {
				mContext.unbindService(mProcList[procIndex].conn);
				mProcList[procIndex].conn = null;
				
				log.i("unbind proc" + procIndex);
			}
		}
	}
	
	private void resetProc(int procIndex) {
		synchronized (mProcList) {
			unbindProc(procIndex);
			mProcList[procIndex].reset();
		}
	}

	private void exitProc(int procIndex) {
		synchronized (mProcList) {
			unbindProc(procIndex);
			
			if(mProcList[procIndex].app != null) {
				mProcList[procIndex].app.exit(false);
				mProcList[procIndex].app = null;
			}
			
			mProcList[procIndex].reset();
			
			log.i("proc" + procIndex + " exited!");
		}
	}
	
	private void killProc(int procIndex) {
		synchronized (mProcList) {
			unbindProc(procIndex);

			if(mProcList[procIndex].app != null) {
				mProcList[procIndex].app.exit(true);
				mProcList[procIndex].app = null;
			}

			mProcList[procIndex].reset();

			log.i("proc" + procIndex + " killed!");
		}
	}
	
	public Context getContext() {
		return mContext;
	}

	/**
	 * 获取空闲进程索引
	 * @param defProcIndex 指定的索引
	 * @param force 被占用时是否强制使用
	 * @return
	 */
	private int getIdleIndex(int defProcIndex, boolean force) {
		synchronized (mProcList) {
			/**
			 * if procIndex specified we shoud check if we can!
			 */
			if(defProcIndex != -1) {
				if(mProcList[defProcIndex].state == ItemState.IDLE) {
					return defProcIndex;
				}
				else if(mProcList[defProcIndex].state == ItemState.WAITING && force) {
					log.w("force exit waiting proc" + defProcIndex);
					resetProc(defProcIndex);
					
					return defProcIndex;
				}
				else if (mProcList[defProcIndex].state == ItemState.RUNNING && force) {
					log.w("force exit waiting proc" + defProcIndex);
					exitProc(defProcIndex);
					
					return defProcIndex;
				}
			}
			
			for (int i = 0; i < MAX_PROC_COUNT; i++) {
				if (mProcList[i].state == ItemState.IDLE)
					return i;
			}

			// 1.还处于 ready 状态又运行了一个应用？出问题了吧干掉
			for (int i = 0; i < MAX_PROC_COUNT; i++) {
				if (mProcList[i].state == ItemState.WAITING) {
					mProcList[i].reset();
					log.w("use ready proc" + i);
					return i;
				}
			}

			// 2.正在运行，长时间未激活
			long time = System.currentTimeMillis();
			int id = -1;
			for (int i = 0; i < MAX_PROC_COUNT; i++) {
				if (mProcList[i].state == ItemState.RUNNING) {
					if (mProcList[i].app.getLastActiveTime() < time) {
						id = i;
						time = mProcList[i].app.getLastActiveTime();
					}
				}
			}
			
			if (id != -1) {
				log.w("exit running proc" + id);
				exitProc(id);
				return id;
			}
		}
		
		return -1;
	}
	
	/**
	 * 从正在运行列表取
	 * 
	 * @param mark MRP路径
	 * @return
	 */
	private int checkRuning(String mark) {
		synchronized (mProcList) {
			for (int i = 0; i < MAX_PROC_COUNT; i++)
				if (mark.equals(mProcList[i].mark))
					return i;
		}
		
		return -1;
	}
	
	public interface RequestCallback {
		void onSuccess(int procIndex, AppProcess process, boolean alreadyRun);
		
		void onFailure(String msg);
	}

	
	/**
	 * 在独立进程运行 mrp
	 *
	 */
	public synchronized void requestIdleProcess(int defProcIndex, boolean force, String mrpPath, RequestCallback cb) {
		int procIndex = checkRuning(mrpPath);
		
		/**
		 * if is already running we dont't case defProcIndex
		 */
		if(procIndex != -1) {
			Log.i(TAG, "MRP正在运行-" + mrpPath);
			// mrp正在运行 is running
			if(mProcList[procIndex].state == ItemState.RUNNING) {
				mProcList[procIndex].app.resume();
				cb.onSuccess(procIndex, mProcList[procIndex].app, true);
				
				return ;
			} 
			else if(mProcList[procIndex].state == ItemState.WAITING) { //怎么还在 waiting ?
				if(System.currentTimeMillis() - mProcList[procIndex].readyTime > 10*1000) { //wait most 10 seconds
					resetProc(procIndex);
				} else {
					procIndex = -1; //开一个新的
				}
			} 
			else if(mProcList[procIndex].state == ItemState.IDLE) {
				resetProc(procIndex);
			}
		} 
		
		if(procIndex == -1) {
			// 获取空闲进程索引下标
			procIndex = getIdleIndex(defProcIndex, force);
			Log.i(TAG, String.format("Index: %d, MRP准备运行-%s", procIndex, mrpPath));
			
			if(procIndex == -1) {
				final String MSG = "no idle process now!";
				log.w(MSG);
				cb.onFailure(MSG);
				
				return ;
			}
		}
		// 绑定进程
		binProc(procIndex, mrpPath, cb);
	}
	
	public void kill(int procIndex) {
		Log.i(TAG, "杀进程");
		killProc(procIndex);
	}


	private final class MrpConnection implements ServiceConnection {
		int procIndex;
		RequestCallback callback;

		public MrpConnection(int procIndex, RequestCallback callback) {
			this.procIndex = procIndex;
			this.callback = callback;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "proc" + procIndex + " onServiceConnected!");

			AppProcess process = new AppProcess(AppProcessManager.this, procIndex, service);
			markAsConnected(procIndex, process);

			callback.onSuccess(procIndex, process, false);
		}

		/**
		 * 进程意外被杀，才会回调此方法
		 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "proc" + procIndex + " onServiceDisconnected!");

			unbindProc(procIndex);
			resetProc(procIndex);
		}
	}
}

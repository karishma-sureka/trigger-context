package com.trigger_context;

import com.trigger_context.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Main_Service extends Service {

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Trigger_Log", "Main_Service-onDestroy");
	}

	static public Main_Service main_service;
	private int mid = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		main_service = this;
		Thread network = new Thread(new Network());

		network.start();

		try {
			network.join();
		} catch (InterruptedException e) {
			Log.i("Trigger_Log", "Main_Service-onCreate--Error in Join");
		}

		Thread comm_Listener = new Thread(new Comm_Listener(6000));// port for
																	// comm

		Thread node_Listener = new Thread(new Node_Listener(6001));// port for
																	// node

		// Bug here .. get name from shared pre//////////
		Thread keep_Alive = new Thread(new Keep_Alive("name", "hi", 6002,
				"255.255.255.255"));// port for alive
		// comm_Listener.start();
		// node_Listener.start();
		keep_Alive.start();

		Log.i("Trigger_Log", "Main_Service-Oncreate--End");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void noti(String title, String txt) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(txt);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mid++, mBuilder.build());
	}
}

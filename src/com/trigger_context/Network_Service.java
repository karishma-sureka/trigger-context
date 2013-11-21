package com.trigger_context;

import java.io.IOException;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class Network_Service extends Service {

	public static Network_Service ns = null;

	public Map<String, ?> getSharedMap(String name) {
		SharedPreferences conditions = getSharedPreferences(name, MODE_PRIVATE);
		return conditions.getAll();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences users_sp = getSharedPreferences(Main_Service.USERS,
				MODE_PRIVATE);// Mac->user

		// //////////////////////// to be removed

		Editor edit = users_sp.edit();
		edit.putString(Main_Service.ANY_USER, "default");
		edit.commit();
		// ////////////////////////////////////

		ns = this;
		Log.i(Main_Service.LOG_TAG, "Network_Service-Oncreate--End");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (Node_Listener.datagramSocket != null
				&& Node_Listener.datagramSocket.isClosed()) {
			Node_Listener.datagramSocket.close();
		}
		if (Comm_Listener.serverSocket != null
				&& !Comm_Listener.serverSocket.isClosed()) {
			try {
				Comm_Listener.serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// maybe join here
		ns = null;
		Log.i(Main_Service.LOG_TAG, "Network_Service-onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Comm_Listener(Main_Service.COMM_PORT)).start();// Listen
																		// AT
																		// 6000

		new Thread(new Node_Listener(Main_Service.NET_PORT, Network.getMAC()))
				.start();// Listen At 6001

		new Thread(new Keep_Alive(Network.getMAC(), Main_Service.NET_PORT,
				Network.getBIP(), getSharedPreferences(Main_Service.MY_DATA,
						MODE_PRIVATE).getLong("timeout",
						Main_Service.DEFAULT_TIME_OUT))).start();
		return START_STICKY;

	}
}

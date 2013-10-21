package com.trigger_context;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Network_Service extends Service {

	private int mid = 0;
	static String ANY_USER = "00:00:00:00:00:00";
	static String USERS = "users";
	static String MY_DATA = "my_data";

	public void noti(String title, String txt) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(txt);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mid++, mBuilder.build());
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences users_sp = getSharedPreferences(MY_DATA, MODE_PRIVATE);
		SharedPreferences my_data = getSharedPreferences(MY_DATA, MODE_PRIVATE);

		Editor edit = users_sp.edit();
		edit.putString(Network_Service.ANY_USER, "default");
		edit.commit();

		ArrayList<String> users = new ArrayList<String>(users_sp.getAll()
				.keySet());

		// to be removed
		Network.setWifiOn(true);
		Thread z = new Thread(new Network());

		z.start();

		try {
			z.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//

		new Thread(new Comm_Listener(6000)).start();// Listen AT 6000

		new Thread(new Node_Listener(users, 6001, my_data.getString("name",
				"userName"), Network.getMAC())).start();// Listen At 6001

		new Thread(new Keep_Alive(my_data.getString("name", "userName"),
				Network.getMAC(), 6001, Network.getBIP(), 10)).start();// Send
																		// To
																		// 6001

		Log.i(Main_Service.LOG_TAG, "Network_Service-Oncreate--End");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(Main_Service.LOG_TAG, "Network_Service-onDestroy");
	}
}

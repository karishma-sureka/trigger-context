package com.trigger_context;

import java.util.Map;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Main_Service extends Service {

	private int mid;
	public static Main_Service main_Service;

	public Map<String, ?> getSharedMap(String userMac) {
		SharedPreferences conditions = getSharedPreferences(userMac,
				MODE_PRIVATE);
		return conditions.getAll();
	}

	public void noti(String title, String txt) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(txt);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mid++, mBuilder.build());
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		main_Service = this;
		Log.i("Trigger_Log", "Main_Service-onCreate");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Trigger_Log", "Main_Service-onDestory");
	}
}

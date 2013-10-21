package com.trigger_context;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main_Activity extends Activity {

	private int mid = 0;
	public static Main_Activity main_activity;

	public void noti(String title, String txt) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(txt);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mid++, mBuilder.build());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		main_activity = this;
		Start_MainService();
		///////////
		Start_NetworkService();
		// ////////
		Log.i(Main_Service.LOG_TAG, "Main_Activity-onCreate--End");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_, menu);
		return true;
	}

	private void Start_MainService() {
		Context x = getBaseContext();
		Intent startServiceIntent = new Intent(x, Main_Service.class);
		x.startService(startServiceIntent);
		Toast.makeText(x, "Starting Main_Service", Toast.LENGTH_LONG).show();
	}

	private void Start_NetworkService() {
		Context x = getBaseContext();
		Intent startServiceIntent = new Intent(x, Network_Service.class);
		x.startService(startServiceIntent);
		Toast.makeText(x, "Starting Network_Service", Toast.LENGTH_LONG).show();
	}
}

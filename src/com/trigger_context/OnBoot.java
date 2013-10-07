package com.trigger_context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class OnBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startServiceIntent = new Intent(context, Network_Service.class);
		context.startService(startServiceIntent);
		Toast.makeText(context, "Starting Service", Toast.LENGTH_LONG).show();

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info != null) {
			if (info.isConnected()) {

				Network.setWifiOn(true);
				new Thread(new Network()).start();

			}
		} else {

			Network.setWifiOn(false);
		}
		Log.i("Trigger_Log", "OnBoot--End");
	}
}
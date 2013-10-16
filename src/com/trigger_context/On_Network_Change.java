package com.trigger_context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class On_Network_Change extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Main_Service.LOG_TAG, "OnNetworkChange-OnReceive");
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		Intent ServiceIntent = new Intent(context, Network_Service.class);

		if (info != null) {
			if (info.isConnected()) {

				// start service
				context.startService(ServiceIntent);
				Toast.makeText(context, "Starting Network_Service",
						Toast.LENGTH_LONG).show();
				Log.i(Main_Service.LOG_TAG, "OnNetworkChange--Start Service");

				Network.setWifiOn(true);
				new Thread(new Network()).start();

			} else {
				// stop service
				context.stopService(ServiceIntent);
				Toast.makeText(context, "Stoping Network_Service",
						Toast.LENGTH_LONG).show();
				Log.i(Main_Service.LOG_TAG, "OnNetworkChange--Stop Service");

				Network.setWifiOn(false);

			}
		}
	}
}
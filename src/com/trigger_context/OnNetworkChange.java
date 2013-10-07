package com.trigger_context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class OnNetworkChange extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("Trigger_Log", "OnNetworkChange-OnReceive");
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		Intent ServiceIntent = new Intent(context, Network_Service.class);

		if (info != null) {
			if (info.isConnected()) {

				// start service
				context.startService(ServiceIntent);
				Toast.makeText(context, "Starting Service", Toast.LENGTH_LONG)
						.show();
				Log.i("Trigger_Log", "OnNetworkChange--Start Service");

				Network.setWifiOn(true);
				new Thread(new Network()).start();

			} else {
				// stop service
				context.stopService(ServiceIntent);
				Toast.makeText(context, "Stoping Service", Toast.LENGTH_LONG)
						.show();
				Log.i("Trigger_Log", "OnNetworkChange--Stop Service");

				Network.setWifiOn(false);

			}
		}
	}
}
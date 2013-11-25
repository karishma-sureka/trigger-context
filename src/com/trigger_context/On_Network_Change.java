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
		NetworkInfo info = cm
				.getNetworkInfo(android.net.ConnectivityManager.TYPE_WIFI);

		Intent ServiceIntent = new Intent(context, Network_Service.class);

		if (info != null) {
			if (info.isConnected()) {
				Main_Service.wifi = true;
				Network.setWifiOn(true);
				Thread z = new Thread(new Network());

				z.start();

				try {
					z.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(context, "Starting Network_Service",
						Toast.LENGTH_LONG).show();
				// start service
				context.startService(ServiceIntent);

				Log.i(Main_Service.LOG_TAG, "OnNetworkChange--Start Service");

			} else {
				Main_Service.wifi = false;
				Network.setWifiOn(false);
				// stop service
				context.stopService(ServiceIntent);
				Toast.makeText(context, "Stoping Network_Service",
						Toast.LENGTH_LONG).show();
				Log.i(Main_Service.LOG_TAG, "OnNetworkChange--Stop Service");

			}
		}
	}
}
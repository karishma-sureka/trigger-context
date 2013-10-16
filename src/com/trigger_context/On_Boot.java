package com.trigger_context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class On_Boot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startServiceIntent = new Intent(context, Main_Service.class);
		context.startService(startServiceIntent);
		Toast.makeText(context, "Starting Main_Service", Toast.LENGTH_LONG)
				.show();
		Log.i("Trigger_Log", "OnBoot--End");
	}
}
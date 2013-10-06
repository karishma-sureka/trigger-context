package com.trigger_context;

import com.trigger.R;

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

	static Main_Service main_service;
	private int mid = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		main_service = this;
		new Thread(new Comm_Listener(6000)).start();// port for comm
		new Thread(new Node_Listener(6001)).start();// port for node
		// Bug here .. get name from shared pre//////////
		new Thread(new Keep_Alive("name", "hi", 6002)).start();
		Log.i("Main_Service", "Oncreate");
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

	public String getIP() {
		Log.i("Main_Service-getIP", "Start");
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			Log.i("Main_Service-getIP", "Error");
			return "";
		}
	}

	public String getMAC() {
		Log.i("Main_Service-getMAC", "Start");
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}
			return sb.toString();
		} catch (SocketException e) {
			Log.i("Main_Service-getMAC", "Error");
			return "";
		} catch (UnknownHostException e) {
			Log.i("Main_Service-getMac", "Error");
			return "";
		}
	}
}

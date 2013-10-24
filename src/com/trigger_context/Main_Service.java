package com.trigger_context;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.trigger_context.conf.Action_Email_Client;
import com.trigger_context.conf.Action_Open_Url;
import com.trigger_context.conf.Action_Post_Tweet;

public class Main_Service extends Service {

	private class SendData implements Runnable {

		String SerIP;
		int SerPo;
		String mess;

		SendData(String IP, int Po, String mes) {
			SerIP = IP;
			SerPo = Po;
			mess = mes;
		}

		void Nio() {
			try {
				InetAddress serverAddr = InetAddress.getByName(SerIP);
				DatagramSocket socket = new DatagramSocket();
				byte[] rbuf = new byte[1024];// 1024 is size.we can change it
												// our req
				DatagramPacket rpacket = new DatagramPacket(rbuf, rbuf.length);
				byte[] sbuf = mess.getBytes();
				DatagramPacket spacket = new DatagramPacket(sbuf, sbuf.length,
						serverAddr, SerPo);
				socket.send(spacket);
				socket.receive(rpacket);
				byte[] rec = rpacket.getData();
				String recv = new String(rec);
				recv = recv.trim();
				noti("Remote Command Execution :",
						recv.equals("0") ? "Sucessful" : "UnSucesfull");
				socket.close();
			} catch (Exception e) {
				noti("Could not execute remote command due to ", e.toString());
			}
		}

		@Override
		public void run() {
			Nio();

		}

	}

	public static final String LOG_TAG = "Trigger_Log";

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
		Log.i(LOG_TAG, "Main_Service-onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG, "Main_Service-onDestory");
	}

	public synchronized void processUser(String mac) {
		if (testConditions(mac)) {
			takeAction(mac);
		}

	}

	private void takeAction(String mac) {
		noti("comes to ", mac);

		SharedPreferences conditions = getSharedPreferences(mac, MODE_PRIVATE);
		Map<String, ?> cond_map = conditions.getAll();
		Set<String> key_set = cond_map.keySet();
		if (key_set.contains("SmsAction")) {
			String number = conditions.getString("SmsActionNumber", null);
			String message = conditions.getString("SmsActionMessage", null);

			try {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(number, null, message, null, null);
				noti("Sms Sent To : ", "" + number);
			} catch (Exception e) {
				noti("Sms Sending To ", number + "Failed");
			}
		}
		if (key_set.contains("OpenWebsiteAction")) {
			Intent dialogIntent = new Intent(getBaseContext(),
					Action_Open_Url.class);
			dialogIntent.putExtra("urlAction",
					(String) cond_map.get("urlAction"));
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);

		}
		if (key_set.contains("ToggleAction")) {
			if (key_set.contains("bluetoothAction")
					&& conditions.getBoolean("bluetoothAction", false)) {
				final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();
				bluetoothAdapter.enable();
			} else if (key_set.contains("bluetoothAction")) {
				final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();
				bluetoothAdapter.disable();
			}
			if (key_set.contains("wifiAction")
					&& conditions.getBoolean("wifiAction", false)) {
				final WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				wm.setWifiEnabled(true);
			} else if (key_set.contains("wifiAction")) {
				final WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				wm.setWifiEnabled(false);
			}
		}
		if (key_set.contains("TweetAction")) {
			Intent dialogIntent = new Intent(getBaseContext(),
					Action_Post_Tweet.class);
			dialogIntent.putExtra("tweetTextAction",
					(String) cond_map.get("tweetTextAction"));
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);
		}

		if (key_set.contains("EmailAction")) {
			Intent dialogIntent = new Intent(getBaseContext(),
					Action_Email_Client.class);
			dialogIntent
					.putExtra("toAction", (String) cond_map.get("toAction"));
			dialogIntent.putExtra("subjectAction",
					(String) cond_map.get("subjectAction"));
			dialogIntent.putExtra("emailMessageAction",
					(String) cond_map.get("emailMessageAction"));
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);
		}
		if (key_set.contains("RemoteServerCmd")) {
			String text = conditions.getString("cmd", null);
			new Thread(new SendData("224.0.0.1", 9876, text)).start();
		}
		// to do : network related part
	}

	private boolean testConditions(String mac) {
		SharedPreferences conditions = getSharedPreferences(mac, MODE_PRIVATE);
		Map<String, ?> cond_map = conditions.getAll();
		Set<String> key_set = cond_map.keySet();
		boolean takeAction = true;
		if (key_set.contains("bluetooth")) {
			// checking the current state against the state set by the user
			final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			takeAction = new Boolean(bluetoothAdapter.isEnabled())
					.equals(conditions.getString("bluetooth", "false"));
		}
		if (takeAction && key_set.contains("wifi")) {
			final WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			takeAction = new Boolean(wm.isWifiEnabled()) == conditions
					.getBoolean("wifi", false);
		}

		if (takeAction && key_set.contains("gps")) {
			final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			takeAction = new Boolean(
					locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER))
					.equals(conditions.getString("gps", "false"));
		}
		if (takeAction && key_set.contains("sms")) {
			final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
			Cursor c = getContentResolver().query(SMS_INBOX, null, "read = 0",
					null, null);
			if (c != null) {
				int unreadMessagesCount = c.getCount();
				c.close();
				takeAction = new Boolean(unreadMessagesCount > 0)
						.equals(conditions.getString("sms", "false"));
			} else {
				takeAction = false;
			}
		}

		// "NOT TESTED" head set, missed call, accelerometer, proximity, gyro,
		// orientation
		if (takeAction && key_set.contains("headset")) {
			AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			takeAction = am.isMusicActive() == conditions.getBoolean("headset",
					false);
			// am.isWiredHeadsetOn() is deprecated

		}
		/*
		 * if(takeAction && key_set.contains("missedCall")) { final String[]
		 * projection = null; final String selection = null; final String[]
		 * selectionArgs = null; final String sortOrder =
		 * android.provider.CallLog.Calls.DATE + " DESC"; Cursor cursor = null;
		 * try{ cursor = getApplicationContext().getContentResolver().query(
		 * Uri.parse("content://call_log/calls"), projection, selection,
		 * selectionArgs, sortOrder); while (cursor.moveToNext()) { String
		 * callLogID =
		 * cursor.getString(cursor.getColumnIndex(android.provider.CallLog
		 * .Calls._ID)); String callNumber =
		 * cursor.getString(cursor.getColumnIndex
		 * (android.provider.CallLog.Calls.NUMBER)); String callDate =
		 * cursor.getString
		 * (cursor.getColumnIndex(android.provider.CallLog.Calls.DATE)); String
		 * callType =
		 * cursor.getString(cursor.getColumnIndex(android.provider.CallLog
		 * .Calls.TYPE)); String isCallNew =
		 * cursor.getString(cursor.getColumnIndex
		 * (android.provider.CallLog.Calls.NEW)); if(Integer.parseInt(callType)
		 * == android.provider.CallLog.Calls.MISSED_CALL_TYPE &&
		 * Integer.parseInt(isCallNew) > 0){
		 * 
		 * } } }catch(Exception ex){ }finally{ cursor.close(); }
		 * 
		 * }
		 */
		return takeAction;
	}
}

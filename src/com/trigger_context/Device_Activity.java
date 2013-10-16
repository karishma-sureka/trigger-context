package com.trigger_context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Device_Activity extends Activity {

	class DeviceDiscovery implements Runnable {

		private DatagramSocket datagramSocket;
		private byte[] buf;
		private DatagramPacket packet;
		private String userData;
		private String[] userDataArray;

		public DeviceDiscovery(int Port) {

			buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			try {
				datagramSocket = new DatagramSocket(Port);
			} catch (SocketException e) {
				Log.i(Main_Service.LOG_TAG,
						"Device-Activity-DeviceDisc-Constructor--Error in Bind");
			}
		}

		@Override
		public void run() {
			Log.i(Main_Service.LOG_TAG,
					"Device-Activity-DeviceDisc-Run Thread Start");
			while (Flag) {
				try {
					datagramSocket.receive(packet);
					userData = new String(packet.getData(), "UTF-8");
					userData = userData.substring(0, packet.getLength());
					userDataArray = userData.split(";");
					DeviceList
							.add(userDataArray[0] + " -> " + userDataArray[1]);
					Device_Activity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							arrayAdapter.notifyDataSetChanged();
						}
					});
				} catch (UnsupportedEncodingException e) {
					Log.i(Main_Service.LOG_TAG,
							"Device-Activity-DeviceDisc-Run--Error in getData()");
				} catch (IOException e) {
					Log.i(Main_Service.LOG_TAG,
							"Device-Activity-DeviceDisc-Run--Error in receive()");
				}

			}
		}
	}

	class NewSendBroadcast implements Runnable {

		private String Name, MAC;
		private DatagramSocket socket;
		private int Port;
		private InetAddress BIP;
		private DatagramPacket sendPackcast;

		public NewSendBroadcast(String Name, String MAC, int Port,
				InetAddress inetAddress) {
			this.Name = Name;
			this.MAC = MAC;
			this.Port = Port;
			this.BIP = inetAddress;
			try {
				socket = new DatagramSocket();
				socket.setBroadcast(true);
			} catch (SocketException e) {
				Log.i(Main_Service.LOG_TAG,
						"SendBordcast--Error Unable to bind to port");
			}
			Log.i(Main_Service.LOG_TAG, "SendBordcast--Constructor");
		}

		@Override
		public void run() {
			Log.i(Main_Service.LOG_TAG, "SendBordcast-run--Started");

			String Packet = (Name + ";" + MAC + ";1");
			byte[] SendData = Packet.getBytes();
			sendPackcast = new DatagramPacket(SendData, SendData.length, BIP,
					Port);

			try {
				socket.send(sendPackcast);
			} catch (IOException e) {
				Log.i(Main_Service.LOG_TAG,
						"Device_Activity-NewSendBoradcast-run-- Error in send");
			}

		}

	}

	ArrayAdapter<String> arrayAdapter;

	boolean Flag;

	ArrayList<String> DeviceList = new ArrayList<String>();
	static String ANY_USER = "00:00:00:00:00:00";
	static String USERS = "users";
	static String MY_DATA = "my_data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		Flag = true;
		if (Network.isWifiOn()) {

			SharedPreferences users_sp = getSharedPreferences(USERS,
					MODE_PRIVATE);
			SharedPreferences my_data = getSharedPreferences(MY_DATA,
					MODE_PRIVATE);
			ArrayList<String> users = new ArrayList<String>(users_sp.getAll()
					.keySet());

			Thread devicedis = new Thread(new DeviceDiscovery(6002));

			Thread sendbroad = new Thread(new NewSendBroadcast(
					my_data.getString("name", "userName"), Network.getMAC(),
					6001, Network.getBIP()));

			sendbroad.start();

			try {
				sendbroad.join();
			} catch (InterruptedException e) {
				Log.i(Main_Service.LOG_TAG,
						"Device_Activity-onCreate--Error in join");

			}
			Toast.makeText(getBaseContext(), "Starting Device Discovery",
					Toast.LENGTH_LONG).show();

			devicedis.start();

		}

		ListView lv = (ListView) findViewById(R.id.DeviceList);
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, DeviceList);
		lv.setAdapter(arrayAdapter);

		Log.i(Main_Service.LOG_TAG, "Device-Activity-onCreate--End");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device_, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Flag = false;
		Log.i(Main_Service.LOG_TAG, "Device_Activity-onDestroy");
	}
}
package com.trigger_context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Device_Activity extends Activity {

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Flag = false;
		Log.i(Main_Service.LOG_TAG, "Device_Activity-onDestroy");
	}

	boolean Flag ;

	class DeviceDiscovery implements Runnable {

		private DatagramSocket datagramSocket;
		private int Port;
		private byte[] buf;
		private DatagramPacket packet;
		private String userData;
		private String[] userDataArray;

		public DeviceDiscovery(int Port) {
			packet = new DatagramPacket(buf, buf.length);
			buf = new byte[256];
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
					"Device-Activity-DeviceDisc-Constructor--Error in Bind");
			while (Flag) {
				try {
					datagramSocket.receive(packet);
					userData = new String(packet.getData(), "UTF-8");
					userData = userData.substring(0, packet.getLength());
					userDataArray = userData.split(";");
					DeviceList.add(userDataArray[0]+userDataArray[1]);
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

	ArrayList<String> DeviceList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		Flag = true;
		
//		new Thread(new DeviceDiscovery(6002)).start();;

		ListView lv = (ListView) findViewById(R.id.DeviceList); 
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, DeviceList);
		lv.setAdapter(arrayAdapter);
		
		Log.i(Main_Service.LOG_TAG,"Device-Activity-onCreate--End");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device_, menu);
		return true;
	}
}

package com.trigger_context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.util.Log;

public class Node_Listener implements Runnable {
	private ArrayList<String> macAddressListActive = new ArrayList<String>();
	private ArrayList<String> macAddressListSet;
	private SharedPreferences users;
	private DatagramSocket datagramSocket, typeSocket;
	private DatagramPacket myPacket;
	private int Port;

	public Node_Listener(ArrayList<String> storedList, int port, String name,
			String mac) {
		macAddressListSet = storedList;
		this.Port = port;
		String myData = name + ";" + mac;
		byte[] myBuf = null;
		try {
			myBuf = myData.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		myPacket = new DatagramPacket(myBuf, myBuf.length);
		Log.i("Trigger_Log", "Node_Listener--constructor end");

	}

	@Override
	public void run() {
		Log.i("Trigger_Log", "Node_Listener-run--start");

		try {
			datagramSocket = new DatagramSocket(Port);
		} catch (SocketException e) {
			Log.i("Trigger_Log", "Node_Listener--Error in Create Socket");

		}
		byte[] buf = new byte[256];
		String userData;
		String[] userDataArray;
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		while (true) {
			try {
				datagramSocket.receive(packet);
				userData = new String(packet.getData(), "UTF-8");
				userData = userData.substring(0, packet.getLength());
				userDataArray = userData.split(";");// name;MAC;type
				if (userDataArray[2].equals(Network.getMAC())) {
					String replyType = new String("1".getBytes(), "UTF-8");
					if (!macAddressListActive.contains(userDataArray[1])) {
						macAddressListActive.add(userDataArray[1]);
						// processing - trigger on arrival - any user or saved
						// user
						if (macAddressListSet.contains(userDataArray[1])) {
							new Thread(new ProcessUser(
									Network_Service.network_Service
											.getSharedMap(userDataArray[1])))
									.start();
						}// ^vj was here
						else if (macAddressListSet
								.contains(Network_Service.ANY_USER)) {
							new Thread(
									new ProcessUser(
											Network_Service.network_Service
													.getSharedMap(Network_Service.ANY_USER)))
									.start();
						}
						// any user
					}
					if (userDataArray[2].equals(replyType)) {
						typeSocket = new DatagramSocket();
						typeSocket.send(myPacket);
					}
					Log.i("Trigger_Log", "Node_Listener--Packet-" + userData);
				}
			} catch (IOException e) {
				Log.i("Trigger_Log", "Node_Listener-run--Error in receive");
			}
		}

	}

}
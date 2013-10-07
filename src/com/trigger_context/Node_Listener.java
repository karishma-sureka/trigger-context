package com.trigger_context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import android.content.SharedPreferences;

public class Node_Listener implements Runnable {
	ArrayList<String> macAddressListActive = new ArrayList<String>();
	ArrayList<String> macAddressListSet;
	SharedPreferences users;
	DatagramSocket datagramSocket, typeSocket;
	DatagramPacket myPacket;

	public Node_Listener(ArrayList<String> storedList, int port, String name,
			String mac) {
		macAddressListSet = storedList;
		String myData = name + ";" + mac;
		byte[] myBuf = null;
		try {
			myBuf = myData.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		myPacket = new DatagramPacket(myBuf, myBuf.length);
	}

	@Override
	public void run() {
		try {
			datagramSocket = new DatagramSocket(6660);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		byte[] buf = new byte[256];
		String userData;
		String[] userDataArray;
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		while (true) {
			try {
				datagramSocket.receive(packet);
				userData = new String(packet.getData(), "UTF-8");
				userDataArray = userData.split(";");// name;MAC;type
				String replyType = new String("1".getBytes(), "UTF-8");
				if (!macAddressListActive.contains(userDataArray[1])) {
					macAddressListActive.add(userDataArray[1]);
					// processing - trigger on arrival - any user or saved user
					if (macAddressListSet.contains(userDataArray[1])) {
						new Thread(new ProcessUser(
								Main_Service.main_service
										.getSharedMap(userDataArray[1])))
								.start();
					}// ^vj was here
					else if (macAddressListSet.contains(Main_Service.ANY_USER)) {
						new Thread(new ProcessUser(
								Main_Service.main_service
										.getSharedMap(Main_Service.ANY_USER)))
								.start();
					}
					// any user
				}
				if (userDataArray[2].equals(replyType)) {
					typeSocket = new DatagramSocket();
					typeSocket.send(myPacket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
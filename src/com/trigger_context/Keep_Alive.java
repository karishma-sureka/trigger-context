package com.trigger_context;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Keep_Alive implements Runnable {

	String Name, MAC;
	DatagramSocket socket;
	int Port;

	public Keep_Alive(String Name, String MAC, int Port) {
		this.Name = Name;
		this.MAC = MAC;
		this.Port = Port;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			Log.i("Keep_Alive", "Error Unable to bind to port");
		}
		Log.i("Keep_Alive", "Constructor");
	}

	@Override
	public void run() {
		Log.i("Keep_Alive-run", "Started");

		String Packet = (Name + ";" + MAC + ";2");

		byte[] SendData = Packet.getBytes();

		try {
			DatagramPacket sendPack = new DatagramPacket(SendData,
					SendData.length, InetAddress.getByName("255.255.255.255"),
					Port);
		} catch (UnknownHostException e) {
			Log.i("Keep_Alive-run", "Error in getbyhostname");
		}
	}
}

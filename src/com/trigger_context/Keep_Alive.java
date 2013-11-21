package com.trigger_context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;

public class Keep_Alive implements Runnable {

	private String MAC, name;
	static DatagramSocket socket = null;
	private int Port;
	private InetAddress BIP;
	private long TimeOut;
	private String data = null;
	private byte[] byteData = null;
	private DatagramPacket pkt = null;

	public Keep_Alive(String MAC, int Port, InetAddress bcastIp, long TimeOut) {
		this.MAC = MAC;
		this.Port = Port;
		this.BIP = bcastIp;
		this.TimeOut = TimeOut;
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
		} catch (SocketException e) {
			Log.i(Main_Service.LOG_TAG,
					"Keep_Alive--Error Unable to bind to port");
		}
		Log.i(Main_Service.LOG_TAG, "Keep_Alive--Constructor");
	}

	@Override
	public void run() {
		Log.i(Main_Service.LOG_TAG, "Keep_Alive-run--Started");

		while (Main_Service.wifi) {
			name = (String) Network_Service.ns.getSharedMap(
					Main_Service.MY_DATA).get("name");
			if (name == null) {
				name = Main_Service.DEFAULT_USER_NAME;
			}
			data = name + ";" + MAC + ";2";
			byteData = data.getBytes();
			pkt = new DatagramPacket(byteData, byteData.length, BIP, Port);
			try {
				socket.send(pkt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(TimeOut * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		socket.close();

		Log.i(Main_Service.LOG_TAG, "Keep_Alive-ending");
	}

}

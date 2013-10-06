package com.trigger_context;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Keep_Alive implements Runnable {

	private String Name, MAC;
	private DatagramSocket socket;
	private int Port;
	private InetAddress BIP;

	public Keep_Alive(String Name, String MAC, int Port, InetAddress inetAddress) {
		this.Name = Name;
		this.MAC = MAC;
		this.Port = Port;
		this.BIP = inetAddress;
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
		} catch (SocketException e) {
			Log.i("Trigger_Log", "Keep_Alive--Error Unable to bind to port");
		}
		Log.i("Trigger_Log", "Keep_Alive--Constructor");
	}

	@Override
	public void run() {
		Log.i("Trigger_Log", "Keep_Alive-run--Started");

		String Packet = (Name + ";" + MAC + ";2");
		byte[] SendData = Packet.getBytes();

		try {
			DatagramPacket sendPack = new DatagramPacket(SendData,
					SendData.length, BIP, Port);
			socket.send(sendPack);
			Log.i("Trigger_Log", "Port:" + socket.getLocalPort());
		} catch (UnknownHostException e) {
			Log.i("Trigger_Log", "Keep_Alive-run--Error in getbyhostname");
		} catch (IOException e) {
			Log.i("Trigger_Log", "Keep_Alive-run--Error in send"); 
		}
	}
}

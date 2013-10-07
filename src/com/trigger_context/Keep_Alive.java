package com.trigger_context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class Keep_Alive implements Runnable {

	private String Name, MAC;
	static DatagramSocket socket;
	private int Port;
	private InetAddress BIP;
	private long TimeOut;

	public Keep_Alive(String Name, String MAC, int Port,
			InetAddress inetAddress, long TimeOut) {
		this.Name = Name;
		this.MAC = MAC;
		this.Port = Port;
		this.BIP = inetAddress;
		this.TimeOut = TimeOut;
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
		Sendbroadcast.sendPackcast = new DatagramPacket(SendData,
				SendData.length, BIP, Port);
		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Thread(new Sendbroadcast()), 5, TimeOut,
				TimeUnit.SECONDS);
	}

}

class Sendbroadcast implements Runnable {
	static DatagramPacket sendPackcast;

	@Override
	public void run() {
		try {
			Keep_Alive.socket.send(sendPackcast);
		} catch (UnknownHostException e) {
			Log.i("Trigger_Log", "Keep_Alive-run--Error in getbyhostname");
		} catch (IOException e) {
			Log.i("Trigger_Log", "Keep_Alive-run--Error in send");
		}
	}
}
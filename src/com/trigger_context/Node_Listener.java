package com.trigger_context;

import android.util.Log;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Node_Listener implements Runnable {
	private int port;
	private DatagramSocket socket;
	private ArrayList<String> NewUser = new ArrayList<String>();
	private ArrayList<String> LeftUser = new ArrayList<String>();
	private ArrayList<String> CurUser = new ArrayList<String>();

	public Node_Listener(int port) {
		this.port = port;

		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			Log.i("Node_Listener",
					String.format("Unable to bind to port:%d", port));
		}
	}

	@Override
	public void run() {
		Log.i("Node_Listener", "Started Thread");
		byte[] receiveData = new byte[1024];
	}
}

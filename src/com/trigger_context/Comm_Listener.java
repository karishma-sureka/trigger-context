package com.trigger_context;

import android.util.Log;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Comm_Listener implements Runnable {
	private int port;
	private ServerSocket serverSocket;

	public Comm_Listener(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			Log.i("Comm_Listener",
					String.format("Error Unable to bind to port:%d", port));
		}
		Log.i("Comm_Listener", "Constructor");
	}

	@Override
	public void run() {
		Log.i("Comm_Listener", "Started Thread");
		Socket req;
		while (true) {
			try {
				req = serverSocket.accept();
				new Thread(new Cond_Action(req)).start();
			} catch (IOException e) {
				Log.i("Comm_Listener-Run", "Error Accept");
			}

		}
	}
}

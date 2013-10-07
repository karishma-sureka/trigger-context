package com.trigger_context;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class Comm_Listener implements Runnable {
	private int port;
	private ServerSocket serverSocket;

	public Comm_Listener(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			Log.i("Trigger_Log",
					"Comm_Listener"
							+ String.format(
									"--Error Unable to bind to port:%d", port));
		}
		Log.i("Trigger_Log", "Comm_Listener--Constructor");
	}

	@Override
	public void run() {
		Log.i("Trigger_Log", "Comm_Listener--Started Thread");
		Socket req;
		while (true) {
			try {
				req = serverSocket.accept();
				new Thread(new Cond_Action(req)).start();
				Log.i("Trigger_Log", "Comm_Listener--New Connection Req From "
						+ req.getInetAddress() + ":" + req.getLocalPort());
			} catch (IOException e) {
				Log.i("Trigger_Log", "Comm_Listener-Run--Error Accept");
			}

		}
	}
}

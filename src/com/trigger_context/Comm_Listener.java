package com.trigger_context;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class Comm_Listener implements Runnable {
	private int port;
	public static ServerSocket serverSocket = null;

	public Comm_Listener(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			Log.i(Main_Service.LOG_TAG,
					"Comm_Listener"
							+ String.format(
									"--Error Unable to bind to port:%d", port));
		}
		Log.i(Main_Service.LOG_TAG, "Comm_Listener--Constructor");
	}

	@Override
	public void run() {
		Log.i(Main_Service.LOG_TAG, "Comm_Listener--Started Thread");
		Socket req = null;
		while (Main_Service.wifi) {
			try {
				req = serverSocket.accept();
				new Thread(new Cond_Action(req)).start();
				Log.i(Main_Service.LOG_TAG,
						"Comm_Listener--New Connection Req From "
								+ req.getInetAddress() + ":"
								+ req.getLocalPort());
			} catch (IOException e) {
				Log.i(Main_Service.LOG_TAG, "Comm_Listener-Run--Error Accept");
			}

		}
		try {
			if (!serverSocket.isClosed()) {
				serverSocket.close();
			}
			serverSocket = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

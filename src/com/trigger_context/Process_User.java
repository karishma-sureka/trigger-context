package com.trigger_context;

import java.net.InetAddress;

public class Process_User implements Runnable {

	String processMac = null;
	InetAddress ip = null;

	public Process_User(String newMac, InetAddress newIp) {
		processMac = newMac;
		ip = newIp;
	}

	@Override
	public void run() {
		Main_Service.main_Service.processUser(processMac, ip);
	}

}

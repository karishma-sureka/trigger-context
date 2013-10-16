package com.trigger_context;

public class Process_User implements Runnable {

	String processMac = null;

	public Process_User(String newMac) {
		processMac = newMac;
	}

	@Override
	public void run() {
		Main_Service.main_Service.processUser(processMac);
	}

}

package com.trigger_context;


public class ProcessUser implements Runnable {
	
	String processMac = null;
	public ProcessUser(String newMac) {
		processMac = newMac;
	}

	@Override
	public void run() {
		Main_Service.main_Service.processUser(processMac);
	}

}

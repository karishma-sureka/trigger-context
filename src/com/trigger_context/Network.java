package com.trigger_context;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.content.Context;

public class Network implements Runnable {

	static private boolean WifiOn;
	static private String IP;
	static private String MAC;

	public static boolean isWifiOn() {
		return WifiOn;
	}

	public static void setWifiOn(boolean wifiOn) {
		WifiOn = wifiOn;
	}

	public static synchronized String getIP() {
		if (isWifiOn())
			return IP;
		else
			return null;
	}

	private static synchronized void setIP(String iP) {
		IP = iP;
	}

	public static synchronized String getMAC() {
		if (isWifiOn())
			return MAC;
		else
			return null;
	}

	private static synchronized void setMAC(String mAC) {
		MAC = mAC;
	}

	private String getDeviceIP() {
		Log.i("Trigger_Log", "Network-getDeviceIP--Start");
		WifiManager wifiManager = (WifiManager) Main_Service.main_service
				.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ipBinary = Integer.toBinaryString(ipAddress);

		while (ipBinary.length() < 32) {
			ipBinary = "0" + ipBinary;
		}

		String a = ipBinary.substring(0, 8);
		String b = ipBinary.substring(8, 16);
		String c = ipBinary.substring(16, 24);
		String d = ipBinary.substring(24, 32);

		String actualIpAddress = Integer.parseInt(d, 2) + "."
				+ Integer.parseInt(c, 2) + "." + Integer.parseInt(b, 2) + "."
				+ Integer.parseInt(a, 2);
		return actualIpAddress;
	}

	private static String getDeviceMAC() {
		Log.i("Trigger_Log", "Network-getDeviceMAC--Start");
		WifiManager wifiManager = (WifiManager) Main_Service.main_service
				.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	@Override
	public void run() {
		Log.i("Trigger_Log", "Network--Start Thread");
		setIP(getDeviceIP());
		setMAC(getDeviceMAC());
	}
}
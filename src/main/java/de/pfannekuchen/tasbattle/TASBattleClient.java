package de.pfannekuchen.tasbattle;

import java.net.InetAddress;

import net.fabricmc.api.ClientModInitializer;

public final class TASBattleClient implements ClientModInitializer {

	/** This Variable will be set in {@link #onInitializeClient()} and will hold information whether the Client has Internet Access, or not. */
	public static boolean isConnected;
	
	/** Server Address of TAS Battle Server */
	public static final String SERVER = "mgnet.work";
	/** Port of TAS Battle Server*/
	public static final short PORT = 25566;
	
	@Override
	public void onInitializeClient() {
		/* Check Connection to the Internet to enabled or disable connect button */
		try {
			System.out.println("Client is " + ((isConnected = checkConnection()) ? "connected" : "not connected") + " to the Internet");
		} catch (final Exception e) {
			isConnected = true;
			System.out.println("Couldn't check connection, assuming client is connected.");
		}
	}

	/** Method to check the Connection to {@link #SERVER} */
	private final static boolean checkConnection() throws Exception { return SERVER.equals(InetAddress.getByName(SERVER).getHostAddress().toString()); }
	
}
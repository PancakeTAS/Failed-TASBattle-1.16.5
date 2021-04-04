package de.pfannekuchen.tasbattle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
	private static boolean checkConnection() {
	    try {
	        final URL url = new URL("http://" + SERVER);
	        final URLConnection conn = url.openConnection();
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	    	e.printStackTrace();
	        return false;
	    }
	}
	
}
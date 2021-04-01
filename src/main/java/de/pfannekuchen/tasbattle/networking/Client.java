package de.pfannekuchen.tasbattle.networking;

import java.net.Socket;
import java.util.ArrayList;

import de.pfannekuchen.tasbattle.TASBattleClient;
import de.pfannekuchen.tasbattleserver.packets.GameRunPacket;
import de.pfannekuchen.tasbattleserver.packets.HandshakePacket;
import de.pfannekuchen.tasbattleserver.packets.LoginPacket;
import de.pfannekuchen.tasbattleserver.packets.UpdatePlayersPacket;
import work.mgnet.packetlib.handler.ConnectionHandler;
import work.mgnet.packetlib.uses.Packet;
import work.mgnet.packetlib.uses.PacketEvent;

public class Client {

	private static ConnectionHandler handler;
	public static HandshakePacket settings;
	public static UpdatePlayersPacket connectedPlayers = new UpdatePlayersPacket(new ArrayList<>(), 0, 0, 0);
	
	public static boolean isOnline;
	public static String playername;
	
	public static boolean shouldFFA;
	public static int ffaWhen = -1;
	
	/* Connects to the Server and sends Magic Bytes */
	public static void connect() throws Exception {
		handler = new ConnectionHandler(new Socket("127.0.0.1", TASBattleClient.PORT));
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					if (ffaWhen > 0) ffaWhen--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		handler.addEvent(new PacketEvent() {
			@Override
			public void onPacket(Packet packet, Socket socket, int id) {
				if (packet.getName().equalsIgnoreCase("HandshakePacket")) {
					System.out.println("[Networking] Recieved Handshake Packet from Server.");
					settings = (HandshakePacket) packet;
					
				} else if (packet.getName().equalsIgnoreCase("UpdatePlayersPacket")) {
					System.out.println("[Networking] Updating Playerlist");
					connectedPlayers = ((UpdatePlayersPacket) packet);
				} else if (packet.getName().equalsIgnoreCase("GameRunPacket")) {
					shouldFFA = ((GameRunPacket) packet).startTimer;
					ffaWhen = ((GameRunPacket) packet).time;
				}
			}
		});
		handler.sendPacket(new LoginPacket(playername, isOnline));
	}
	
	/* Closes the Socket again */
	public static void disconnect() throws Exception {
		handler.close();
	}
	
	/* Serialize a Packet and send it to the Client */
	public static void sendPacket(Packet p) throws Exception {
		handler.sendPacket(p);
	}
	
}

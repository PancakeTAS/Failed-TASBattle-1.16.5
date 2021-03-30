package de.pfannekuchen.tasbattle.networking;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import de.pfannekuchen.tasbattle.TASBattleClient;
import de.pfannekuchen.tasbattleserver.packets.HandshakePacket;
import de.pfannekuchen.tasbattleserver.packets.LoginPacket;
import de.pfannekuchen.tasbattleserver.packets.UpdatePlayersPacket;
import net.minecraft.client.MinecraftClient;
import work.mgnet.packetlib.handler.ConnectionHandler;
import work.mgnet.packetlib.uses.Packet;
import work.mgnet.packetlib.uses.PacketEvent;

public class Client {

	private static ConnectionHandler socket;
	public static HandshakePacket settings;
	public static ArrayList<String> connectedPlayers = new ArrayList<>();
	
	/* Connects to the Server and sends Magic Bytes */
	public static void connect() throws Exception {
		ConnectionHandler handler = new ConnectionHandler(new Socket("127.0.0.1", TASBattleClient.PORT));
		handler.addEvent(new PacketEvent() {
			@Override
			public void onPacket(Packet packet, Socket socket, int id) {
				if (packet.getName().equalsIgnoreCase("HandshakePacket")) {
					System.out.println("[Networking] Recieved Handshake Packet from Server.");
					settings = (HandshakePacket) packet;
					
				} else if (packet.getName().equalsIgnoreCase("UpdatePlayersPacket")) {
					System.out.println("[Networking] Updating Playerlist");
					connectedPlayers = ((UpdatePlayersPacket) packet).players;
				}
			}
		});
		handler.sendPacket(new LoginPacket(MinecraftClient.getInstance().getName(), false)); // TODO: Change with actual online check.
	}
	
	/* Closes the Socket again */
	public static void disconnect() throws IOException {
		socket.getTarget().close();
	}
	
	/* Serialize a Packet and send it to the Client */
	public static void sendPacket(Packet p) throws Exception {
		socket.sendPacket(p);
	}
	
}

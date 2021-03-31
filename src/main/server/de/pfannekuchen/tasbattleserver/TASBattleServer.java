package de.pfannekuchen.tasbattleserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import de.pfannekuchen.tasbattleserver.packets.HandshakePacket;
import de.pfannekuchen.tasbattleserver.packets.LoginPacket;
import de.pfannekuchen.tasbattleserver.packets.UpdatePlayersPacket;
import de.pfannekuchen.tasbattleserver.settings.Movement;
import work.mgnet.packetlib.handler.ServerHandler;
import work.mgnet.packetlib.main.PacketLib;
import work.mgnet.packetlib.uses.Packet;
import work.mgnet.packetlib.uses.PacketEvent;
import work.mgnet.packetlib.uses.ServerEvent;

public class TASBattleServer {
	
	private static HandshakePacket gameSettings;
	private static final File properties = new File("battle.properties");
	public static HashMap<Socket, LoginPacket> clients = new HashMap<Socket, LoginPacket>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
		Properties settings = new Properties();
		if (!properties.exists()) properties.createNewFile();
		settings.load(new FileInputStream(properties));
		gameSettings = new HandshakePacket();
		gameSettings.optifineAllowed = Boolean.parseBoolean(settings.getProperty("optifineAllowed"));
		gameSettings.sodiumAllowed = Boolean.parseBoolean(settings.getProperty("sodiumAllowed"));
		gameSettings.currentMovementOption = Movement.valueOf(settings.getProperty("currentMovementOption").toUpperCase());
		gameSettings.skywarsMap = settings.getProperty("skywarsMap");
		gameSettings.bedwarsMap = settings.getProperty("bedwarsMap");
		gameSettings.ffaMap = settings.getProperty("ffaMap");
		gameSettings.tickrate = Integer.parseInt(settings.getProperty("tickrate"));
		
		ServerHandler server = new ServerHandler(PacketLib.openServer(25566));
		server.addEvent(new PacketEvent() {
			
			@Override
			public void onPacket(Packet packet, Socket socket, int id) {
				if (packet.getName().equalsIgnoreCase("LoginPacket")) {
					TASBattleServer.clients.put(socket, (LoginPacket) packet);
					ArrayList<String> pList = new ArrayList<String>();
					clients.forEach((c, b) -> {
						pList.add(b.username + (b.isOnline ? "" : " [Offline]"));
					});
					UpdatePlayersPacket refreshPlayers = new UpdatePlayersPacket(pList);
					clients.forEach((c, b) -> {
						try {
							server.sendPacket(refreshPlayers, c);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
			
		});
		server.addEvent(new ServerEvent() {
			
			@Override
			public void onRegister(Socket socket, int id) {
				try {
					server.sendPacket(gameSettings, socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onExit(Socket socket) {
				
			}
			
			@Override
			public void onError(Socket socket, String error) {
				
			}
			
			@Override
			public void onDisconnect(Socket socket) {
				TASBattleServer.clients.remove(socket);
				ArrayList<String> pList = new ArrayList<String>();
				clients.forEach((c, b) -> {
					pList.add(b.getName() + (b.isOnline ? "" : " [Offline]"));
				});
				UpdatePlayersPacket refreshPlayers = new UpdatePlayersPacket(pList);
				clients.forEach((c, b) -> {
					try {
						server.sendPacket(refreshPlayers, c);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
			
			@Override
			public void onConnect(Socket socket) {
				
			}
		});
	}
	
}

package de.pfannekuchen.tasbattleserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import de.pfannekuchen.tasbattleserver.packets.GameRunPacket;
import de.pfannekuchen.tasbattleserver.packets.HandshakePacket;
import de.pfannekuchen.tasbattleserver.packets.JoinQueuePacket;
import de.pfannekuchen.tasbattleserver.packets.LoginPacket;
import de.pfannekuchen.tasbattleserver.packets.UpdatePlayersPacket;
import work.mgnet.packetlib.handler.ServerHandler;
import work.mgnet.packetlib.main.PacketLib;
import work.mgnet.packetlib.uses.Packet;
import work.mgnet.packetlib.uses.PacketEvent;
import work.mgnet.packetlib.uses.ServerEvent;

public class TASBattleServer {
	
	private static HandshakePacket gameSettings;
	private static final File properties = new File("battle.properties");
	public static HashMap<Socket, LoginPacket> clients = new HashMap<Socket, LoginPacket>();
	
	public static volatile List<Socket> queueFFA = new ArrayList<>();
	public static int ffaCooldown = -1;
	
	public static boolean isFFARunning = false;
	
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					ffaCooldown--;
					if (ffaCooldown == 0) {
						// TODO: Start the game.
						System.out.println("Starting new FFA Game");
						isFFARunning = true;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		Properties settings = new Properties();
		if (!properties.exists()) properties.createNewFile();
		settings.load(new FileInputStream(properties));
		gameSettings = new HandshakePacket();
		gameSettings.skywarsMap = settings.getProperty("skywarsMap");
		gameSettings.bedwarsMap = settings.getProperty("bedwarsMap");
		gameSettings.ffaMap = settings.getProperty("ffaMap");
		gameSettings.bedwarsTickrate = Integer.parseInt(settings.getProperty("bedwarsTickrate"));
		gameSettings.skywarsTickrate = Integer.parseInt(settings.getProperty("skywarsTickrate"));
		gameSettings.ffaTickrate = Integer.parseInt(settings.getProperty("ffaTickrate"));
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
					UpdatePlayersPacket refreshPlayers = new UpdatePlayersPacket(pList, queueFFA.size(), 0, 0);
					clients.forEach((c, b) -> {
						try {
							server.sendPacket(refreshPlayers, c);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				} else if (packet.getName().equalsIgnoreCase("JoinQueuePacket")) {
					if (((JoinQueuePacket) packet).queue.equalsIgnoreCase("ffa")) {
						
						
						if (queueFFA.contains(socket)) queueFFA.remove(socket);
						else if (!isFFARunning) queueFFA.add(socket);
						
						
						// Update Clients
						ArrayList<String> pList = new ArrayList<String>();
						clients.forEach((c, b) -> {
							pList.add(b.username + (b.isOnline ? "" : " [Offline]"));
						});
						UpdatePlayersPacket refreshPlayers = new UpdatePlayersPacket(pList, queueFFA.size(), 0, 0);
						clients.forEach((c, b) -> {
							try {
								server.sendPacket(refreshPlayers, c);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
						
						if (queueFFA.size() >= 2) {
							ffaCooldown = 10;
							clients.forEach((c, b) -> {
								try {
									server.sendPacket(new GameRunPacket(true, 10), c);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						} else {
							ffaCooldown = -1;
							clients.forEach((c, b) -> {
								try {
									server.sendPacket(new GameRunPacket(false, 10), c);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						}
					}
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
				if (queueFFA.contains(socket)) queueFFA.remove(socket);
				ArrayList<String> pList = new ArrayList<String>();
				clients.forEach((c, b) -> {
					pList.add(b.username + (b.isOnline ? "" : " [Offline]"));
				});
				UpdatePlayersPacket refreshPlayers = new UpdatePlayersPacket(pList, queueFFA.size(), 0, 0);
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

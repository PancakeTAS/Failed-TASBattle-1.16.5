package work.mgnet.packetlib.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import work.mgnet.packetlib.uses.IdPacket;
import work.mgnet.packetlib.uses.Packet;
import work.mgnet.packetlib.uses.PacketEvent;
import work.mgnet.packetlib.uses.ServerEvent;

public class ServerHandler {

	/**
	 * @see Packet, PacketEvent
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 * @param packet
	 */
	
	private ServerSocket server; /**Socket*/
	private ArrayList<Socket> unregisteredClients = new ArrayList<Socket>(); /**All Clients Unregistered*/
	public HashMap<Socket, Integer> registeredClients = new HashMap<Socket, Integer>(); /**All Clients */
	public HashMap<Socket, ObjectOutputStream> registeredStreams = new HashMap<Socket, ObjectOutputStream>(); /**All Clients */
	private volatile ArrayList<PacketEvent> events = new ArrayList<PacketEvent>(); /**List of all Events that'll be run by the eventDaemon*/
	private volatile ArrayList<ServerEvent> events2 = new ArrayList<ServerEvent>(); /**List of all Events that'll be run by the eventDaemon*/
	protected Thread acceptDaemon = new Thread(new Runnable() { /**This Thread waits for new Clients*/
		
		@Override
		public synchronized void run() {
			while(true) {
				try {
					Socket s = server.accept();
					if (s != null) {
						int size = registeredClients.size() + unregisteredClients.size();
						System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[SERVER] " + s.getInetAddress().getHostName() + " connected");
						unregisteredClients.add(s);
						registeredStreams.put(s, new ObjectOutputStream(s.getOutputStream()));
						for (ServerEvent serverEvent : events2) {
							serverEvent.onConnect(s);
						}
						ObjectInputStream input = new ObjectInputStream(s.getInputStream());
						registeredStreams.get(s).writeObject(new IdPacket(size));
						registeredStreams.get(s).flush();
						new Thread(new Runnable() { /**This Thread waits for inputs*/
							
							@Override
							public synchronized void run() {
								while (unregisteredClients.contains(s) || registeredClients.containsKey(s)) {
									try {
										Object r = input.readObject();
										if (r instanceof Packet) {
											if (r instanceof IdPacket) {
												if (size != registeredClients.size() + unregisteredClients.size() - 1) {
													s.close();
													System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[SERVER] " + s.getInetAddress().getHostName() + ": Invalid Id (Possibly Lag)");
													unregisteredClients.remove(s);
													for (ServerEvent serverEvent : events2) {
														serverEvent.onExit(s);
													}
													for (ServerEvent serverEvent : events2) {
														serverEvent.onError(s, "Invalid Id");
													}
													return;
												}
												unregisteredClients.remove(s);
												registeredClients.put(s, size);
												for (ServerEvent serverEvent : events2) {
													serverEvent.onRegister(s, size);
												}
												System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[SERVER] " + s.getInetAddress().getHostName() + ": registered");
											}
											System.out.println("Server recieves (" + events.size() + "): " + ((Packet) r).getName());
											for (PacketEvent packetEvent : events) {
												packetEvent.onPacket((Packet) r, s, size);
											}
										}
									} catch (IOException e) {
										System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[SERVER] " + s.getInetAddress().getHostName() + ": R/- Error (Disconnect)");
										try {
											s.close();
										} catch (Exception e3) {
											
										}
										/*synchronized (registeredClients) {
											for (Entry<Integer, Socket> s2 : registeredClients.entrySet()) {
												if (s2.getValue().equals(s)) registeredClients.remove(s2.getKey());
											}
										}*/
										if (registeredClients.containsKey(s)) registeredClients.remove(s);
										if (unregisteredClients.contains(s)) unregisteredClients.remove(s);
										if (registeredStreams.containsKey(s)) registeredStreams.remove(s);
										for (ServerEvent serverEvent : events2) {
											serverEvent.onDisconnect(s);
										}
									} catch (ClassNotFoundException e) {
										System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[SERVER] " + s.getInetAddress().getHostName() + ": Not an Object Error");
										for (ServerEvent serverEvent : events2) {
											serverEvent.onError(s, "RW");
										}
									}
									
								}
								
							}
						}).start();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				} catch (IOException e) {
					
				}
			}
		}
	});

	
	public ServerHandler(ServerSocket target) throws IOException { /**Constructor*/
		this.server = target;
		acceptDaemon.start();
	}
	
	public void sendPacket(Packet obj, Socket client) throws IOException { /**Send a Packet*/
		System.out.println("Outgoing: " + obj.getName());
		registeredStreams.get(client).writeObject(obj);
		registeredStreams.get(client).flush();
		registeredStreams.get(client).reset();
	}
	
	public void addEvent(PacketEvent event) { /**Register Event*/
		this.events.add(event);
	}

	public void addEvent(ServerEvent event) { /**Register Event*/
		this.events2.add(event);
	}
	
	public ServerSocket getServer() { /**Return Socket*/
		return server;
	}
	
	public ArrayList<ServerEvent> getEvents2() { /**Get all Events*/
		return events2;
	}
	
	public ArrayList<PacketEvent> getEvents() { /**Get all Events*/
		return events;
	}
	
}

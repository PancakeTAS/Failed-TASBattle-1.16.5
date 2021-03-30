package de.pfannekuchen.tasbattleserver.packets;

import work.mgnet.packetlib.uses.Packet;

public class LoginPacket extends Packet {

	private static final long serialVersionUID = 7559070651522429112L;
	public String username;
	public boolean isOnline;
	
	public LoginPacket(String name, boolean online) {
		super(1, "LoginPacket");
		username = name;
		isOnline = online;
	}

}

package de.pfannekuchen.tasbattleserver.packets;

import work.mgnet.packetlib.uses.Packet;

public class ConnectPacket extends Packet {

	private static final long serialVersionUID = 4670909662123959550L;
	public int port;
	
	public ConnectPacket(int port) {
		super(1, "ConnectPacket");
		this.port = port;
	}
	
}

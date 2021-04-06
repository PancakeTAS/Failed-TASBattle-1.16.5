package de.pfannekuchen.tasbattleserver.packets;

import work.mgnet.packetlib.uses.Packet;

public class ConnectPacket extends Packet {

	private static final long serialVersionUID = 4670909662123959550L;
	public int port;
	public boolean connect;
	
	public ConnectPacket(int port, boolean connect) {
		super(1, "ConnectPacket");
		this.port = port;
		this.connect = connect;
	}
	
}

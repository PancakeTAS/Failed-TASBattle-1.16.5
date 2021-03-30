package de.pfannekuchen.tasbattleserver.packets;

import de.pfannekuchen.tasbattleserver.settings.Movement;
import work.mgnet.packetlib.uses.Packet;

public class HandshakePacket extends Packet {

	public HandshakePacket() {
		super(1, "HandshakePacket");
	}
	private static final long serialVersionUID = 7833295038983161831L;
	public boolean optifineAllowed;
	public boolean sodiumAllowed;
	public Movement currentMovementOption;
	public String skywarsMap;
	public String bedwarsMap;
	public String ffaMap;
	public int tickrate;

}

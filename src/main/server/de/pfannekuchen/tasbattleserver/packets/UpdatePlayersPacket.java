package de.pfannekuchen.tasbattleserver.packets;

import java.util.ArrayList;

import work.mgnet.packetlib.uses.Packet;

public class UpdatePlayersPacket extends Packet {

	private static final long serialVersionUID = -6668316197149886559L;
	public ArrayList<String> players = new ArrayList<>();
	
	public UpdatePlayersPacket(ArrayList<String> players) {
		super(1, "UpdatePlayersPacket");
		this.players = players;
	}
	
}

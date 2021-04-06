package de.pfannekuchen.tasbattleserver.packets;

import java.util.ArrayList;

import work.mgnet.packetlib.uses.Packet;

public class UpdatePlayersPacket extends Packet {

	private static final long serialVersionUID = -6668316197149886559L;
	public ArrayList<String> players = new ArrayList<>();
	public int queueFFA, queueBedwars, queueSkywars;
	
	public UpdatePlayersPacket(ArrayList<String> players, int queueFFA, int queueBedwars, int queueSkywars) {
		super(1, "UpdatePlayersPacket");
		this.players = players;
		this.queueFFA = queueFFA;
		this.queueBedwars = queueBedwars;
		this.queueSkywars = queueSkywars;
	}
	
}

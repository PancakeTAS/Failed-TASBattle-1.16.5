package de.pfannekuchen.tasbattleserver.packets;

import work.mgnet.packetlib.uses.Packet;

public class GameRunPacket extends Packet {
	
	private static final long serialVersionUID = -3459233260542404044L;
	public boolean startTimer;
	public int time;
	
	public GameRunPacket(boolean start, int time) {
		super(1, "GameRunPacket");
		this.time = time;
		this.startTimer = start;
	}
	
}

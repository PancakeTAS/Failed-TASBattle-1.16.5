package de.pfannekuchen.tasbattleserver.packets;

import work.mgnet.packetlib.uses.Packet;

public class JoinQueuePacket extends Packet {

	private static final long serialVersionUID = -5409214567669806565L;

	public String queue;
	
	public JoinQueuePacket(String queue) {
		super(1, "JoinQueuePacket");
		this.queue = queue;
	}

}

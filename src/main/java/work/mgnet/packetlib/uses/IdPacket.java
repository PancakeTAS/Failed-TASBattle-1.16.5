package work.mgnet.packetlib.uses;

public class IdPacket extends Packet {

	/**
	 * @see Packet, PacketEvent
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 * @param packet
	 */
	private static final long serialVersionUID = -6291319497990310753L;
	
	public int entityId;
	
	public IdPacket(int id) { /**Constructor*/
		super(0, "IdPacket");
		this.entityId = id;
	}
	
}

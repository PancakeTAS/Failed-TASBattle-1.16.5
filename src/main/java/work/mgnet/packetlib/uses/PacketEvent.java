package work.mgnet.packetlib.uses;

import java.net.Socket;

public interface PacketEvent {
	
	/**
	 * @see Packet, ConnectionHandler
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 * @param packet
	 */
	
	public void onPacket(Packet packet, Socket socket, int id); /**Event Method, will be run on Packet*/
	
}

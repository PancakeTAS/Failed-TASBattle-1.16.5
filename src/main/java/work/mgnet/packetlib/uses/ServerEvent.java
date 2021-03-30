package work.mgnet.packetlib.uses;

import java.net.Socket;

public interface ServerEvent {
	
	/**
	 * @see Packet, ConnectionHandler
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 * @param packet
	 */
	
	public void onConnect(Socket socket); /**Event Method, will be run on Connect*/
	public void onRegister(Socket socket, int id); /**Event Method, will be run on Register*/
	public void onDisconnect(Socket socket); /**Event Method, will be run on Disconnect*/
	public void onExit(Socket socket); /**Event Method, will be run on User Disconnect*/
	public void onError(Socket socket, String error); /**Event Method, will be run on User Disconnect*/

}
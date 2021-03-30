package work.mgnet.packetlib.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class PacketLib {
	
	/**
	 * @param host
	 * @param port
	 * @return Socket
	 * @throws UnknownHostException
	 * @throws IOException
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 */
	
	public static Socket openSocket(String host, int port) throws UnknownHostException, IOException { /**Creates a new Client*/
		return new Socket(host, port);
	}
	
	public static ServerSocket openServer(int port) throws IOException { /**Creates a new Server*/
		return new ServerSocket(port);
	}
	
}

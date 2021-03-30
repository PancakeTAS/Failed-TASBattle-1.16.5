package work.mgnet.packetlib.uses;

import java.io.Serializable;

public class Packet implements Serializable {
	
	/**
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 * @see ConnectionHandler, ServerHandler
	 */
	
	private static final long serialVersionUID = 1191762645818878983L; /**Needs to be set for serialization*/
	private int versionId; /**Is used to verify if a client has the correct update*/
	private String name; /**Name of the Packet. Very much unused*/
	public int sender; /**Getting Socket Id*/
	
	public Packet(int versionId, String name) { /**Constructor*/
		this.versionId = versionId;
		this.name = name;
		this.sender = -2;
	}

	public int getVersionId() { /**Returns the Id to check the Version of the Stream*/
		return versionId;
	}

	public String getName() { /**Returns the Name*/
		return name;
	}
	
	public int getSenderId() { /**Returns the Id to check the Client*/
		return sender;
	}
	
	@Override
	public String toString() { /**Some Time save*/
		return name;
	}
	
}

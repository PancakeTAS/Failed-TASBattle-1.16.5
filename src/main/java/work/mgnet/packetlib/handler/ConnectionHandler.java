package work.mgnet.packetlib.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import work.mgnet.packetlib.uses.IdPacket;
import work.mgnet.packetlib.uses.Packet;
import work.mgnet.packetlib.uses.PacketEvent;

public class ConnectionHandler {

	/**
	 * @see Packet, PacketEvent
	 * @author MCPfannkuchenYT
	 * @category Library
	 * @since v1.0-b1
	 * @param packet
	 */
	
	private volatile Socket target; /**Socket*/
	public int id = -1;
	private ObjectOutputStream outputstream; /**OutputStream for sending Objects*/
	private ObjectInputStream inputstream; /**InputStream for Objects*/
	private volatile ArrayList<PacketEvent> events = new ArrayList<PacketEvent>(); /**List of all Events that'll be run by the eventDaemon*/
	protected Thread eventDaemon = new Thread(new Runnable() { /**This Thread waits for Events*/
		
		@Override
		public void run() {
			while(target.isConnected()) {
				try {
					Object read = inputstream.readObject();
					if (read instanceof Packet) {
						if ((read instanceof IdPacket) == false) System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[CLIENT] " + (Packet) read + " from Server");
						if (read instanceof IdPacket) {
							sendPacket(new IdPacket(((IdPacket) read).entityId));
							id = ((IdPacket) read).entityId;
							System.out.println(new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + "[CLIENT] " + "Successfully registered with id " + id);
						}
						for (PacketEvent packetEvent : events) {
							packetEvent.onPacket((Packet) read, null, 0);
						}
					}
				} catch (Exception e) {
					return;
				}
			}
		}
	});
	
	public ConnectionHandler(Socket target) throws IOException { /**Constructor*/
		this.target = target;
		this.outputstream = new ObjectOutputStream(target.getOutputStream());
		this.inputstream = new ObjectInputStream(target.getInputStream());
		eventDaemon.start();
	}
	
	public void close() throws IOException {
		eventDaemon.interrupt();
		target.close();
	}
	
	public void sendPacket(Packet obj) throws Exception { /**Send a Packet*/
		if (obj instanceof IdPacket ? true : id != -1) {
			Packet objId = obj;
			objId.sender = id;
			outputstream.writeObject(objId);
			outputstream.flush();
			outputstream.reset();
		} else {
			Thread.sleep(10);
			sendPacket(obj);
		}
	}
	
	public void addEvent(PacketEvent event) { /**Register Event*/
		this.events.add(event);
	}

	public Socket getTarget() { /**Return Socket*/
		return target;
	}

	public ObjectOutputStream getOutputstream() { /**Return Input Stream*/
		return outputstream;
	}

	public ObjectInputStream getInputstream() { /**Return Output Stream*/
		return inputstream;
	}

	public ArrayList<PacketEvent> getEvents() { /**Get all Events*/
		return events;
	}
	
}

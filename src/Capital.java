import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;

//client handler
public class Capital implements Runnable {
	private Socket socket;
	private int clientNumber;
	private String clientName;
	private server joinedServer;
	// constants to define message types
	private static final int JOIN = 1;
	private static final int SEND = 2;
	private static final int ACCEPTED = 3;
	private static final int REJECTED = 4;
	private static final int MEMBERS = 5;
	private static final int OTHERMEMBERS = 6;
	private static final int NOTIFICATION = 7;
	private static final int END = 8;

	public Capital(Socket socket, int clientNumber, server joinedServer) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.joinedServer = joinedServer;
		//System.out.println("New client #" + clientNumber + "connected at " + socket);
	}

	public String getName() {
		return this.clientName;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void run() {
		try {
			while (true) {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream os = new DataOutputStream(socket.getOutputStream());
				byte messageType = in.readByte();
				BufferedReader inContent = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String messageContent = inContent.readLine();
				// String messageContent2 = "";
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				// handle each message type accordingly
				switch (messageType) {
				case JOIN:
					if (joinedServer.addName(messageContent)) {
						os.writeByte(ACCEPTED);
						this.clientName = messageContent;
						joinedServer.addClient(this);
						joinedServer.notifyAllClients();
					} else {
						os.writeByte(REJECTED);
					}
					break;
				case SEND:
					String[] contentSplit = messageContent.split(" ");
					String sender = contentSplit[0];
					String Receiver = contentSplit[1];
					int TTL = Integer.parseInt(contentSplit[2]);
					TTL--;
					if (TTL < 0) {
						JFrame error = new JFrame();
						error.setSize(200,200);
						error.add(new JLabel("Increase TTL or user is logged out"));
						error.setVisible(true);
						break;
					}
					String message = messageContent.substring(messageContent.indexOf("#") + 1);
					joinedServer.directSend(sender, Receiver, message, TTL);
					out.println("Message Sent");
					break;
				case MEMBERS:
					out.println(joinedServer.getAllMembers());
					break;
				case OTHERMEMBERS:
					out.println(joinedServer.getMembers());
					break;
				case NOTIFICATION:
					joinedServer.notifyLocalClients(messageContent);
					break;
				case END:
					joinedServer.removeClient(clientName,this);
					joinedServer.notifyAllClients();
				}
				

			}
		} catch (Exception e) {

		}
	}
}

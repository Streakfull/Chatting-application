import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;

public class Client {
	private boolean acceptFlag = true;
	private String name;
	private Socket clientSocket;
	DataOutputStream outToServer;
	DataInputStream inputStream;
	private static ChatScreen chatScreen;
	// private boolean isGettingMembers = false;
	// constants to define message types
	private static final int JOIN = 1;
	private static final int SEND = 2;
	private static final int ACCEPTED = 3;
	private static final int MEMBERS = 5;


	public Client(String name, int port) throws UnknownHostException, IOException {
		this.name = name;
		this.clientSocket = new Socket("localhost", port);
		this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
		this.inputStream = new DataInputStream(clientSocket.getInputStream());
	}

	public void join(String name) throws IOException {
		this.sendMessage(name, JOIN, null, null, 0);

	}

	@SuppressWarnings("deprecation")
	public void receiveMessage() throws IOException {
		while (true) {
			String resp = this.inputStream.readLine();
			if (!resp.contains("essage Sent")) {
				ChatPanel chat = chatScreen.getchatPanel();
				if (!resp.contains("IRECTSENDFrom")) {
					chat.setMembersParent(resp);
					// isGettingMembers = false;
				} else {
					String receiver = resp.substring(15, resp.indexOf(":"));
					chat.setMessage(resp.substring(15), receiver);
				}

			}
		}
	}

	public void checkName() throws IOException {
		byte respType = inputStream.readByte();
		if (respType == ACCEPTED) {
			this.acceptFlag = false;
		}
	}

	public void sendMessage(String message, int type, String sender, String receiver, int TTL) throws IOException {
		outToServer.writeByte(type);
		if (sender != null) {
			outToServer.writeBytes(sender + " " + receiver + (" " + TTL) + " " + "#");
		}
		outToServer.writeBytes(message + '\n');
		outToServer.flush();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Thread messageLoop = new Thread() {
		public void run() {
			while (true) {
				System.out.print("Message:");
				try {
					BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					String inputMessage = inFromUser.readLine();
					switch (inputMessage) {
					case "Bye":
						System.exit(0);
						break;
					case "Members":
						sendMessage("", MEMBERS, null, null, 0);
						break;
					default:
						String receiver = inputMessage.split(">>")[0];
						String message = inputMessage.substring(inputMessage.indexOf('>') + 2);
						sendMessage(message, SEND, name, receiver, 2);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	};
	public Thread receiveLoop = new Thread() {
		public void run() {
			try {
				receiveMessage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// METHODs FOR GUI..
	public boolean checkUsedName() throws IOException {
		byte respType = inputStream.readByte();
		return respType == ACCEPTED;
	}

	public void getClientMembers() throws IOException {
		// this.isGettingMembers = true;
		sendMessage("", MEMBERS, null, null, 0);

	}

	public String getName() {
		return this.name;
	}
	
		

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Color background = new Color(255, 251, 242);
		ChatScreen chatscreen = new ChatScreen(400, 100, "Memo", background);
		Client.chatScreen = chatscreen;
		/*
		 * System.out.println("Please choose server A or B"); String chosenServer =
		 * reader.readLine(); int port = chosenServer.equals("A") ? 6789 : 6790;
		 * System.out.println(port); Client client = new Client("", port); String
		 * inputName = ""; while (client.acceptFlag) {
		 * System.out.print("Please Enter your name: "); inputName = reader.readLine();
		 * client.join(inputName); client.checkName();
		 * 
		 * } client.setName(inputName); client.messageLoop.start();
		 * client.receiveLoop.start();
		 */
	}
}

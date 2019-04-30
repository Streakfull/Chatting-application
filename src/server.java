import java.util.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class server {
	// List of client names and client handlers (capital)
	private List<String> clientNames = new ArrayList<String>();
	private List<Capital> clients = new ArrayList<Capital>();
	private int port;
	private DataOutputStream outToServer;
	private DataInputStream inputStream;
	private boolean notFound;
	private static final int SEND = 2;
	private static final int OTHERMEMBERS = 6;
	private static final int NOTIFICATION = 7;

	public server(int port) throws IOException {
		System.out.println("The server is running on port " + port + ".");
		ExecutorService pool = Executors.newFixedThreadPool(2000);
		this.port = port;
		this.notFound = true;
		int clientNumber = 0;
		try (ServerSocket listener = new ServerSocket(port)) {
			while (true) {
				pool.execute(new Capital(listener.accept(), clientNumber++, this));
			}
		}
	}

	public boolean getNotFound() {
		return this.notFound;
	}

	public void setNotFound(boolean notFound) {
		this.notFound = notFound;
	}

	public void sendOtherServer(String message, String sender, String receiver, int TTL)
			throws UnknownHostException, IOException {
		int serverPort = this.port == 6790 ? 6789 : 6790;
		Socket serverSocket = new Socket("localhost", serverPort);
		this.outToServer = new DataOutputStream(serverSocket.getOutputStream());
		// this.inputStream = new DataInputStream(serverSocket.getInputStream());
		outToServer.writeByte(SEND);
		outToServer.writeBytes(sender + " " + receiver + " " + TTL + " " + "#");
		outToServer.writeBytes(message + '\n');
		outToServer.flush();
		serverSocket.close();
	}

	public boolean addName(String name) throws UnknownHostException, IOException {
		if (this.clientNames.contains(name) || checkOthers(name))
			return false;
		this.clientNames.add(name);
		return true;

	}

	public boolean checkOthers(String name) throws UnknownHostException, IOException {
		String others = getOtherMembers();
		String sub = others.substring(1, others.length() - 1);
		List<String> names = Arrays.asList(sub.split(","));
		if (names.contains(name) || names.contains(" " + name))
			return true;
		return false;
	}

	public void addClient(Capital c) {
		clients.add(c);
	}

	public String getMembers() {
		return this.clientNames.toString().replace(" ", "");
	}

	public String getAllMembers() throws UnknownHostException, IOException {
		String localMembers = getMembers();
		String globalMembers = getOtherMembers();
		// System
		return localMembers.substring(1, localMembers.length() - 1) + ","
				+ globalMembers.substring(1, globalMembers.length() - 1);
	}

	public String getOtherMembers() throws UnknownHostException, IOException {
		int serverPort = this.port == 6790 ? 6789 : 6790;
		Socket serverSocket = new Socket("localhost", serverPort);
		DataOutputStream outToServerL = new DataOutputStream(serverSocket.getOutputStream());
		DataInputStream inputStreamL = new DataInputStream(serverSocket.getInputStream());
		outToServerL.writeByte(OTHERMEMBERS);
		outToServerL.writeBytes("" + '\n');
		@SuppressWarnings("deprecation")
		String otherMembers = inputStreamL.readLine();
		serverSocket.close();
		return otherMembers;
	}

	// Send a message to another client
	public void directSend(String sender, String Receiver, String Message, int TTL)
			throws UnknownHostException, IOException {
		if (!this.clientNames.contains(Receiver)) {
			this.sendOtherServer(Message, sender, Receiver, TTL);
			return;
		}
		Stream<Capital> clientStream = this.clients.stream();
		clientStream.forEach(client -> {
			if (client.getName().equals(Receiver)) {
				Socket socket = client.getSocket();
				OutputStream os;
				try {
					os = socket.getOutputStream();
					PrintWriter out = new PrintWriter(os, true);
					out.println("DIRECTSENDFrom " + sender + ":" + Message);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	public void sendMembers(String members) {
		Stream<Capital> clientStream = this.clients.stream();
		clientStream.forEach(client -> {
			Socket socket = client.getSocket();
			OutputStream os;
			try {
				os = socket.getOutputStream();
				PrintWriter out = new PrintWriter(os, true);
				out.println(members);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
	}

	public void notifyAllClients() throws UnknownHostException, IOException {
		String members = this.getAllMembers();
		this.sendMembers(members);
		this.notifyOtherClients(members);

	}

	public void notifyLocalClients(String members) {
		this.sendMembers(members);
	}

	public void notifyOtherClients(String members) throws UnknownHostException, IOException {
		int serverPort = this.port == 6790 ? 6789 : 6790;
		Socket serverSocket = new Socket("localhost", serverPort);
		DataOutputStream outToServerL = new DataOutputStream(serverSocket.getOutputStream());
		outToServerL.writeByte(NOTIFICATION);
		outToServerL.writeBytes(members + '\n');
		serverSocket.close();
	}

	public void removeClient(String name, Capital client) {
		this.clientNames.remove(name);
		this.clients.remove(client);
	}

}

import java.io.IOException;

public class serverNet {
	public static Thread serverA = new Thread() {
		public void run() {
			try {
				new server(6789);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	public static Thread serverB = new Thread() {
		public void run() {
			try {
				new server(6790);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public static void main(String[] args) throws IOException {
		serverA.start();
		serverB.start();

	}

}

package Übung3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.net.ServerSocket;

public class Server {
	public static void main(String[] args) throws IOException {

		Server server = new Server();
		try {
			server.test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void test() throws IOException {
		int port = 12345;
		@SuppressWarnings("resource")

		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server verfügbar...");

		while (true) {
			Socket client = serverSocket.accept();
			System.out.println("Client "+ client.getInetAddress() + " verbunden...");
			//Thread gibt die möglichkeit mehrere Files Parallel zu versenden.
			new Thread(new Runnable() {
				public void run() {
					try {
						sendFile(client);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	//Methode um die Datei richtig zu verschicken
	void sendFile(Socket client) throws IOException {
		String pfad = leseNachricht(client);
		File file = new File(pfad);

		if (file.exists()) {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			schreibeNachrichtInt(client, fileContent.length);
			schreibeNachricht(client, fileContent);
		}
	}

	
	String leseNachricht(Socket socket) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[200];
		/* int anzahlZeichen = */bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
		int pos = buffer.length - 1;
		while (pos != 0 && buffer[pos] == 0) {
			pos--;
		}
		String nachricht = new String(buffer, 0, pos);
		return nachricht;
	}

	//Sendet den Client die bytes des Files
	void schreibeNachricht(Socket socket, byte[] nachricht) throws IOException {
		OutputStream streamlol = socket.getOutputStream();
		streamlol.write(nachricht);
		streamlol.flush();
	}
	
	//sendet den Client die größe in bytes des Files
	void schreibeNachrichtInt(Socket socket, int nachricht) throws IOException {
		DataOutputStream out = null;
		try {
			// create write stream to send information
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}
		out.writeInt(nachricht);
	}
}

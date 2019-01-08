package Übung3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		Client client = new Client();
		try {
			client.test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void test() throws IOException {
		String pfad = null;
		Socket socket = clientVerbindung();
		System.out.println("Verbindung mit Server erfolgreich!");
		
		System.out.println("Gib den Server-Pfad an  (mit 2 \\ anstatt 1):");
		//z.B.: pfad = "C:\\Desktop\\Server\\Bild.jpg";
		pfad = input();
		//Teilt nach "\\" um den Name der Datei zu finden (dateiTyp[dateiTyp.length-1])
		String[] dateiTyp = pfad.split("\\\\");
		schreibeNachricht(socket, pfad);
		

		System.out.println("Gib den Pfad an wo du die Datei speichern willst (mit 2 \\ anstatt 1):");
		//z.B.: pfad = "C:\\Desktop\\Client";
		pfad = input();
		
		//Erstellt eine Datei mit den name der Originellen und stellt sie im angegebenen Pfad
		try (FileOutputStream fos = new FileOutputStream(pfad + "\\" + dateiTyp[dateiTyp.length-1])) {
				fos.write(leseData(socket));
		}
		
		System.out.println("Die Datei: " + dateiTyp[dateiTyp.length-1] + "\nbefindet sich im Pfad: " + pfad);
		//Öffnet den Pfad wo die Datei gespeichert wird und highlightet sie
		Runtime.getRuntime().exec("explorer.exe /select," + pfad + "\\" + dateiTyp[dateiTyp.length-1]);

	}
	
	//Eine Nachricht wird den Server gesendet
	void schreibeNachricht(Socket socket, String nachricht) throws IOException {
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		printWriter.print(nachricht + " ");
		printWriter.flush();
	}
	
	//Client verbindet sich mit den Server
	Socket clientVerbindung() throws UnknownHostException, IOException {
		String ip = "127.0.0.1"; //localhost
		int port = 12345;
		return new Socket(ip, port);
	}
	
	// Die Methode "leseNachricht" lest die Anzahl an benutzte bytes für den File, setzt sie in einen Byte array ein und lässt sich den File schiken
	byte[] leseData(Socket socket) throws IOException {
		
		DataInputStream out = null;
		try {
		    //create write stream to send information
		    out=new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			
		}		
		InputStream stream = socket.getInputStream();		
		byte[] data = new byte[out.readInt()];
		int count = stream.read(data);
		return data;
	}
	
		String input() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		return br.readLine();
	}
}
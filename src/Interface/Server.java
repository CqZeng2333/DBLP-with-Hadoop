/**
 * Server for accepting query
 */
package Interface;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

import utils.UrlUtil;
import utils.UrlUtil.UrlEntity;

public class Server {
	ServerSocket server;
	int portNum;
	
	Server() {
		initPara(5566);
	}
	
	Server(int portNum) {
		initPara(portNum);
	}
	
	/**
	 * Initiate server with given port number
	 * @param portNum
	 */
	private void initPara(int portNum) {
		try {
			this.portNum = portNum;
			server = new ServerSocket(portNum);
			System.out.println("Server created success.");
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Continuously waiting for client connection
	 */
	public void acceptClients() {
		while (true) {
			try {
				Socket socket = server.accept();
				new Thread(new ServerThread(socket)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Create the server
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.acceptClients();
	}
	
	/**
	 * The thread running on server to communicate with client
	 * @author CqZeng
	 */
	class ServerThread implements Runnable {
		Socket socketForClient;
		BufferedReader in;
		PrintWriter out;
		
		/**
		 * ServerThread constructor
		 * @param socket
		 */
		public ServerThread(Socket socket) {
			this.socketForClient = socket;
		}
		
		/**
		 * The function of thread running on server
		 * Handle the communication with client
		 */
		@Override
		public void run() {
			initConnection();
			response();
			closeSocket();
		}
		
		/**
		 * Initiate input stream and output stream
		 */
		private void initConnection() {
			try {
				if (socketForClient == null) {
					System.out.println("Server thread created in failure.");
					return;
				}
				System.out.println("200 OK: Connection created in success.");
				System.out.println("Server thread created in success.");
				out = new PrintWriter(socketForClient.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socketForClient.getInputStream()));
			} catch(IOException e) {
				System.out.println(e);
			}
		}
		
		/**
		 * Receive query from client and send back the query result
		 */
		private void response() {
			String inputLine;
			try {
				while ((inputLine = in.readLine()) != null) {
					if (inputLine.startsWith("GET")) {
						System.out.println("Server received: " + inputLine);
						String[] strs = inputLine.split(" ");
						String result = this.generateResponseText(strs[1].substring(1));
						String responseText = "HTTP/1.1 200 OK\r\n"
								+ "Access-Control-Allow-Origin:*\r\n"
								+ "Access-Control-Allow-Headers:*\r\n"
								+ "Content-Type: text/html; charset=utf-8\r\n"
								+ "Content-Length:" + result.getBytes().length + "\r\n\r\n"
								+ result;
						out.println(responseText);
						System.out.println("Send back successfully. ");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				closeSocket();
				System.out.println("Server thread closed. ");
			}
		}
		
		private String generateResponseText(String input) {
			UrlEntity url = UrlUtil.parse(input);
			Map<String, String> params = url.params;
			
			ArrayList<String> queryResult = UserInterface.query(params.get("query"), params.get("searchType"), params.get("journal"));
			String result = "";
			for (String s : queryResult) {
				
				result += s;
			}
			return result;
		}
		
		/**
		 * Release all of the resource of the server thread
		 */
		private void closeSocket() {
			if (socketForClient == null) {
				return;
			} 
			try {
				socketForClient.shutdownInput();
				socketForClient.shutdownOutput();
				in.close();
				out.close();
				socketForClient.close();
				System.out.println("Server thread closed. ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

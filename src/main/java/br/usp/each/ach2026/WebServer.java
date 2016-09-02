package br.usp.each.ach2026;

import java.net.ServerSocket;
import java.net.Socket;

public final class WebServer {
	
	public static void main(String argv[]) throws Exception {
		// ajustar numero da porta
		int port = 6789;
		
		// estabelecer socket de escuta
		ServerSocket welcomeSocket = new ServerSocket(port);
		System.out.println("Server up and running...");
		
		// processar a requisicao de servico http em um laco infinito
		while (true) {
			// escutar requisicao de conexao tcp
			Socket connectionSocket = welcomeSocket.accept();
			
			// construir um objeto para processar a mensagem de requisicao http
			HttpRequest request = new HttpRequest(connectionSocket);
			
			// criar um novo thread para processar a requisicao
			Thread thread = new Thread(request);
			thread.start();
		}
		
	}
}

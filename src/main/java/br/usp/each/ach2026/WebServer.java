package br.usp.each.ach2026;

import java.net.ServerSocket;
import java.net.Socket;

import br.usp.each.ach2026.PropertiesManager.ListingDirectories;

public final class WebServer {
	
	public static void main(String argv[]) throws Exception {
		// ajustar numero da porta
		int port = 6789;
		
		// processar arquivo de configuracoes
		PropertiesManager manager = new PropertiesManager();
		ListingDirectories listing = manager.getListingType();
		
		// estabelecer socket de escuta
		ServerSocket welcomeSocket = new ServerSocket(port);
		System.out.println("Server up and running...");
		
		// processar a requisicao de servico http em um laco infinito
		while (true) {
			// escutar requisicao de conexao tcp
			Socket connectionSocket = welcomeSocket.accept();
			
			// construir um objeto para processar a mensagem de requisicao http
			HttpRequest request = new HttpRequest(connectionSocket);
			request.setListingDirectories(listing);
			
			// criar um novo thread para processar a requisicao
			Thread thread = new Thread(request);
			thread.start();
		}
		
	}
}

package br.usp.each.ach2026;

import org.apache.log4j.Logger;

import br.usp.each.ach2026.PropertiesManager.ListingDirectories;
import br.usp.each.ach2026.response.HttpResponse;
import br.usp.each.ach2026.response.HttpResponseFactory;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequest implements Runnable {

    private final static Logger logger = Logger.getLogger(HttpRequest.class);
    private final Socket socket;
    private ListingDirectories listing;

    public HttpRequest(final Socket socket) throws Exception {
        this.socket = socket;
    }

    public void run() {
        try {
            processRequest();
        } catch (final Exception ex) {
//			ex.printStackTrace();
        }
    }

    private void processRequest() throws Exception {
        // obter uma referencia para os trechos de entrada e saida do socket
        final InputStream is = this.socket.getInputStream();
        final DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());
        
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);
        
        // obter a linha de requisicao da mensagem de requisicao http
        final String requestLine = br.readLine();
        
        // gerenciar autorizacao
        AuthManager auth = new AuthManager(br);
        final boolean logged = auth.isLogged();

        // extrair o nome do arquivo a linha de requisicao
        final StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); // pular o metodo, que deve ser GET
        String fileName = tokens.nextToken();

        // acrescente um '.' de modo que a requisicao do arquivo
        // esteja dentro do diretorio atual
        fileName = "." + fileName;
        
        HttpResponse response = new HttpResponseFactory().getResponse(fileName, logged, listing);
        
        final long bytes = response.getBytes();
        final int statusCode = response.getStatusCode();
        response.writeHeader(os);
        response.writeBody(os);

        // fechando cadeias e socket
        os.close();
        br.close();
        this.socket.close();
        
        // escrevendo log
        final int port = this.socket.getLocalPort();
        final String address = this.socket.getInetAddress().getHostAddress();
        logger.info(String.format("%s:%d \"%s\" %d %d\n", address, port, requestLine, statusCode, bytes));
    }
    
    public void setListingDirectories(ListingDirectories listing) {
    	this.listing = listing;
    }
}

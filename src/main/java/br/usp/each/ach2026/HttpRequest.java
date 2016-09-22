package br.usp.each.ach2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class HttpRequest implements Runnable {

	private final static Logger logger = Logger.getLogger(HttpRequest.class);
	private final static String CRLF = "\r\n";
	private Socket socket;

	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	public void run() {
		try {
			processRequest();
		}
		catch (Exception ex) {
//			ex.printStackTrace();
		}
	}

	private void processRequest() throws Exception {
		// obter uma referencia para os trechos de entrada e saida do socket
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());

		// ajustar filtros de entrada
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		// obter a linha de requisicao da mensagem de requisicao http
		String requestLine = br.readLine();

		// exibir a linha de requisicao
//		System.out.println();
//		System.out.println(requestLine);

		// obter e exibir as linhas de cabecalho
		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
//			System.out.println(headerLine);
		}

		// extrair o nome do arquivo a linha de requisicao
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken(); // pular o metodo, que deve ser GET
		String fileName = tokens.nextToken();

		// acrescente um '.' de modo que a requisicao do arquivo
		// esteja dentro do diretorio atual
		fileName = "." + fileName;

		// abrir o arquivo requisitado
		FileInputStream fis = null;
		boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);
		}
		catch (FileNotFoundException ex) {
			fileExists = false;
		}

		// construir a mensagem de resposta
		int statusCode;
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		
		if (fileExists) {
			statusCode = 200;
			statusLine = "HTTP/1.0 200 OK" + CRLF;
			contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
		}
		else {
			if (new File(fileName).isDirectory()) {
				statusCode = 200;
				statusLine = "HTTP/1.0 200 OK" + CRLF;
				contentTypeLine = "Content-type: text/html" + CRLF;
				entityBody = HtmlGenerator.listDirectoryContent(fileName);
			}
			else {
				statusCode = 404;
				statusLine = "HTTP/1.0 404 Not Found" + CRLF;
				contentTypeLine = "Content-type: text/html" + CRLF;
				entityBody = HtmlGenerator.fileNotFound(fileName);
			}
		}

		// enviar a linha de status
		os.writeBytes(statusLine);

		// enviar a linha de conteudo
		os.writeBytes(contentTypeLine);

		// fim das linhas de cabecalho
		os.writeBytes(CRLF);

		// enviar o corpo da entidade
		int bytes;
		if (fileExists) {
			bytes = IOUtils.toByteArray(fis).length;
			sendBytes(fis, os);
			fis.close();
		}
		else {
			bytes = entityBody.getBytes("UTF-8").length;
			os.writeBytes(entityBody);
		}

		// fechando cadeias e socket
		os.close();
		br.close();
		socket.close();
		
		int port = socket.getLocalPort();
		String address = socket.getInetAddress().getHostAddress();
		ZonedDateTime timestamp = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
//		System.out.printf("%s:%d - [%s] \"%s\" %d %d\n", address, port, timestamp.format(formatter), requestLine, statusCode, bytes);
		
//		logger.info("{}:{} \"{}\" {} {}", address, port, requestLine, statusCode, bytes);
		logger.info(String.format("%s:%d \"%s\" %d %d\n", address, port, requestLine, statusCode, bytes));
	}

	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
		// construir um buffer de 1k para comportar os bytes no caminho para o socket
		byte[] buffer = new byte[1024];
		int bytes = 0;

		// copiar o arquivo requisitado dentro da cadeia de saida do socket
		while ((bytes = fis.read(buffer)) != -1) {
			os.write(buffer, 0, bytes);
		}
	}

	private static String contentType(String fileName) {
		String contentType = "application/octet-stream";
		if (fileName.endsWith(".html") || fileName.endsWith(".htm"))
			contentType = "text/html";

		if (fileName.endsWith("gif"))
			contentType = "image/gif";

		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
			contentType = "image/jpeg";

		return contentType;
	}
}

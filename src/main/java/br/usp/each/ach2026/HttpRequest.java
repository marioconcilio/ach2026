package br.usp.each.ach2026;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class HttpRequest implements Runnable {

    private final static Logger logger = Logger.getLogger(HttpRequest.class);
    private final static String CRLF = "\r\n";
    private final Socket socket;

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

        // ajustar filtros de entrada
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);

        // obter a linha de requisicao da mensagem de requisicao http
        final String requestLine = br.readLine();

        // exibir a linha de requisicao
//		System.out.println();
//		System.out.println(requestLine);
        boolean logged = false;
        // obter e exibir as linhas de cabecalho
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            if (headerLine.contains("Authorization:")) {
                final String[] authorizationHeader = headerLine.split(":");
                final String[] value = authorizationHeader[1].split(" ");
                if (value[0].equalsIgnoreCase("Basic") && value[1].equals(encoded("user:password"))) {
                    logged = true;
                }
            }
//			System.out.println(headerLine);
        }

        // extrair o nome do arquivo a linha de requisicao
        final StringTokenizer tokens = new StringTokenizer(requestLine);
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
        } catch (final FileNotFoundException ex) {
            fileExists = false;
        }

        // construir a mensagem de resposta
        final int statusCode;
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;

        if (requestLine.startsWith("/restrito") && !logged) {
            statusCode = 401;
            statusLine = "HTTP/1.0 401 Unauthorized" + CRLF;
            contentTypeLine = "Content-type: text/html" + CRLF;
            entityBody = HtmlGenerator.unauthorized(fileName);
        } else if (fileExists) {
            statusCode = 200;
            statusLine = "HTTP/1.0 200 OK" + CRLF;
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        } else {
            if (new File(fileName).isDirectory()) {
                statusCode = 200;
                statusLine = "HTTP/1.0 200 OK" + CRLF;
                contentTypeLine = "Content-type: text/html" + CRLF;
                entityBody = HtmlGenerator.listDirectoryContent(fileName);
            } else {
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
        final int bytes;
        if (fileExists) {
            bytes = IOUtils.toByteArray(fis).length;
            sendBytes(fis, os);
            fis.close();
        } else {
            bytes = entityBody.getBytes("UTF-8").length;
            os.writeBytes(entityBody);
        }

        // fechando cadeias e socket
        os.close();
        br.close();
        this.socket.close();

        final int port = this.socket.getLocalPort();
        final String address = this.socket.getInetAddress().getHostAddress();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
//		System.out.printf("%s:%d - [%s] \"%s\" %d %d\n", address, port, timestamp.format(formatter), requestLine, statusCode, bytes);

//		logger.info("{}:{} \"{}\" {} {}", address, port, requestLine, statusCode, bytes);
        logger.info(String.format("%s:%d \"%s\" %d %d\n", address, port, requestLine, statusCode, bytes));
    }

    private String encoded(final String value) {
        return new BASE64Encoder().encode(value.getBytes());
    }

    private static void sendBytes(final FileInputStream fis, final OutputStream os) throws Exception {
        // construir um buffer de 1k para comportar os bytes no caminho para o socket
        final byte[] buffer = new byte[1024];
        int bytes = 0;

        // copiar o arquivo requisitado dentro da cadeia de saida do socket
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    private static String contentType(final String fileName) {
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

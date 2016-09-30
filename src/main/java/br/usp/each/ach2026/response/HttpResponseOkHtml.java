package br.usp.each.ach2026.response;

import java.io.DataOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponseOkHtml extends HttpResponse {
	
	private String body;

	public HttpResponseOkHtml(String filename) throws Exception {
		super(filename);
		body = new String(Files.readAllBytes(Paths.get(filename)));
	}

	@Override
	public int getStatusCode() {
		return 200;
	}

	@Override
	public long getBytes() throws Exception {
		return body.getBytes("UTF-8").length;
	}

	@Override
	public void writeHeader(DataOutputStream os) throws Exception {
		os.writeBytes("HTTP/1.0 200 OK" + CRLF);
		os.writeBytes("Content-type: text/html" + CRLF);
		os.writeBytes(CRLF);
	}

	@Override
	public void writeBody(DataOutputStream os) throws Exception {
		os.writeBytes(body);
	}

}

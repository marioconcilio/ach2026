package br.usp.each.ach2026.response;

import java.io.DataOutputStream;

import br.usp.each.ach2026.HtmlGenerator;

public class HttpResponseNotFound extends HttpResponse {
	
	private String body;

	public HttpResponseNotFound(String filename) {
		super(filename);
		body = HtmlGenerator.fileNotFound(filename);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}

	@Override
	public int getBytes() throws Exception {
		return body.getBytes("UTF-8").length;
	}

	@Override
	public void writeHeader(DataOutputStream os) throws Exception {
		os.writeBytes("HTTP/1.0 404 Not Found" + CRLF);
		os.writeBytes("Content-type: text/html" + CRLF);
		os.writeBytes(CRLF);
	}

	@Override
	public void writeBody(DataOutputStream os) throws Exception {
		os.writeBytes(body);
	}

}

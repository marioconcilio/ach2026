package br.usp.each.ach2026.response;

import java.io.DataOutputStream;

import br.usp.each.ach2026.HtmlGenerator;

public class HttpResponseOkDirectory extends HttpResponse {
	
	private String body;

	public HttpResponseOkDirectory(String filename) throws Exception {
		super(filename);
		body = HtmlGenerator.listDirectoryContent(filename);
	}

	@Override
	public int getStatusCode() {
		return 200;
	}

	@Override
	public int getBytes() throws Exception {
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

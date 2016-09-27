package br.usp.each.ach2026.response;

import java.io.DataOutputStream;

import br.usp.each.ach2026.HtmlGenerator;

public class HttpResponseUnauthorized extends HttpResponse {
	
	private String body;

	public HttpResponseUnauthorized(String filename) {
		super(filename);
		body = HtmlGenerator.unauthorized(filename);
	}

	@Override
	public int getStatusCode() {
		return 401;
	}

	@Override
	public int getBytes() throws Exception {
		return body.getBytes("UTF-8").length;
	}

	@Override
	public void writeHeader(DataOutputStream os) throws Exception {
		os.writeBytes("HTTP/1.0 401 Unauthorized" + CRLF);
		os.writeBytes("Content-type: text/html" + CRLF);
		os.writeBytes(CRLF);
	}

	@Override
	public void writeBody(DataOutputStream os) throws Exception {
		os.writeBytes(body);
	}

}

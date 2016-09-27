package br.usp.each.ach2026.response;

import java.io.DataOutputStream;
import java.io.FileInputStream;

public abstract class HttpResponse {
	
	protected final static String CRLF = "\r\n";
	protected String filename;
	protected boolean fileExists;
	protected FileInputStream fis = null;
	
	public abstract int getStatusCode();
	public abstract int getBytes() throws Exception;
	public abstract void writeHeader(final DataOutputStream os) throws Exception;
	public abstract void writeBody(final DataOutputStream os) throws Exception;
	
	public HttpResponse(String filename) {
		this.filename = filename;
	}
	
	public String contentType() {
        String contentType = "application/octet-stream";
        if (filename.endsWith(".html") || filename.endsWith(".htm"))
            contentType = "text/html";

        if (filename.endsWith("gif"))
            contentType = "image/gif";

        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
            contentType = "image/jpeg";

        return contentType;
    }
	
	public void setFileInputStream(FileInputStream fis) {
		this.fis = fis;
	}

}

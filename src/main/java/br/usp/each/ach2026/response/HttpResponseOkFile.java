package br.usp.each.ach2026.response;

import java.io.DataOutputStream;

import org.apache.commons.io.IOUtils;

public class HttpResponseOkFile extends HttpResponse {

	public HttpResponseOkFile(String filename) {
		super(filename);
	}

	@Override
	public int getStatusCode() {
		return 200;
	}

	@Override
	public long getBytes() throws Exception {
		return IOUtils.toByteArray(fis).length;
	}

	@Override
	public void writeHeader(final DataOutputStream os) throws Exception {
		os.writeBytes("HTTP/1.0 200 OK" + CRLF);
		os.writeBytes("Content-type: " + contentType() + CRLF);
		os.writeBytes(CRLF);
	}

	@Override
	public void writeBody(DataOutputStream os) throws Exception {
		// construir um buffer de 1k para comportar os bytes no caminho para o socket
        final byte[] buffer = new byte[1024];
        int bytes = 0;
        
        // copiar o arquivo requisitado dentro da cadeia de saida do socket
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
        
        fis.close();
	}

}

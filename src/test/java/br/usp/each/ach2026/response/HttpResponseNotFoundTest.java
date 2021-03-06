package br.usp.each.ach2026.response;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

import br.usp.each.ach2026.HtmlGenerator;

public class HttpResponseNotFoundTest {
	
	private static HttpResponseNotFound response;
	private static String body;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final String filename = "filename.test";
		response = new HttpResponseNotFound(filename);
		body = HtmlGenerator.fileNotFound(filename);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		response = null;
		body = null;
	}

	@Test
	public final void testGetStatusCode() {
		assertEquals(404, response.getStatusCode());
	}

	@Test
	public final void testGetBytes() throws Exception {
		final long bytes = body.getBytes("UTF-8").length;
		assertEquals(bytes, response.getBytes());
	}

	@Test
	public final void testWriteHeader() throws Exception {
		// given
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream os = new DataOutputStream(baos);
		
		// when
		response.writeHeader(os);
		final String header = new String(baos.toByteArray(), "UTF-8");
		
		// then
		final StringBuilder expected = new StringBuilder();
		expected.append("HTTP/1.0 404 Not Found")
		.append(HttpResponse.CRLF)
		.append("Content-type: text/html")
		.append(HttpResponse.CRLF)
		.append(HttpResponse.CRLF);

		assertEquals(expected.toString(), header);
	}

	@Test
	public final void testWriteBody() throws Exception {
		// given
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream os = new DataOutputStream(baos);
		
		//when
		response.writeBody(os);
		final String writtenBody = new String(baos.toByteArray(), "UTF-8");
		
		// then
		assertEquals(body, writtenBody);
	}

}

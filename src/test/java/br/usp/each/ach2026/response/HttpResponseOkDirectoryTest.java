package br.usp.each.ach2026.response;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import br.usp.each.ach2026.HtmlGenerator;

public class HttpResponseOkDirectoryTest {
	
	@ClassRule
	public static final TemporaryFolder folder = new TemporaryFolder();
	
	private static HttpResponseOkDirectory response;
	private static String body;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final File dir = folder.newFolder();
		response = new HttpResponseOkDirectory(dir.getAbsolutePath());
		body = HtmlGenerator.listDirectoryContent(dir.getAbsolutePath());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		response = null;
		body = null;
	}

	@Test
	public final void testGetStatusCode() {
		assertEquals(200, response.getStatusCode());
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
		expected.append("HTTP/1.0 200 OK")
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
		
		// when
		response.writeBody(os);
		final String writtenBody = new String(baos.toByteArray(), "UTF-8");
		
		// then
		assertEquals(body, writtenBody);
	}

}

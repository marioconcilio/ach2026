package br.usp.each.ach2026.response;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class HttpResponseOkFileTest {
	
	@ClassRule
	public static final TemporaryFolder folder = new TemporaryFolder();
	
	private static HttpResponseOkFile response;
	private static File file;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		file = folder.newFile();
		// write test file
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()))) {
			writer.write("Testing file Testing file Testing file Testing file");
		}
		
		response = new HttpResponseOkFile(file.getAbsolutePath());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		response = null;
	}
	
	@Before
	public final void setUp() throws Exception {
		final FileInputStream fis = new FileInputStream(file);
		response.setFileInputStream(fis);
	}

	@Test
	public final void testGetStatusCode() {
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public final void testGetBytes() throws Exception {
		final FileInputStream fis = new FileInputStream(file);
		response.setFileInputStream(fis);
		final long bytes = file.length();
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
		.append("Content-type: application/octet-stream")
		.append(HttpResponse.CRLF)
		.append(HttpResponse.CRLF);

		assertEquals(expected.toString(), header);
	}

	@Test
	public final void testWriteBody() throws Exception {
		// given
		final File expected = folder.newFile();
		final FileOutputStream fos = new FileOutputStream(expected);
		final DataOutputStream os = new DataOutputStream(fos);
		
		// when
		response.writeBody(os);
		
		// then
		boolean equals = FileUtils.contentEquals(expected, file);
		assertTrue(equals);
	}
	
	@Test
	public final void testContentType() {
		// given
		HttpResponse htm = new HttpResponseOkFile("test.htm");
		HttpResponse html = new HttpResponseOkFile("test.html");
		HttpResponse gif = new HttpResponseOkFile("test.gif");
		HttpResponse jpg = new HttpResponseOkFile("test.jpg");
		HttpResponse jpeg = new HttpResponseOkFile("test.jpeg");
		
		// when
		String contentHtm = htm.contentType();
		String contentHtml = html.contentType();
		String contentGif = gif.contentType();
		String contentJpg = jpg.contentType();
		String contentJpeg = jpeg.contentType();
		
		// then
		assertEquals("text/html", contentHtm);
		assertEquals("text/html", contentHtml);
		assertEquals("image/gif", contentGif);
		assertEquals("image/jpeg", contentJpg);
		assertEquals("image/jpeg", contentJpeg);
	}

}

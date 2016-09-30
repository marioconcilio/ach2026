package br.usp.each.ach2026;

import static org.junit.Assert.*;

import org.junit.Test;

public class HtmlGeneratorTest {

	@Test
	public void testUnauthorized() {
		String filename = "./TestFile.test";
		String html = "<html>"
				+ "<head><title>Unauthorized</title></head>"
				+ "<body><h1>Unauthorized</h1><p>Access to the requested URL "+ filename.substring(1) +" is Restrict.</p></body>"
				+ "</html>";
		
		String htmlGenerated = HtmlGenerator.unauthorized(filename);
		assertEquals(html, htmlGenerated);
	}

	@Test
	public void testFileNotFound() {
		String filename = "./TestFile.test";
		String html = "<html>"
				+ "<head><title>Not Found</title></head>"
				+ "<body><h1>Not Found</h1><p>The requested URL "+ filename.substring(1) +" was not found on this server.</p></body>"
				+ "</html>";
		
		String htmlGenerated = HtmlGenerator.fileNotFound(filename);
		assertEquals(html, htmlGenerated);
	}

	@Test
	public void testReadableFileSize() {
		// given
		final long size1 = 900L;
		final long size2 = 1024L;
		final long size3 = 2048L;
		final long size4 = 2220L;
		final long size5 = 1048576L;
		
		// when
		String bytes = HtmlGenerator.readableFileSize(size1);
		String kilo = HtmlGenerator.readableFileSize(size2);
		String kilo2 = HtmlGenerator.readableFileSize(size3);
		String kilo2_2 = HtmlGenerator.readableFileSize(size4);
		String mega = HtmlGenerator.readableFileSize(size5);
		
		// then
		assertEquals("900 B", bytes);
		assertEquals("1 kB", kilo);
		assertEquals("2 kB", kilo2);
		assertEquals("2.2 kB", kilo2_2);
		assertEquals("1 MB", mega);
	}

}

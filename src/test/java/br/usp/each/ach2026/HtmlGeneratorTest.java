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
	public void testListDirectoryContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadableFileSize() {
		fail("Not yet implemented");
	}

}

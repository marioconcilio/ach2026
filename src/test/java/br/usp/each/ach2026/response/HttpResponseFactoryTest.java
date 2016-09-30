package br.usp.each.ach2026.response;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import br.usp.each.ach2026.PropertiesManager.ListingDirectories;

public class HttpResponseFactoryTest {
	
	@ClassRule
	public static final TemporaryFolder folder = new TemporaryFolder();
	
	static File testFolder;
	static File testFolder2;
	static File restritoFolder;
	static File htmlFile;
	static File testFile;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		/*
		 * |--- temp/
		 * 		|--- test_folder/
		 * 			 |--- index.html
		 * 		|--- test_folder2/
		 * 		|--- restrito/
		 * 		|--- test_file
		 */
		testFolder = folder.newFolder();
		testFolder2 = folder.newFolder();
		restritoFolder = folder.newFolder("restrito");
		htmlFile = new File(testFolder, "index.html");
		testFile = folder.newFile();
		
		// write html file
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(htmlFile.getAbsolutePath()))) {
			writer.write("<html>");
			writer.write("<head><title>Test</title></head>");
			writer.write("<body><h1>Test Page</h1><p>Testing page.</p></body>");
			writer.write("</html>");
		}
		
		// write test file
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(testFile.getAbsolutePath()))) {
			writer.write("Testing file");
		}
	}

	@Test
	public void testListingDirectoriesAllowed() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(testFolder.getAbsolutePath(), false, ListingDirectories.ALLOWED);
		assertEquals(HttpResponseOkDirectory.class, response.getClass());
	}
	
	@Test
	public void testListingDirectoriesDenied() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(testFolder.getAbsolutePath(), false, ListingDirectories.DENIED);
		assertEquals(HttpResponseUnauthorized.class, response.getClass());
	}
	
	@Test
	public void testListingDirectoriesDefaultWithIndex() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(testFolder.getAbsolutePath(), true, ListingDirectories.DEFAULT);
		assertEquals(HttpResponseOkHtml.class, response.getClass());
	}
	
	@Test
	public void testListingDirectoriesDefaultWithoutIndex() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(testFolder2.getAbsolutePath(), true, ListingDirectories.DEFAULT);
		assertEquals(HttpResponseNotFound.class, response.getClass());
	}
	
	@Test
	public void testNotFound() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse("testfilename", false, ListingDirectories.ALLOWED);
		assertEquals(HttpResponseNotFound.class, response.getClass());
	}

	@Test
	public void testRestritoWhenLogged() throws Exception {
		final boolean logged = true;
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(restritoFolder.getAbsolutePath(), logged, ListingDirectories.ALLOWED);
		assertEquals(HttpResponseOkDirectory.class, response.getClass());
	}
	
	@Test
	public void testRestritoWhenNotLogged() throws Exception {
		final boolean logged = false;
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(restritoFolder.getAbsolutePath(), logged, ListingDirectories.ALLOWED);
		assertEquals(HttpResponseUnauthorized.class, response.getClass());
	}
	
	@Test
	public void testResponseOkHtml() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(htmlFile.getAbsolutePath(), false, ListingDirectories.ALLOWED);
		assertEquals(HttpResponseOkHtml.class, response.getClass());
	}
	
	@Test
	public void testResponseOkFile() throws Exception {
		final HttpResponse response = new HttpResponseFactory()
				.getResponse(testFile.getAbsolutePath(), false, ListingDirectories.ALLOWED);
		assertEquals(HttpResponseOkFile.class, response.getClass());
	}
	
}

package br.usp.each.ach2026.response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import br.usp.each.ach2026.PropertiesManager.ListingDirectories;

@RunWith(MockitoJUnitRunner.class)
public class HttpResponseFactoryTest {

	@Test
	public void testListingDirectoriesAllowed() throws Exception {
		File file = new File("/");
		File fileSpy = spy(file);
		doReturn(false).when(fileSpy).isDirectory();

		HttpResponse response = new HttpResponseFactory().getResponse("/", false, ListingDirectories.ALLOWED);
		assertEquals(response.getClass(), HttpResponseOkDirectory.class);
	}
	
	@Test
	public void testListingDirectoriesDenied() throws Exception {
		File file = mock(File.class);
		when(file.isDirectory()).thenReturn(true);
		
		HttpResponse response = new HttpResponseFactory().getResponse("/", false, ListingDirectories.DENIED);
		assertEquals(response.getClass(), HttpResponseUnauthorized.class);
	}
	
	@Test
	public void testNotFound() throws Exception {
		File file = new File("/");
		File fileSpy = spy(file);
		doReturn(false).when(fileSpy).isDirectory();
		
//		PowerMockito.whenNew(FileInputStream.class).withArguments(Matchers.anyString()).thenThrow(new FileNotFoundException());
		
		HttpResponse response = new HttpResponseFactory().getResponse("/", false, ListingDirectories.ALLOWED);
		assertEquals(response.getClass(), HttpResponseNotFound.class);
	}

}

package br.usp.each.ach2026;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuthManagerTest {
	
	private static AuthManager auth;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final String str = "Test";
		final InputStream is = new ByteArrayInputStream(str.getBytes());
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		auth = null;
	}

	@Test
	public final void testGetRequestLine() {
		
	}

	@Test
	public final void testIsLogged() {
		
	}

	@Test
	public final void testEncode() {
	}

}

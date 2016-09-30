package br.usp.each.ach2026;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import br.usp.each.ach2026.PropertiesManager.ListingDirectories;

public class PropertiesManagerTest {
	
	@ClassRule
	public static final TemporaryFolder folder = new TemporaryFolder();
	private File config;

	@Before
	public final void setUp() throws Exception {
		config = folder.newFile();
	}

	@Test
	public final void testGetListingTypeAllowed() throws Exception {
		/*
		 * lower case
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=allowed");
		}
		
		PropertiesManager manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.ALLOWED, manager.getListingType());
		
		/*
		 * upper case
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=ALLOWED");
		}
		
		manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.ALLOWED, manager.getListingType());
	}
	
	@Test
	public final void testGetListingTypeDenied() throws Exception {
		/*
		 * lower case
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=denied");
		}
		
		PropertiesManager manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.DENIED, manager.getListingType());
		
		/*
		 * upper case
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=DENIED");
		}
		
		manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.DENIED, manager.getListingType());
	}
	
	@Test
	public final void testGetListingTypeDefault() throws Exception {
		/*
		 * lower case
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=default");
		}
		
		PropertiesManager manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.DEFAULT, manager.getListingType());
		
		/*
		 * upper case
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=DEFAULT");
		}
		
		manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.DEFAULT, manager.getListingType());
		
		/*
		 * any word
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getAbsolutePath()))) {
			writer.write("listing.directories=asd");
		}
		
		manager = new PropertiesManager(config.getAbsolutePath());
		assertEquals(ListingDirectories.DEFAULT, manager.getListingType());
	}

}

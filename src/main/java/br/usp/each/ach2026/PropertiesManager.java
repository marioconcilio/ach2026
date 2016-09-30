package br.usp.each.ach2026;

import java.io.*;
import java.util.Properties;

public class PropertiesManager {
	
	public enum ListingDirectories {
		ALLOWED, DENIED, DEFAULT 
	}
	
	private ListingDirectories listingType;
	
	public PropertiesManager(String filename) {
		Properties prop = new Properties();
		
		try (FileInputStream input = new FileInputStream(filename)) {
			prop.load(input);
			String listing = prop.getProperty("listing.directories").toLowerCase();
			
			if (listing.equals("allowed")) {
				this.listingType = ListingDirectories.ALLOWED;
			}
			else if (listing.equals("denied")) {
				this.listingType = ListingDirectories.DENIED;
			}
			else {
				this.listingType = ListingDirectories.DEFAULT;
			}
		}
		catch (IOException ex) {
			this.listingType = ListingDirectories.DEFAULT;
		}

	}
	
	public ListingDirectories getListingType() {
		return this.listingType;
	}

}

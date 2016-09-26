package br.usp.each.ach2026;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
	
	public enum ListingDirectories {
		ALLOWED, DENIED, DEFAULT 
	}
	
	private ListingDirectories listingType;
	
	public PropertiesManager() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			String filename = "config.properties";
			input = new FileInputStream(filename);
			
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
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public ListingDirectories getListingType() {
		return this.listingType;
	}

}

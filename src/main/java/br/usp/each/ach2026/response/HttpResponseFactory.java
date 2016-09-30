package br.usp.each.ach2026.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import br.usp.each.ach2026.PropertiesManager.ListingDirectories;

public class HttpResponseFactory {
	
	public HttpResponse getResponse(String filename, boolean logged, ListingDirectories listing) throws Exception {
		FileInputStream fis = null;
		boolean fileExists = true;
		
		// abrir o arquivo requisitado
        try {
            fis = new FileInputStream(filename);
        } catch (final FileNotFoundException ex) {
            fileExists = false;
        }
        
        HttpResponse response = null;
        if (filename.contains("restrito") && !logged) {
        	response = new HttpResponseUnauthorized(filename);
        }
        else if (fileExists) {
        	if (filename.contains(".html")) {
        		response = new HttpResponseOkHtml(filename);
        	}
        	else {
        		response = new HttpResponseOkFile(filename);
            	response.setFileInputStream(fis);
        	}
        }
        else {
        	if (new File(filename).isDirectory()) {
        		switch (listing) {
        			case ALLOWED:
        				response = new HttpResponseOkDirectory(filename);
        				break;
        			
        			case DENIED:
        				response = new HttpResponseUnauthorized(filename);
        				break;
        			
        			case DEFAULT:
        				filename = filename + "/index.html";
        				try {
        		            fis = new FileInputStream(filename);
        		            response = new HttpResponseOkHtml(filename);
        		        } catch (final FileNotFoundException ex) {
        		            response = new HttpResponseNotFound(filename);
        		        }
        				break;
        		}
        	}
        	else {
        		response = new HttpResponseNotFound(filename);
        	}
        }
		
		return response;
	}

}

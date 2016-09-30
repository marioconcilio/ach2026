package br.usp.each.ach2026;

import java.io.BufferedReader;

import org.apache.commons.codec.binary.Base64;

public class AuthManager {
	
	private static final String USER = "user";
	private static final String PASS = "password";
	
	private BufferedReader br;
	
	public AuthManager(BufferedReader br) throws Exception {
		this.br = br;
	}
	
	public boolean isLogged() throws Exception {
		String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            if (headerLine.contains("Authorization:")) {
                final String[] authorizationHeader = headerLine.split(":");
                final String[] value = authorizationHeader[1].split(" ");
                final String encoded = encode(USER + ":" + PASS);
                
                if (value[0].equalsIgnoreCase("Basic") && value[1].equals(encoded)) {
                    return true;
                }
            }

        }
		
		return false;
	}
	
	public String encode(final String value) {
		return new Base64().encodeToString(value.getBytes());
	}

}

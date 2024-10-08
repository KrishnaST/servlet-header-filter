package com.paytech.valves;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

public class AllInOneValve extends ValveBase {

	private static final long DEFAULT_MAX_AGE = 2592000;
    
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
    	if("https".equalsIgnoreCase(request.getScheme())) {
    		 response.addHeader("Strict-Transport-Security", "includeSubDomains; preload; max-age="+ DEFAULT_MAX_AGE);
    	}
        response.addHeader("content-security-policy", "upgrade-insecure-requests;");
        response.addHeader("x-xss-protection", "1; mode=block");
        response.addHeader("x-frame-options", "SAMEORIGIN");
        response.addHeader("x-content-type-options", "nosniff");
        response.addHeader("referrer-policy", "strict-origin-when-cross-origin");
        response.addHeader("permissions-policy", "geolocation=self");
        
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "*");

        response.getCookies().forEach(cookie -> {
        	cookie.setHttpOnly(true);
        	cookie.setPath("/mwtTransXT");
        	cookie.setSecure(true);
        });
        //response.addHeader("Set-Cookie", "__Secure-JSESSIONID="+DatatypeConverter.printHexBinary(bytes)+"; Path=/mwtTransXT; Secure; HttpOnly; SameSite=Strict");
        getNext().invoke(request, response);
    }

}
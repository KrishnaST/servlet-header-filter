package com.paytech.jakarta.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class ResponseHeaderFilter implements Filter {

	private static final Logger logger = Logger.getLogger(ResponseHeaderFilter.class.getName());
	
	private final Map<String, String> headers = new HashMap<>();
	
	@Override
	public final void init(final FilterConfig filterConfig) throws ServletException {
		final Enumeration<String> paramNames = filterConfig.getInitParameterNames();
		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			final String paramValue = filterConfig.getInitParameter(paramName);
			if(paramName != null && paramValue != null) headers.put(paramName, paramValue);
		}
		logger.info("ResponseHeaderFilter initialized with headers --> "+headers);
	}

	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.finest("ServletResponse class --> "+response.getClass().getName());
		if (response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			logger.finest("URL -> "+httpRequest.getRequestURL().toString());
            final HttpServletResponse httpResponse = (HttpServletResponse) response;
            if (response.isCommitted()) throw new ServletException("Response already commited.");
            headers.forEach((k,v) -> httpResponse.addHeader(k, v));
            logger.finest("applying headers --> "+headers);
        } else logger.finest("not a HttpServletResponse implementation class --> "+response.getClass().getName());
        chain.doFilter(request, response);
	}

}

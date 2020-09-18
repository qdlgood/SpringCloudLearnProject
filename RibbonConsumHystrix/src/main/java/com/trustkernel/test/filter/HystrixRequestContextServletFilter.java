package com.trustkernel.test.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@WebFilter(filterName = "hystrixRequestContextServletFilter", urlPatterns = "/*", asyncSupported = true)
public class HystrixRequestContextServletFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 //初始化Hystrix请求上下文
		 HystrixRequestContext context = HystrixRequestContext.initializeContext();
         chain.doFilter(request, response);
         context.close();
	}

	@Override
	public void destroy() {
	}

}

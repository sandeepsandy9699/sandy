package com.paas.sms.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

public class RouteFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		return null;
	}

	@Override
	public String filterType() {
		System.out.println("Using Route Filter");
		return "route";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}

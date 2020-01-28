package com.paas.sms.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.paas.sms.zuul.filter.ErrorFilter;
import com.paas.sms.zuul.filter.PostFilter;
import com.paas.sms.zuul.filter.PreFilter;
import com.paas.sms.zuul.filter.RouteFilter;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class PaaszuulserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaaszuulserviceApplication.class, args);
	}
	
	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}
	@Bean
	public PostFilter postFilter() {
	    return new PostFilter();
	}
	@Bean
	public ErrorFilter errorFilter() {
	    return new ErrorFilter();
	}
	@Bean
	public RouteFilter routeFilter() {
	    return new RouteFilter();
	}

}

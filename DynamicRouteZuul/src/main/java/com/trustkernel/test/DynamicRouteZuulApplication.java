package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class DynamicRouteZuulApplication {

	public static void main(String[] args) {
        SpringApplication.run(DynamicRouteZuulApplication.class, args);
	}

}

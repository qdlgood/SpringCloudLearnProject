package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class RibbonCustomerApplication {
	
	@Bean //将此Bean交给spring容器管理
	@LoadBalanced //此注解开启 负载均衡
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
	
		SpringApplication.run(RibbonCustomerApplication.class, args);
	}

}

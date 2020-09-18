package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients //开启feign
@SpringCloudApplication
public class FeignCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignCustomerApplication.class, args);
	}

}

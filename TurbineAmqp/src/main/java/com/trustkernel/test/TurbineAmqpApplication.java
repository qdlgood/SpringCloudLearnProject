package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

@EnableTurbineStream
@SpringCloudApplication
public class TurbineAmqpApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurbineAmqpApplication.class, args);
	}

}

package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaRegisterCenterApplication {

	public static void main(String[] args) {
		// 启动 eureka注册中心 应用
        SpringApplication.run(EurekaRegisterCenterApplication.class, args);
	}

}

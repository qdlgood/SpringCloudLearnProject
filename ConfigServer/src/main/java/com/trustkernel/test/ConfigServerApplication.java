package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
/**
 * 这里不能使用@SpringCloudApplication注解，会报错
 * 原因：pom文件没有依赖hystrix
 * @author qdl
 *
 */
@SpringBootApplication
@EnableConfigServer   //开启config 的server功能
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}

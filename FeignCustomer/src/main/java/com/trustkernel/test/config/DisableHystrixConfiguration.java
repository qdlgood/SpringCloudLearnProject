package com.trustkernel.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import feign.Feign;

/**
 * 用于禁用Feign中Hystrix的配置
 */
public class DisableHystrixConfiguration {
	
	 @Bean
	 @Scope("prototype")
	 public Feign.Builder feignBuilder(){
	        return Feign.builder();
	 }

}

package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.trustkernel.test.filter.AccessFilter;

@EnableZuulProxy //开启zuul网关服务功能
@SpringCloudApplication
//@Import(AccessFilter.class)
public class ZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}

//	@Bean
//    public AccessFilter accessFilter(){
//        return new AccessFilter();
//    }
}

package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy //开启zuul网关服务功能：只要添加这个注解默认路由配置(http://localhost:当前服务端口号/服务提供者的服务名/方法路径)就会生效
@SpringCloudApplication
public class DynamicRouteZuulApplication {

	public static void main(String[] args) {
        SpringApplication.run(DynamicRouteZuulApplication.class, args);
	}

}

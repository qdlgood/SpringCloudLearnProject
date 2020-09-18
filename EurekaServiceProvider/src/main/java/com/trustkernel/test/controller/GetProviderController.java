package com.trustkernel.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.trustkernel.test.entity.User;

/**
 * 服务提供者:get请求
 * @author qdl
 *
 */
@RestController
public class GetProviderController {
	
	@Autowired
    private DiscoveryClient discoveryClient; //注入发现客户端
	
	private final Logger logger = LoggerFactory.getLogger(GetProviderController.class);
	
	@GetMapping(value = "/hello")
	public String hello() {
		ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
		logger.info("打印hello host: "+serviceInstance.getHost()+" service_id: "+serviceInstance.getServiceId());
		return "hello";
	}
	
	@GetMapping(value = "/getParameter/{name}")
	public String getParameter(@PathVariable String name) {
		ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
		logger.info("getParameter host: "+serviceInstance.getHost()+" service_id: "+serviceInstance.getServiceId());
	    return "getParameter "+name;
	}
	
	@GetMapping(value = "/getUser")
	public User getUser() {
		ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
		logger.info("getUser host: "+serviceInstance.getHost()+" port: "+serviceInstance.getPort()+" serviceInstanceid: "+serviceInstance.getServiceId());
		return new User("zaozao", "woman", "123456789");
	}
}

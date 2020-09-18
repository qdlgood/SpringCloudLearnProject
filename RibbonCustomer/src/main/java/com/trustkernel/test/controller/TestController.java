package com.trustkernel.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/ribbon-customer", method = RequestMethod.GET)
	public String helloCustomer() {
		//"http://hello-service/test",hello-service是服务名替代主机名，因此不会暴露接口真实地址
		return restTemplate.getForEntity("http://hello-service/test", String.class).getBody();
	}

}

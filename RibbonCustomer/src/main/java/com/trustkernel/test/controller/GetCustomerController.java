package com.trustkernel.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.trustkernel.test.entity.User;

/**
 * ribbon服务消费者:get请求
 * @author qdl
 *
 */
@RestController
public class GetCustomerController {
	
	@Autowired
	private RestTemplate restTemplate; //注入restTemplate
	
	private final Logger logger = LoggerFactory.getLogger(GetCustomerController.class);
	
	@GetMapping(value = "/customer/hello")
    public String useGetForEntity() {
		 //使用restTemplate调用微服务接口,hello-service是服务名
		return restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
	}
	
	/**
     *ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
               * 使用占位符对参数进行替换，内部使用String.format方法实现
     */
	@GetMapping(value = "/customer/getParameter")
    public String useGetForEntityByParam(@RequestParam String name) {
		 //使用restTemplate调用微服务接口,hello-service是服务名
		return restTemplate.getForEntity("http://hello-service/getParameter/{1}", String.class, name).getBody();
	}
	
	@GetMapping(value = "/customer/getUser")
	public User useGetForEntityGetUser() {
		ResponseEntity<User> entity = restTemplate.getForEntity("http://hello-service/getUser", User.class);
	    User user = entity.getBody();
	    logger.info("user: "+ user);
	    return user;
	}
	
	/**
     * getForEntity方法内部会提取map中，以占位符为key的值作为参数回填入url中
     * ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables)
     */
	@GetMapping(value = "/customer/getParameter/{name}")
	public String useGetForEntityByMap(@PathVariable String name) {
		Map<String, String> map = new HashMap<>();
		map.put("key", name);
		return restTemplate.getForEntity("http://hello-service/getParameter/{key}", String.class, map).getBody();
	}
	
	/**
	 * T getForObject(String url, Class<T> responseType)
	 */
	@GetMapping(value = "/customer/getUserV2")
	public User useGetForObjectGetUser() {
		return restTemplate.getForObject("http://hello-service/getUser", User.class);
	}
}

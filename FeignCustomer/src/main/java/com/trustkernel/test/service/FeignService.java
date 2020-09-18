package com.trustkernel.test.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.trustkernel.test.config.DisableHystrixConfiguration;
import com.trustkernel.test.entity.User;

//@FeignClient(value = "hello-service",configuration = DisableHystrixConfiguration.class) 
//其中的value的值为要调用服务的名称, configuration中配置的是禁用Feign中的Hystrix功能的类

@FeignClient(value = "hello-service", fallback = FeignServiceFallback.class) //其中的value的值为要调用服务的名称，fallback中配置的是服务降级的类（由于Feign会自动重试，所以配置并没有生效）
public interface FeignService {
	
	//@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@GetMapping("/hello")
	String helloFeign();
	
	 /**
                 * 在服务提供者我们有一个方法是用直接写在链接，SpringMVC中用的@PathVariable
                 * 这里边和SpringMVC中有些有一点点出入，SpringMVC中只有一个参数而且参数名的话是不用额外指定参数名的，而feign中必须指定,不然程序会出错
     */
	@RequestMapping(value = "/getParameter/{names}",method = RequestMethod.GET)
	String getParameterFeign(@PathVariable("names") String name);
	
	/**
	 * 同上：也需要指定参数名
	 */
    @GetMapping("/merge/users")
	List<User> getUserListByIds(@RequestParam("ids") List<Long> idList);
    
    @PostMapping("/user")
    User getUser(@RequestBody User user);
    
    /**
               * 测试重连机制
     */
    @RequestMapping(value = "/retry", method = RequestMethod.GET)
    String feignRetry();

}

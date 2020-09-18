package com.trustkernel.test.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trustkernel.test.entity.User;
import com.trustkernel.test.service.FeignService;
import com.trustkernel.test.service.ProviderFeignService;

@RestController
@RequestMapping("/feign")
public class FeignController {
	
	@Autowired
	private FeignService feignService;
	
	@Autowired
	private ProviderFeignService providerFeignService;
	
	private final Logger logger = LoggerFactory.getLogger(FeignController.class);
	
	@GetMapping(value = "/hello")
	public String sayHello() {
		return feignService.helloFeign();
	}
	
	@GetMapping(value = "/getParameter/{test}")
	public String getParameter(@PathVariable String test) {
		return feignService.getParameterFeign(test);
	}
	
	@GetMapping("/getUsers")
	public List<User> getUsers(@RequestParam List<Long> idList) {
		return feignService.getUserListByIds(idList);
	}
	
	@PostMapping("/getUser")
	public User getUser(@RequestBody User u) {
		return feignService.getUser(u);
	}
	
	@GetMapping("/refactor/{name}")
	public String refactorProviderService(@PathVariable String name) {
		return providerFeignService.hello(name);
	}
	
   /**
             * 测试重连机制
    */
    @GetMapping("/retry")
    public String retry(){
        return feignService.feignRetry();
    }

}

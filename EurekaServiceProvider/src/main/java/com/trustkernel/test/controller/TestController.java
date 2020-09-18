package com.trustkernel.test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trustkernel.test.entity.User;
import com.trustkernel.test.service.ProviderService;

@RestController
public class TestController implements ProviderService{
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	private final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
		logger.info("host: " + serviceInstance.getHost() + " service_id: " + serviceInstance.getServiceId());
	    return "test is successful";
	}
	
	/**
	 * 请求缓存是在同一请求多次访问中保证只调用一次这个服务提供者的接口，在这同一次请求第一次的结果会被缓存，保证同一请求中同样的多次访问返回结果相同
	  * 为了请求测试Hystrix（请求缓存）提供的返回随机数的接口
	 * @return 0~99998
	 */
    @GetMapping("/hystrix/cache")
    public Integer getRandomInteger(){
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        return randomInt;
    }
    
    /**
               * 为Hystrix（请求合并）提供的接口
     */
    @GetMapping("/merge/users/{id}")
    public User getUserById(@PathVariable Long id){
        logger.info("=========getUserById方法：入参ids："+id);
        return new User("one"+id, "女", "110-"+id);
    }

    @GetMapping("/merge/users")
    public List<User> getUsersByIds(@RequestParam List<Long> ids){
        List<User> userList = new ArrayList<>();
        User user;
        logger.info("=========getUsersByIds方法：入参ids："+ids);
        for(Long id : ids){
            user = new User("person"+id , "男", "123-"+id);
            userList.add(user);
        }
        System.out.println(userList);
        return userList;
    }
    
    /**
               * 重写接口的方法需注意的几点：1.@RestController注解必须加上
     * 2.@GetMapping等url之类的不用写
     * 3.方法的参数注解依旧要写
     */
	@Override
	public String hello(@PathVariable String name) {
		logger.info("refactorProviderService的hello方法执行了，入参：name:{}", name);
        return "hello, "+name;
	}
    
    /**
               * 测试Feign延迟重试的代码
               * 这里我们为这个方法加上超过Feign默认6000ms(一定 要大于ribbon.ReadTimeout设置的值才能看到效果) 的延迟，我们只需要通过查看日志输出即可
     */
    @GetMapping("/retry")
    public String feignRetry() {
        logger.info("feignRetry方法调用成功");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}

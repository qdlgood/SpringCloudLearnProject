package com.trustkernel.test.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.trustkernel.test.entity.User;

/**
 * 服务提供者:post请求
 * @author qdl
 *
 */
@RestController
public class PostProviderController {
	
	private final Logger logger = LoggerFactory.getLogger(PostProviderController.class);
	
	/**
	 * 接收一个对象再返回回去,postForE
	 * 
	 * ntity/postForObject方法通用
	 */
    @PostMapping("/user")
    public User returnUserByPost(@RequestBody User user){
        logger.info("/use接口 "+user);
        if(user == null) return new User("这是一个空对象","","");
        return user;
    }

    /**
              * 测试PostForEntity方法的参数，可以直接看输出判断结果了
     */
    @PostMapping("/user/{str}")
    public User returnUserByPost(@PathVariable String str, @RequestBody User user){
        logger.info("/user/someparam 接口传参 name："+str +","+user);
        if(user == null) return new User("这是一个空对象","","");
        return user;
    }

    /**
             * 为postForLocation方法返回URI
     */
    @PostMapping("/location")
    public URI returnURI(@RequestBody User user){
        //这里模拟一个url，真实资源位置不一定是这里
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://hello-service/location").build().expand(user).encode();
        URI toUri = uriComponents.toUri();
        logger.info("/provider location uri:"+toUri);
        return toUri;
    }

	
	

}

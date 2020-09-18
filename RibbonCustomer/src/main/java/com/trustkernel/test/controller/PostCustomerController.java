package com.trustkernel.test.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.trustkernel.test.entity.User;

/**
 * ribbon服务消费者:post请求
 * @author qdl
 *
 */
@RestController
public class PostCustomerController {
	
	@Autowired
	private RestTemplate restTemplate; //注入restTemplate
	
	private final Logger logger = LoggerFactory.getLogger(PostCustomerController.class);
	
	@PostMapping("/entity")
    public User postForEntity(){
        User user = new User("zaozao","1","678912345");
        ResponseEntity<User> entity = restTemplate.postForEntity("http://hello-service/user/{str}", user, User.class, "测试参数");
        User body = entity.getBody(); //所有restTemplate.*ForEntity方法都是包装类，body为返回类型对象
        return body;
    }

    /**
     * 使用URI传参，测试结果会显示在服务提供者的终端中
     * ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType)
     */
    @PostMapping("/entity/uri")
    public User postForEntityByURI(){
        User user = new User("老张","1","678912345");
        //这里只是将url转成uri，并没有添加参数
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://hello-service/user").build().encode();
        URI toUri = uriComponents.toUri();
        //使用user传参
        User object = restTemplate.postForObject(toUri, user, User.class);
        return object;
    }

    /**
     * 这里测试postForObject方法，需要注意的参数如上述方法的描述，区别只是不需要getBody了
     * postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
     * postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
     */
    @PostMapping("/object")
    public User postForObject(){
        User user = new User("zaozao","1","123654987");
        //这里url传1是为了调用服务者项目中的一个接口
        User responseBody = restTemplate.postForObject("http://hello-service/user/1", user, User.class);
        return responseBody;
    }

    /**
     * post请求还有一种：postForLocation，这里也同样有三种重载，除了无需指定返回类型外，用法相同，返回类型均为URI
     * postForLocation(String url, Object request, Object... uriVariables)
     * postForLocation(String url, Object request, Map<String, ?> uriVariables)
     * postForLocation(URI url, Object request)
     */
    @PostMapping("/customer/location")
    public URI postForLocation(){
        User user = new User("zaozao","1","987654321");
        URI uri = restTemplate.postForLocation("http://hello-service/location", user);
        //不知道为什么返回来是空，这个方法仅供参考吧
        logger.info("/customer location uri:"+uri);
        return uri;
    }
}

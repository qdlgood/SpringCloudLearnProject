package com.trustkernel.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.trustkernel.test.entity.User;

/**
 * ribbon服务消费者:put&delete请求
 * @author qdl
 *
 */
@RestController
public class PutAndDeleteCustomerController {
	
	@Autowired
	private RestTemplate restTemplate; //注入restTemplate
	
	private final Logger logger = LoggerFactory.getLogger(PutAndDeleteCustomerController.class);
	
	/**
     * put请求示例，一般put请求多用作修改
     */
    @PutMapping("/put")
    public void put(@RequestBody User user){
        restTemplate.put("http://hello-service/put", user);
    }

    /**
     * delete请求示例
     */
    @DeleteMapping("/del/{id}")
    public void delete(@PathVariable Long id){
        restTemplate.delete("http://hello-service/delete/{1}", id);
    }

}

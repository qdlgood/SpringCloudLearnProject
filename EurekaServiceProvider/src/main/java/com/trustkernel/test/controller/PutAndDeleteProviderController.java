package com.trustkernel.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trustkernel.test.entity.User;

/**
 * 服务提供者 put&delete请求
 * @author qdl
 *
 */
@RestController
public class PutAndDeleteProviderController {
	
	private final Logger logger = LoggerFactory.getLogger(PutAndDeleteProviderController.class);
	
	@PutMapping("/put")
    public void put(@RequestBody User user){
        logger.info("/put user: " + user.toString());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        logger.info("/delete id: " + id);
    }

}

package com.trustkernel.test.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProviderService {
	
	/**
     * 声明一个接口，没有实现
     * 必需指定参数名
     */
    @GetMapping(value = "/refactor-service/{name}")
    String hello(@PathVariable("name") String name);

}

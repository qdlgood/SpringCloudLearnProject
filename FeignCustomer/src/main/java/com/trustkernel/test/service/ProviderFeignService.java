package com.trustkernel.test.service;

import org.springframework.cloud.netflix.feign.FeignClient;
/**
 * 继承服务提供者ProviderService接口，从而拥有这个接口所有的方法
 * 那么在这个Feign中就只需要使用ProviderService定义的接口方法
 * @author qdl
 *
 */
@FeignClient(value = "hello-service")
public interface ProviderFeignService extends ProviderService{

}

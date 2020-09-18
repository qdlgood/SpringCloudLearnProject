package com.trustkernel.test.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trustkernel.test.entity.User;
import com.trustkernel.test.service.FeignService;

/**
 * 服务降级配置
 * @author qdl
 *
 */
@Component //把此类做为Bean交给Spring管理
public class FeignServiceFallback implements FeignService{
    
	/**
	 * 这里只用这个方法举例，返回一个“失败”
	 */
	@Override
	public String helloFeign() {
		return "失败";
	}

	@Override
	public String getParameterFeign(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUserListByIds(List<Long> idList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String feignRetry() {
		// TODO Auto-generated method stub
		return null;
	}

}

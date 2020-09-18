package com.trustkernel.test.hystrix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.trustkernel.test.entity.User;
import com.trustkernel.test.service.RibbonHystrixService;

/**
 * 批量请求命令的实现：用来调用服务提供者的接口
 * @author qdl
 *
 */
public class UserBatchCommand extends HystrixCommand<List<User>>{
	
	private final Logger logger = LoggerFactory.getLogger(UserBatchCommand.class);
	
	private RibbonHystrixService ribbonHystrixService;
	
	private List<Long> ids;
	
	protected UserBatchCommand( RibbonHystrixService ribbonHystrixService, List<Long> ids) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("userBatchCommand")));
		this.ribbonHystrixService = ribbonHystrixService;
		this.ids = ids;
	}

	@Override
	protected List<User> run() throws Exception {
		List<User> users = ribbonHystrixService.findAll(ids);
        logger.info("UserBatchCommand类的run方法： " + users);
        return users;
	}
	
	/**
     * Fallback回调方法，如果没有会报错
     */
    @Override
    protected List<User> getFallback(){
        logger.info("UserBatchCommand的run方法，调用失败");
        return null;
    }

}

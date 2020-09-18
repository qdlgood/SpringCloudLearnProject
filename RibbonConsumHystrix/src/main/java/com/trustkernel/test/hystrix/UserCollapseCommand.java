package com.trustkernel.test.hystrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import com.trustkernel.test.entity.User;
import com.trustkernel.test.service.RibbonHystrixService;

/**
 * 这是一个将请求合并的类：继承HystrixCollapser的请求合并器
 * @author qdl
 *
 */
public class UserCollapseCommand  extends HystrixCollapser<List<User>,User,Long> {
	
	private RibbonHystrixService ribbonHystrixService;
	
	private Long id;
	
	/**
	 * 构造方法，主要用来设置这个合并器的时间，意为每多少毫秒就会合并一次
	 * @param ribbonHystrixService 调用的服务
	 * @param id 单个请求传入的参数
	 */
	public UserCollapseCommand(RibbonHystrixService ribbonHystrixService, Long id) {
		super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("userCollapseCommand"))
				.andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
		this.ribbonHystrixService = ribbonHystrixService;
		this.id = id;
	}

	/*
	 * 获取请求中的参数
	 */
	@Override
	public Long getRequestArgument() {
		return id;
	}

	/**
              * 创建命令，执行批量操作
     */
	@Override
	protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> requests) {
		//按请求数声名UserId的集合
        List<Long> idList = new ArrayList<>(requests.size());
        //通过请求将100毫秒中的请求参数取出来装进集合中
        idList.addAll(requests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        //返回UserBatchCommand对象，自动执行UserBatchCommand的run方法
        return new UserBatchCommand(ribbonHystrixService, idList);
	}
	
	/**
               * 将返回的结果匹配回请求中
     * @param batchResponse 批量操作的结果
     * @param collapsedRequests 合在一起的请求
     */
	@Override
	protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, Long>> requests) {
		int count = 0 ;
        for(CollapsedRequest<User,Long> collapsedRequest : requests){
            //从批响应集合中按顺序取出结果
            User user = batchResponse.get(count++);
            //将结果放回原Request的响应体内
            collapsedRequest.setResponse(user);
        }
		
	}

}

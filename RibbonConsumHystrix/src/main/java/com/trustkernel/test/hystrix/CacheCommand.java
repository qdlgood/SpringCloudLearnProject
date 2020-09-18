package com.trustkernel.test.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

public class CacheCommand extends HystrixCommand<Integer>{
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Long id;
	
	public CacheCommand(Setter setter, RestTemplate restTemplate, Long id) {
		super(setter);
		this.restTemplate = restTemplate;
        this.id = id;
	}
    
	/*
	 * 调用产生随机数的接口
	 */
	@Override
	protected Integer run() throws Exception {
		return restTemplate.getForObject("http://hello-service/hystrix/cache",Integer.class);
	}
	
	/*
	 * 开启请求缓存，只需重载getCacheKey方法
	 * 因为我们这里使用的是id，不同的请求来请求的时候会有不同cacheKey所以，同一请求第一次访问会调用，之后都会走缓存
               * 好处：   1.减少请求数、降低并发
     *     2.同一用户上下文数据一致
     *     3.这个方法会在run()和construct()方法之前执行，减少线程开支
	 */
	@Override
	public String getCacheKey() {
		return String.valueOf(id);//保证同一请求返回同一值即可
	}

	/*
	 * 清理缓存
	 * 我们需要把之前的cache清掉
              * 说明 ：1.其中getInstance方法中的第一个参数的key名称要与实际相同
     *    2.clear方法中的cacheKey要与getCacheKey方法生成的key方法相同
     *    3.注意我们用了commandKey是test,大家要注意之后new这个Command的时候要指定相同的commandKey,否则会清除不成功
	 */
	public static void flushRequestCache(Long id){
        HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("test"), 
        		HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }

}

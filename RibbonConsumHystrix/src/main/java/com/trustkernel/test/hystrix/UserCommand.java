package com.trustkernel.test.hystrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.trustkernel.test.entity.User;

public class UserCommand extends HystrixCommand<User>{
	
	private RestTemplate restTemplate;
	
	private Long id;
	
	//main方法里同步和异步请求使用
	public UserCommand(Setter setter, RestTemplate restTemplate, Long id) {
		super(setter);
		this.restTemplate = restTemplate;
		this.id = id;
	}
	
	//=====================HystrixCommand构造方法=========================
	//只有setter的构造方法
    public UserCommand(Setter setter){
        super(setter);
    }
    //只设置组名的构造方法
    public UserCommand(HystrixCommandGroupKey group) {
        super(group);
    }
    //设置组名、线程池名
    public UserCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group,threadPool);
    }
    //设置组名和执行超时时间
    public UserCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group,executionIsolationThreadTimeoutInMilliseconds);
    }
    //设置组名、命令名、线程池名、执行超时时间
    public UserCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group,threadPool,executionIsolationThreadTimeoutInMilliseconds);
    }

	@Override
	protected User run() throws Exception {
      return restTemplate.getForObject("http://hello-service/getUser", User.class);
	}

}

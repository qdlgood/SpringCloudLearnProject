package com.trustkernel.test.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.trustkernel.test.entity.User;
import com.trustkernel.test.hystrix.CacheCommand;
import com.trustkernel.test.hystrix.UserCommand;

@Service
public class RibbonHystrixService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(RibbonHystrixService.class);
	
	@HystrixCommand(fallbackMethod = "hystrixFallback")
	public String helloService() {
	    long start = System.currentTimeMillis();
        //设置随机3秒内延迟，hystrix默认延迟2秒未返回则熔断，调用回调方法
        int sleepMillis = new Random().nextInt(3000);
        logger.info("----sleep-time:" + sleepMillis);

        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		//调用服务提供者的接口，正常则返回字符串
		String result = restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
		long end = System.currentTimeMillis();
		logger.info("----spend-time:"+(end-start));
		return result;
	}
	
	/**
	 * 调用服务失败处理的方法
	 */
	public String hystrixFallback() {
		return "error";
	}
	

    /**
               * 使用自定义HystrixCommand（继承方式）同步方法调用接口
     */
    public User useSyncRequestGetUser(){
        //这里使用Spring注入的RestTemplate, Spring注入的对象都是静态的
        User userSync = new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                restTemplate ,0L).execute();

        return userSync;
    }

    /**
               * 使用自定义HystrixCommand（继承方式）异步方法调用接口
     */
    public User useAsyncRequestGetUser(){
    	Future<User> userFuture = new UserCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
 				HystrixCommandGroupKey.Factory.asKey("")).andCommandPropertiesDefaults(
 						HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
 				restTemplate, 0L).queue();
        User userAsync = null;
        try {
         	userAsync = userFuture.get();
 		} catch (InterruptedException | ExecutionException e) {
 			e.printStackTrace();
 		}

        return userAsync;
    }
    
    /**
               * 使用注解方式实现异步请求调用
               * 注意：此处AsyncResult为netfix实现，spring也做了实现，注意导包。
     */
    @HystrixCommand(fallbackMethod = "fallbackForUserTypeReturnMethod")
    public Future<User> asyncRequest(){
        return new AsyncResult<User>() {
			@Override
			public User invoke() {
				return restTemplate.getForObject("http://hello-service/getUser", User.class);
			}
		};
    }

    /**
               * 调用服务失败处理方法：返回类型为User
     */
    public User fallbackForUserTypeReturnMethod(){
        return null;
    }
    
    /**
               * 继承方式开启请求缓存,注意commandKey必须与清除的commandKey一致
     */
    public void openCacheByExtends(){
        CacheCommand command1 = new CacheCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                                HystrixCommandGroupKey.Factory.asKey("group")).andCommandKey(
                                HystrixCommandKey.Factory.asKey("test")), restTemplate, 1L);
        
        CacheCommand command2 = new CacheCommand(com.netflix.hystrix.HystrixCommand.Setter.withGroupKey(
                                HystrixCommandGroupKey.Factory.asKey("group")).andCommandKey(
                                HystrixCommandKey.Factory.asKey("test")), restTemplate, 1L);
        
        Integer result1 = command1.execute();
        Integer result2 = command2.execute();
        //第一次和第二次结果一样
        logger.info("first request result is:{} ,and secend request result is: {}", result1, result2);
    }

    /**
               * 继承方式清除请除缓存
     */
    public void clearCacheByExtends(){
        CacheCommand.flushRequestCache(1L);
        logger.info("请求缓存已清空！");
    }
    
    /**
               * 使用注解请求缓存 方式1
     * @CacheResult  标记这是一个缓存方法，结果会被缓存
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "commandKey1")
    public Integer openCacheByAnnotation1(Long id){
        //此次结果会被缓存
        return restTemplate.getForObject("http://hello-service/hystrix/cache", Integer.class);
    }

    /**
              * 使用注解清除缓存 方式1
     * @CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @CacheRemove(commandKey = "commandKey1", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation1(Long id){
        logger.info("请求缓存已清空！");
        //这个@CacheRemove注解直接用在更新方法上效果更好
    }

    /**
               * 第一种方法没有使用@CacheKey注解，而是使用这个方法进行生成cacheKey的替换办法
               * 这里有两点要特别注意：
     * 1、这个方法的入参的类型必须与缓存方法的入参类型相同，如果不同被调用会报这个方法找不到的异常
     * 2、这个方法的返回值一定是String类型
     */
    public String getCacheKey(Long id){
        return String.valueOf(id);
    }
    
    /**
               * 使用注解请求缓存 方式2
     * @CacheResult  标记这是一个缓存方法，结果会被缓存
     * @CacheKey 使用这个注解会把最近的参数作为cacheKey
               * 注意：1.当参数是对象时，可使用@CacheKey("属性名")指定特定参数
     *    2.不使用@CacheKey，则会使用所有参数列表中的参数作为cacheKey
     */
    @CacheResult
    @HystrixCommand(commandKey = "commandKey2")
    public Integer openCacheByAnnotation2(@CacheKey Long id){
        //此次结果会被缓存
        return restTemplate.getForObject("http://hello-service/hystrix/cache", Integer.class);
    }

    /**
               * 使用注解清除缓存 方式2
     * @CacheRemove 必须指定commandKey才能进行清除指定缓存
     */
    @CacheRemove(commandKey = "commandKey2")
    public void flushCacheByAnnotation2(@CacheKey Long id){
        logger.info("请求缓存已清空！");
    }
    
    /**请求合并使用到的测试方法**/

    /**
               * 查一个User对象
     */
    public User findOne(Long id){
        logger.info("findOne方法执行了，id= "+id);
        return restTemplate.getForObject("http://hello-service/merge/users/{1}", User.class, id);
    }

    /**
               * 查多个对象
               * 注意： 这里用的是数组，作为结果的接收，因为restTemplate.getForObject方法在这里受限
               *              如果尽如《SpringCloud微服务实战》一书中指定类型为List.class，会返回一个List<LinkedHashMap>类型的集合
               *              为了避坑这里我们使用数组的方式接收结果
     */
    public List<User> findAll(List<Long> ids){
    	logger.info("findAll方法执行了，ids = "+ids);
    	//第一种
//    	User[] users = restTemplate.getForObject("http://hello-service/merge/users?ids={1}", User[].class, StringUtils.join(ids, ","));
//    	logger.info("findAll方法： users is " + users);
//    	return Arrays.asList(users);
    	
    	//第二种
    	List<User> list = new ArrayList<>();
    	User user;
        List<LinkedHashMap<String, String>> linkedHashMaps = restTemplate.getForObject("http://hello-service/merge/users?ids={1}", List.class, StringUtils.join(ids, ","));
        for (LinkedHashMap<String, String> linkedHashMap : linkedHashMaps) {
        	user = new User();
        	user.setName(linkedHashMap.get("name").toString());
        	user.setSex(linkedHashMap.get("sex").toString());
			user.setPhone(linkedHashMap.get("phone").toString());
		    list.add(user);
		}
        logger.info("findAll方法： users is " + list);
        return list;
    }
    
    /**注解方式实现请求合并**/

    /**
               * 被合并请求的方法
               * 注意是timerDelayInMilliseconds，注意拼写
     */
    @HystrixCollapser(batchMethod = "findAllByAnnotation", collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds", value = "100")})
    public Future<User> findOneByAnnotation(Long id){
        //其实根本不会进入这个方法体
    	logger.info("不会被打印。。。");
        return null;
    }

    /**
               * 真正执行的方法
     */
    @HystrixCommand
    public List<User> findAllByAnnotation(List<Long> ids){
    	logger.info("findAllByAnnotation方法执行了，ids= " + ids);
        User[] users = restTemplate.getForObject("http://hello-service/merge/users?ids={1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(users);
    }

}

package com.trustkernel.test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.trustkernel.test.entity.User;
import com.trustkernel.test.hystrix.UserCollapseCommand;
import com.trustkernel.test.service.RibbonHystrixService;

@RestController
@RequestMapping("hystrix")
public class RibbonHystrixController {
	
	@Autowired
	RibbonHystrixService service;
	
	private final Logger logger = LoggerFactory.getLogger(RibbonHystrixController.class);
	
	@GetMapping("/test")
	public String helloHystrix() {
		//调用服务层的方法
		return service.helloService();
	}
	
	//同步调用：获取到结果直接返回并立即显示结果
    //异步调用：获取到结果，延迟直到调用，结果才显示
	
	/**
               * 继承方式：发送同步请求，实现自定义Hystrix
     */
    @GetMapping("/sync")
    public User sendSyncRequestGetUser(){
        return service.useSyncRequestGetUser();
    }

    /**
               * 继承方式：发送异步请求，实现自定义Hystrix
     */
    @GetMapping("/async")
    public User sendAsyncRequestGetUser(){
        return service.useAsyncRequestGetUser();
    }
    
    /**
               * 注解方式：发送异步请求
     */
    @GetMapping("/annotationasync")
    public User sendAsyncRequestByAnnotation(){
        Future<User> userFuture = service.asyncRequest();
        try {
            return userFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
              * 继承方式：开启请求缓存,并多次调用CacheCommand的方法
               * 在两次请求之间加入清除缓存的方法
     */
    @GetMapping("/cacheextends")
    public void openCacheTest(){
        //初始化Hystrix请求上下文
        HystrixRequestContext.initializeContext();
        //开启请求缓存并测试两次
        service.openCacheByExtends();
        //清除缓存
        service.clearCacheByExtends();
        //再次开启请求缓存并测试两次
        service.openCacheByExtends();
    }
    
    /**
               * 注解方式：开启请求缓存，第一种
     */
    @GetMapping("/cacheannotation1")
    public void openCacheByAnnotation1(){
        //初始化Hystrix请求上下文
        HystrixRequestContext.initializeContext();
        //访问并开启缓存
        Integer result1 = service.openCacheByAnnotation1(1L);
        Integer result2 = service.openCacheByAnnotation1(1L);
        logger.info("first request result is:{} ,and secend request result is: {}", result1, result2);
        //清除缓存
        service.flushCacheByAnnotation1(1L);
        //再一次访问并开启缓存
        Integer result3 = service.openCacheByAnnotation1(1L);
        Integer result4 = service.openCacheByAnnotation1(1L);
        logger.info("first request result is:{} ,and secend request result is: {}", result3, result4);
    }
    
    /**
               * 注解方式：开启请求缓存，第二种
     */
    @GetMapping("/cacheannotation2")
    public void openCacheByAnnotation2(){
        //初始化Hystrix请求上下文
        //HystrixRequestContext.initializeContext(); //将其注释的原因：加了Filter类做了处理
        //访问并开启缓存
        Integer result1 = service.openCacheByAnnotation2(2L);
        Integer result2 = service.openCacheByAnnotation2(2L);
        logger.info("first request result is:{} ,and secend request result is: {}", result1, result2);
        //清除缓存
        service.flushCacheByAnnotation2(2L);
        //再一次访问并开启缓存
        Integer result3 = service.openCacheByAnnotation2(2L);
        Integer result4 = service.openCacheByAnnotation2(2L);
        logger.info("first request result is:{} ,and secend request result is: {}", result3, result4);
    }
    
    /**
              * 单个请求处理
     * @param id
     */
    @GetMapping("/users/{id}")
    public User findOne(@PathVariable Long id){
        logger.debug("=============/hystrix/users/{} 执行了", id);
        User user = service.findOne(id);
        return user;
    }

    /**
              * 多个请求处理
     * @param ids 使用逗号分隔
     */
    @GetMapping("/users")
    public List<User> findAll(@RequestParam List<Long> ids){
        logger.debug("=============/hystrix/users?ids={} 执行了", ids);
        return service.findAll(ids);
    }
    
    /**
               * 继承方式： 异步方法，测试合并请求
               * 说明：这个测试本应在findOne方法中new一个UserCollapseCommand对象进行测试
               *         苦于没有好的办法做并发实验，这里就放在一个Controller中了
               *         我们看到，在这个方法中用了三个UserCollapseCommand对象进行模拟高并发
     */
    @GetMapping("/asynccollapse")
    public List<User> asyncCollapseTest(){
        logger.info("==========>asyncCollapseTest方法执行了");
        List<User> userList = new ArrayList<>();
        Future<User> queue1 = new UserCollapseCommand(service, 1L).queue();
        Future<User> queue2 = new UserCollapseCommand(service, 2L).queue();
        Future<User> queue3 = new UserCollapseCommand(service, 3L).queue();
        try {
            User user1 = queue1.get();
            User user2 = queue2.get();
            User user3 = queue3.get();
            userList.add(user1);
            userList.add(user2);
            userList.add(user3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
               *  继承方式：同步方法，测试请求合并
               * 说明：这个方法是用来与上面的方法做类比的，通过这个实验我们发现如果使用同步方法，
               *             那么这个请求合并的作用就没有用了，这会给findAll方法造成性能浪费
     */
    @GetMapping("/synccollapse")
    public List<User> syncCollapseTest(){
        logger.info("==========>syncCollapseTest方法执行了");
        List<User> userList = new ArrayList<>();
        User user1 = new UserCollapseCommand(service, 1L).execute();
        User user2 = new UserCollapseCommand(service, 2L).execute();
        User user3 = new UserCollapseCommand(service, 3L).execute();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        return userList;
    }
    
    /**
               * 注解方式：异步方法，测试请求合并
     */
    @GetMapping("/collapsebyannotation")
    public List<User> collapseByAnnotation() throws ExecutionException, InterruptedException {
    	List<User> userList = new ArrayList<>();
        Future<User> one = service.findOneByAnnotation(1L);
        Future<User> two = service.findOneByAnnotation(2L);
        userList.add(one.get());
        userList.add(two.get());
        return userList;
    }

}

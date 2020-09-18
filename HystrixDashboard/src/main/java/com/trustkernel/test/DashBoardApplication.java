package com.trustkernel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
/**
   *    仪表盘 启动主类
 * @author qdl
 *
 */
@SpringBootApplication
@EnableHystrixDashboard
public class DashBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashBoardApplication.class, args);
	}

}

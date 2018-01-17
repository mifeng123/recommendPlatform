package com.okay.consumerTpZuti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableHystrixDashboard
@EnableCircuitBreaker
//一定不能少@EnableCircuitBreaker，刚开始就是少这个报的unable to connect错误
public class ConsumerTpZutiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerTpZutiApplication.class, args);
    }
}

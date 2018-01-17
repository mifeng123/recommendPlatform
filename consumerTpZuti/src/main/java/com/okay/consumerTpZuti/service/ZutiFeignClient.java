package com.okay.consumerTpZuti.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-teacherspace-zuti", fallbackFactory = FeignClientFallbackFactory.class)
public interface ZutiFeignClient {
    @GetMapping("/zuti")
    int add(@RequestParam("a") int a, @RequestParam("b") int b);
}

@Component
class FeignClientFallbackFactory implements FallbackFactory<ZutiFeignClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallbackFactory.class);

    @Override
    public ZutiFeignClient create(Throwable cause) {
        return new ZutiFeignClient() {
            @Override
            public int add(@RequestParam("a") int a, @RequestParam("b") int b) {
                // 日志最好放在各个fallback方法中，而不要直接放在create方法中。
                // 否则在引用启动时，就会打印该日志。
                // 详见https://github.com/spring-cloud/spring-cloud-netflix/issues/1471
                FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause);
                return -9999;
            }
        };
    }
}
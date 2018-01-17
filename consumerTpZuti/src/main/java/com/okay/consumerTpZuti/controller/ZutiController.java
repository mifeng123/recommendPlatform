package com.okay.consumerTpZuti.controller;

import com.okay.consumerTpZuti.service.ZutiFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@RestController
public class ZutiController {
    @Autowired
    ZutiFeignClient zutiFeignClient;

    @GetMapping("/add")
    public int add(@RequestParam Integer a, @RequestParam Integer b) {
        System.out.println(this.zutiFeignClient);
        return zutiFeignClient.add(a,b);
    }
}

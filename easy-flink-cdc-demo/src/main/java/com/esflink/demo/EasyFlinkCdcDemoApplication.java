package com.esflink.demo;

import cn.easyes.starter.register.EsMapperScan;
import com.esflink.starter.annotation.EasyFlinkScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EasyFlinkScan("com.esflink.demo.sink")
@SpringBootApplication
@EsMapperScan("com.esflink.demo.mapper.es")
public class EasyFlinkCdcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyFlinkCdcDemoApplication.class, args);

        System.out.println("init");
    }

}

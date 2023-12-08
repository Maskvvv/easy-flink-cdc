package com.esflink.demo;

import com.esflink.starter.annotation.EasyFlinkScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EasyFlinkScan("com.esflink.demo.sink")
@SpringBootApplication
public class EasyFlinkCdcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyFlinkCdcDemoApplication.class, args);

        System.out.println("init");
    }

}

package com.esflink.demo;

import com.esflink.starter.annotation.EasyFlinkScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EasyFlinkScan("com.esflink.demo.sink")
@SpringBootApplication
public class EasyFlinkCdcDemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(EasyFlinkCdcDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EasyFlinkCdcDemoApplication.class, args);

        logger.info("init");
    }

}

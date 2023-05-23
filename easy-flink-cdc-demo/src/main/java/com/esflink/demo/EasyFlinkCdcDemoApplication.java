package com.esflink.demo;

import com.esflink.starter.annotation.EasyFlinkScan;
import com.esflink.starter.context.FlinkSinkHolder;
import com.esflink.starter.data.FlinkDataChangeSink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@EasyFlinkScan("com.esflink.demo.sink")
@SpringBootApplication
public class EasyFlinkCdcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyFlinkCdcDemoApplication.class, args);

        Map<String, List<FlinkDataChangeSink>> sinkMap = FlinkSinkHolder.SINK_MAP;

        System.out.println("init");
    }

}

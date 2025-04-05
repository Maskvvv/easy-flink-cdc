package com.esflink.demo.config;

import org.apache.doris.flink.cfg.DorisExecutionOptions;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.cfg.DorisReadOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.doris.flink.sink.writer.serializer.SimpleStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 描述
 *
 * @author xlh
 * @date 2025/1/7
 * @desc
 */
@Configuration
public class DorisConfig {

    @Bean
    public DorisSink.Builder<String> buildOptions() {
        DorisSink.Builder<String> builder = DorisSink.builder();
        Properties properties = new Properties();
// 上游是json数据的时候，需要开启以下配置
        properties.setProperty("read_json_by_line", "true");
        properties.setProperty("format", "json");

        DorisExecutionOptions executionOptions = DorisExecutionOptions.builder()
                .setLabelPrefix("label-doris")
                .setDeletable(false)
                .setBatchMode(true)  //开启攒批写入
                .setBufferFlushMaxRows(30000)
                .setStreamLoadProp(properties)
                .build();

        builder.setDorisReadOptions(DorisReadOptions.builder().build())
                .setDorisExecutionOptions(executionOptions)
                .setSerializer(new SimpleStringSerializer());
        return builder;
    }
}

package com.esflink.starter.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取配置文件
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
public abstract class AbstractFlinkPropertiesParser implements FlinkPropertiesParser {

    protected List<Field> getFlinkPropertiesFields() {
        List<String> filedNames = new ArrayList<>();
        Class<FlinkProperties> flinkPropertiesClass = FlinkProperties.class;
        Field[] fields = flinkPropertiesClass.getDeclaredFields();

        return Arrays.asList(fields);
    }
}

package com.esflink.starter.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.springframework.core.io.Resource;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取配置文件
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
public class DefaultFlinkPropertiesParser extends AbstractFlinkPropertiesParser {

    @Override
    public List<FlinkProperties> getProperties(Resource resource) {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            Config config = ConfigFactory.parseReader(reader).resolve();

            return parser(config);
        } catch (Exception e) {
            throw new RuntimeException("FlinkPropertiesParser error to read config: " + resource, e);
        }
    }

    /**
     * 解析 Config 为 FlinkProperties
     *
     * @author zhouhongyin
     * @since 2023/5/23 17:37
     */
    private List<FlinkProperties> parser(Config config) throws IllegalAccessException {
        List<FlinkProperties> flinkPropertiesList = new ArrayList<>();

        ConfigObject root = config.root();
        for (Map.Entry<String, ConfigValue> rootEntry : root.entrySet()) {
            String rootName = rootEntry.getKey();
            Config properties = config.getConfig(rootName);

            List<Field> flinkPropertiesFields = getFlinkPropertiesFields();
            FlinkProperties flinkProperties = new FlinkProperties();
            flinkProperties.setName(rootName);
            for (Field filed : flinkPropertiesFields) {
                String filedName = filed.getName();
                String filedValue = properties.getString(filedName);

                filed.setAccessible(true);
                filed.set(flinkProperties, filedValue);
            }

            flinkPropertiesList.add(flinkProperties);
        }
        return flinkPropertiesList;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        FlinkProperties flinkProperties = new FlinkProperties();

        Class<? extends FlinkProperties> flinkPropertiesClass = flinkProperties.getClass();
        Field name = flinkPropertiesClass.getDeclaredField("name");
        name.setAccessible(true);

        name.set(flinkProperties, "1111");
        System.out.println(flinkProperties.getName());
    }

}

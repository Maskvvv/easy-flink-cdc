package com.esflink.starter.properties.parser;

import com.esflink.starter.properties.FlinkJobProperties;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
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
 * typesafe 配置文件解析
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
public class DefaultFlinkListenerPropertiesParser extends AbstractFlinkListenerPropertiesParser {

    public static final String NAME = "local";

    @Override
    public List<FlinkJobProperties> getProperties(Resource resource) {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            Config config = ConfigFactory.parseReader(reader).resolve();

            return parser(config);
        } catch (Exception e) {
            throw new RuntimeException("FlinkPropertiesParser error to read config: " + resource, e);
        }
    }

    /**
     * 解析 Config 为 FlinkJobProperties
     *
     * @author zhouhongyin
     * @since 2023/5/23 17:37
     */
    private List<FlinkJobProperties> parser(Config config) throws IllegalAccessException {
        List<FlinkJobProperties> flinkJobPropertiesList = new ArrayList<>();

        ConfigObject root = config.root();
        for (Map.Entry<String, ConfigValue> rootEntry : root.entrySet()) {
            String rootName = rootEntry.getKey();
            Config properties = config.getConfig(rootName);

            List<Field> flinkPropertiesFields = getFlinkPropertiesFields();
            FlinkJobProperties flinkJobProperties = new FlinkJobProperties();
            flinkJobProperties.setName(rootName);
            for (Field filed : flinkPropertiesFields) {
                String filedName = filed.getName();
                String filedValue = "";
                try {
                    filedValue = properties.getString(filedName);
                } catch (ConfigException.Missing exception) {
                    continue;
                }

                filed.setAccessible(true);
                filed.set(flinkJobProperties, filedValue);
            }

            flinkJobPropertiesList.add(flinkJobProperties);
        }
        return flinkJobPropertiesList;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        FlinkJobProperties flinkJobProperties = new FlinkJobProperties();

        Class<? extends FlinkJobProperties> flinkPropertiesClass = flinkJobProperties.getClass();
        Field name = flinkPropertiesClass.getDeclaredField("name");
        name.setAccessible(true);

        name.set(flinkJobProperties, "1111");
        System.out.println(flinkJobProperties.getName());
    }

}

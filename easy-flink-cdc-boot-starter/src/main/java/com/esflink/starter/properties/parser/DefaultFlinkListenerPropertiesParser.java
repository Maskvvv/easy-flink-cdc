package com.esflink.starter.properties.parser;

import com.esflink.starter.properties.FlinkListenerProperties;
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
 * typesafe 配置文件解析
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
public class DefaultFlinkListenerPropertiesParser extends AbstractFlinkListenerPropertiesParser {

    public static final String NAME = "local";

    @Override
    public List<FlinkListenerProperties> getProperties(Resource resource) {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            Config config = ConfigFactory.parseReader(reader).resolve();

            return parser(config);
        } catch (Exception e) {
            throw new RuntimeException("FlinkPropertiesParser error to read config: " + resource, e);
        }
    }

    /**
     * 解析 Config 为 FlinkListenerProperties
     *
     * @author zhouhongyin
     * @since 2023/5/23 17:37
     */
    private List<FlinkListenerProperties> parser(Config config) throws IllegalAccessException {
        List<FlinkListenerProperties> flinkListenerPropertiesList = new ArrayList<>();

        ConfigObject root = config.root();
        for (Map.Entry<String, ConfigValue> rootEntry : root.entrySet()) {
            String rootName = rootEntry.getKey();
            Config properties = config.getConfig(rootName);

            List<Field> flinkPropertiesFields = getFlinkPropertiesFields();
            FlinkListenerProperties flinkListenerProperties = new FlinkListenerProperties();
            flinkListenerProperties.setName(rootName);
            for (Field filed : flinkPropertiesFields) {
                String filedName = filed.getName();
                String filedValue = properties.getString(filedName);

                filed.setAccessible(true);
                filed.set(flinkListenerProperties, filedValue);
            }

            flinkListenerPropertiesList.add(flinkListenerProperties);
        }
        return flinkListenerPropertiesList;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        FlinkListenerProperties flinkListenerProperties = new FlinkListenerProperties();

        Class<? extends FlinkListenerProperties> flinkPropertiesClass = flinkListenerProperties.getClass();
        Field name = flinkPropertiesClass.getDeclaredField("name");
        name.setAccessible(true);

        name.set(flinkListenerProperties, "1111");
        System.out.println(flinkListenerProperties.getName());
    }

}

package com.esflink.starter.meta;


import com.esflink.starter.properties.EasyFlinkProperties;

import java.io.Serializable;
import java.util.Objects;

/**
 * flink 标识
 *
 * @author zhouhongyin
 * @since 2023/5/31 23:07
 */
public class FlinkJobIdentity implements Serializable {

    private String applicationName;

    private String port;

    private String flinkJobName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFlinkJobName() {
        return flinkJobName;
    }

    public void setFlinkJobName(String flinkJobName) {
        this.flinkJobName = flinkJobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlinkJobIdentity)) return false;
        FlinkJobIdentity that = (FlinkJobIdentity) o;
        return applicationName.equals(that.applicationName) && port.equals(that.port) && flinkJobName.equals(that.flinkJobName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, port, flinkJobName);
    }

    public static FlinkJobIdentity generate(EasyFlinkProperties.Meta meta, String flinkJobName) {
        String applicationName = meta.getApplicationName();
        String port = meta.getPort();

        FlinkJobIdentity flinkJobIdentity = new FlinkJobIdentity();
        flinkJobIdentity.setApplicationName(applicationName);
        flinkJobIdentity.setPort(port);
        flinkJobIdentity.setFlinkJobName(flinkJobName);
        return flinkJobIdentity;

    }
}

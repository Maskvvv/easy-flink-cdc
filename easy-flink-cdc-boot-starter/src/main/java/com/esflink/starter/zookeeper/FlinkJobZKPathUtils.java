package com.esflink.starter.zookeeper;

import com.esflink.starter.meta.FlinkJobIdentity;

/**
 * <p> FlinkJob Zookeeper path 生成器 </p>
 *
 * @author zhouhongyin
 * @since 2023/8/11 17:12
 */
public class FlinkJobZKPathUtils {

    public static final String ZOOKEEPER_SEPARATOR = "/";

    public static String ZK_PATH_PREFIX = ZOOKEEPER_SEPARATOR + "easyFlinkCdc";

    public static String create(FlinkJobIdentity flinkJobIdentity) {
        String path = ZK_PATH_PREFIX + ZOOKEEPER_SEPARATOR + flinkJobIdentity.getApplicationName() + flinkJobIdentity.getPort()
                + ZOOKEEPER_SEPARATOR + flinkJobIdentity.getFlinkJobName();

        return path;
    }

}

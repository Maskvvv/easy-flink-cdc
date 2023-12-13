package com.esflink.starter.common.utils;

import java.util.Optional;

import static com.esflink.starter.constants.BaseEsConstants.UNKNOWN;


public class EEVersionUtils {

    /**
     * 获取指定类版本号
     * <ul>
     *      <li>只能获取jar包版本，并且打包后META-INF/MANIFEST.MF文件中存在 Implementation-Version</li>
     *      <li>不存在 Implementation-Version 时返回 unknown</li>
     *      <li>如果获取EE本身版本需要打包后获取,在test包测试用例中无法获取</li>
     * </ul>
     *
     * @param <T>         泛型
     * @param objectClass T.getClass()
     * @return classVersion
     */
    public static <T> String getJarVersion(Class<T> objectClass) {
        return Optional.ofNullable(objectClass.getPackage().getImplementationVersion()).
                orElse(UNKNOWN);
    }

}

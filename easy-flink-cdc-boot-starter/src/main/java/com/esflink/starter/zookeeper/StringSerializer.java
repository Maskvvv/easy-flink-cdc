package com.esflink.starter.zookeeper;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.StandardCharsets;

/**
 * 基于string的序列化方式
 *
 * @author jianghang 2012-7-11 下午02:57:09
 * @version 1.0.0
 */
public class StringSerializer implements ZkSerializer {

    @Override
    public String deserialize(final byte[] bytes) throws ZkMarshallingError {
        return new String(bytes);
    }

    @Override
    public byte[] serialize(final Object data) throws ZkMarshallingError {
        if (data instanceof byte[]) {
            return (byte[]) data;
        } else {
            return ((String.valueOf(data))).getBytes(StandardCharsets.UTF_8);
        }
    }

}

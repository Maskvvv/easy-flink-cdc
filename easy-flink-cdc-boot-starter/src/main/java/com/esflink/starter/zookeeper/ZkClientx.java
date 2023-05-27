package com.esflink.starter.zookeeper;

import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;

public class ZkClientx extends ZkClient {


    public ZkClientx(String serverstring) {
        this(serverstring, Integer.MAX_VALUE);
    }

    public ZkClientx(String zkServers, int connectionTimeout) {
        this(new ZkConnection(zkServers), connectionTimeout);
    }

    public ZkClientx(String zkServers, int sessionTimeout, int connectionTimeout) {
        this(zkServers, sessionTimeout, connectionTimeout, new StringSerializer());
    }

    public ZkClientx(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
    }

    public ZkClientx(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer, long operationRetryTimeout) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer, operationRetryTimeout);
    }

    public ZkClientx(IZkConnection connection) {
        super(connection);
    }

    public ZkClientx(IZkConnection connection, int connectionTimeout) {
        this(connection, connectionTimeout, new StringSerializer());
    }

    public ZkClientx(IZkConnection zkConnection, int connectionTimeout, ZkSerializer zkSerializer) {
        super(zkConnection, connectionTimeout, zkSerializer);
    }

    public ZkClientx(IZkConnection zkConnection, int connectionTimeout, ZkSerializer zkSerializer, long operationRetryTimeout) {
        super(zkConnection, connectionTimeout, zkSerializer, operationRetryTimeout);
    }

    public String readDataString(String path) {
        return super.readData(path);
    }

    /**
     * Create a persistent Sequential node.
     *
     * @param path
     * @param createParents if true all parent dirs are created as well and no
     *                      {@link ZkNodeExistsException} is thrown in case the path already exists
     * @throws ZkInterruptedException   if operation was interrupted, or a
     *                                  required reconnection got interrupted
     * @throws IllegalArgumentException if called from anything except the
     *                                  ZooKeeper event thread
     * @throws ZkException              if any ZooKeeper exception occurred
     * @throws RuntimeException         if any other exception occurs
     */
    public String createPersistentSequential(String path, boolean createParents) throws ZkInterruptedException,
            IllegalArgumentException, ZkException,
            RuntimeException {
        try {
            return create(path, null, CreateMode.PERSISTENT_SEQUENTIAL);
        } catch (ZkNoNodeException e) {
            if (!createParents) {
                throw e;
            }
            String parentDir = path.substring(0, path.lastIndexOf('/'));
            createPersistent(parentDir, createParents);
            return createPersistentSequential(path, createParents);
        }
    }

    /**
     * Create a persistent Sequential node.
     *
     * @param path
     * @param data
     * @param createParents if true all parent dirs are created as well and no
     *                      {@link ZkNodeExistsException} is thrown in case the path already exists
     * @throws ZkInterruptedException   if operation was interrupted, or a
     *                                  required reconnection got interrupted
     * @throws IllegalArgumentException if called from anything except the
     *                                  ZooKeeper event thread
     * @throws ZkException              if any ZooKeeper exception occurred
     * @throws RuntimeException         if any other exception occurs
     */
    public String createPersistentSequential(String path, Object data, boolean createParents)
            throws ZkInterruptedException,
            IllegalArgumentException,
            ZkException,
            RuntimeException {
        try {
            return create(path, data, CreateMode.PERSISTENT_SEQUENTIAL);
        } catch (ZkNoNodeException e) {
            if (!createParents) {
                throw e;
            }
            String parentDir = path.substring(0, path.lastIndexOf('/'));
            createPersistent(parentDir, createParents);
            return createPersistentSequential(path, data, createParents);
        }
    }

    /**
     * Create a persistent Sequential node.
     *
     * @param path
     * @param data
     * @param createParents if true all parent dirs are created as well and no
     *                      {@link ZkNodeExistsException} is thrown in case the path already exists
     * @throws ZkInterruptedException   if operation was interrupted, or a
     *                                  required reconnection got interrupted
     * @throws IllegalArgumentException if called from anything except the
     *                                  ZooKeeper event thread
     * @throws ZkException              if any ZooKeeper exception occurred
     * @throws RuntimeException         if any other exception occurs
     */
    public void createPersistent(String path, Object data, boolean createParents) throws ZkInterruptedException,
            IllegalArgumentException, ZkException,
            RuntimeException {
        try {
            create(path, data, CreateMode.PERSISTENT);
        } catch (ZkNodeExistsException e) {
            if (!createParents) {
                throw e;
            }
        } catch (ZkNoNodeException e) {
            if (!createParents) {
                throw e;
            }
            String parentDir = path.substring(0, path.lastIndexOf('/'));
            createPersistent(parentDir, createParents);
            createPersistent(path, data, createParents);
        }
    }
}

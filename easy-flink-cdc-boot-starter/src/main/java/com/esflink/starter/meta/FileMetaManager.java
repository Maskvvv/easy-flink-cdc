package com.esflink.starter.meta;


/**
 * 基于文件刷新的metaManager实现
 *
 * <pre>
 * 策略：
 * 1. 先写内存，然后定时刷新数据到File
 * 2. 数据采取overwrite模式(只保留最后一次)
 * </pre>
 *
 * @author zhouhongyin
 * @version 1.0.4
 */
public class FileMetaManager extends MemoryMetaManager implements MetaManager {

    //private static final Logger logger = Logger.getLogger(FileMixedMetaManager.class.getName());
    //
    //
    //private static final Charset charset = StandardCharsets.UTF_8;
    ///**
    // * 游标文件存放路径
    // */
    //private File dataDir;
    //private String dataFileName = "meta.dat";
    //
    ///**
    // * flinkJob 游标文件map
    // */
    //private Map<String, File> dataFileCaches;
    //
    //private long period = 1000;
    //
    //private ScheduledExecutorService executor;
    //
    ///**
    // * 需要记录的游标任务
    // */
    //private Set<FlinkJobIdentity> updateCursorTasks;
    //
    //public void start() {
    //    super.start();
    //    if (!dataDir.exists()) {
    //        try {
    //            FileUtils.forceMkdir(dataDir);
    //        } catch (IOException e) {
    //            throw new MetaManagerException(e);
    //        }
    //    }
    //
    //    if (!dataDir.canRead() || !dataDir.canWrite()) {
    //        throw new MetaManagerException("dir[" + dataDir.getPath() + "] can not read/write");
    //    }
    //
    //    dataFileCaches = new ConcurrentHashMap<>();
    //
    //    updateCursorTasks = new CopyOnWriteArraySet<>();
    //
    //    executor = Executors.newScheduledThreadPool(1);
    //    // 启动定时工作任务
    //    executor.scheduleAtFixedRate(() -> {
    //                List<FlinkJobIdentity> tasks = new ArrayList<>(updateCursorTasks);
    //                for (FlinkJobIdentity flinkJobIdentity : tasks) {
    //                    try {
    //                        updateCursorTasks.remove(flinkJobIdentity);
    //
    //                        // 定时将内存中的最新值刷到file中，多次变更只刷一次
    //                        flushDataToFile(clientIdentity.getDestination());
    //                    } catch (Throwable e) {
    //                        // ignore
    //                        LogUtils.error("period update [" + flinkJobIdentity.toString() + "] curosr failed!", e.getMessage());
    //                    }
    //                }
    //            },
    //            period,
    //            period,
    //            TimeUnit.MILLISECONDS);
    //}
    //
    //public void stop() {
    //    flushDataToFile();// 刷新数据
    //
    //    super.stop();
    //    executor.shutdownNow();
    //    destinations.clear();
    //    batches.clear();
    //}
    //
    //
    //private void flushDataToFile() {
    //    for (String destination : destinations.keySet()) {
    //        flushDataToFile(destination);
    //    }
    //}
    //
    //private void flushDataToFile(String destination) {
    //    flushDataToFile(destination, dataFileCaches.get(destination));
    //}
    //
    //private void flushDataToFile(String destination, File dataFile) {
    //    FileMetaInstanceData data = new FileMetaInstanceData();
    //    if (destinations.containsKey(destination)) {
    //        synchronized (destination.intern()) { // 基于destination控制一下并发更新
    //            data.setDestination(destination);
    //
    //            List<FileMetaClientIdentityData> clientDatas = new ArrayList<>();
    //            List<ClientIdentity> clientIdentitys = destinations.get(destination);
    //            for (ClientIdentity clientIdentity : clientIdentitys) {
    //                FileMetaClientIdentityData clientData = new FileMetaClientIdentityData();
    //                clientData.setClientIdentity(clientIdentity);
    //                Position position = cursors.get(clientIdentity);
    //                if (position != null && position != nullCursor) {
    //                    clientData.setCursor((LogPosition) position);
    //                }
    //
    //                clientDatas.add(clientData);
    //            }
    //
    //            data.setClientDatas(clientDatas);
    //        }
    //        //fixed issue https://github.com/alibaba/canal/issues/4312
    //        //客户端数据为空时不覆盖文件内容 （适合单客户端）
    //        if (data.getClientDatas().isEmpty()) {
    //            return;
    //        }
    //        String json = JsonUtils.marshalToString(data);
    //        try {
    //            FileUtils.writeStringToFile(dataFile, json);
    //        } catch (IOException e) {
    //            throw new CanalMetaManagerException(e);
    //        }
    //    }
    //}
    //
    //private File getDataFile(String flinkJobName) {
    //    File destinationMetaDir = new File(dataDir, flinkJobName);
    //    if (!destinationMetaDir.exists()) {
    //        try {
    //            FileUtils.forceMkdir(destinationMetaDir);
    //        } catch (IOException e) {
    //            throw new MetaManagerException(e);
    //        }
    //    }
    //
    //    return new File(destinationMetaDir, dataFileName);
    //}
    //
    //
    //public void setDataDir(String dataDir) {
    //    this.dataDir = new File(dataDir);
    //}
    //
    //public void setDataDirByFile(File dataDir) {
    //    this.dataDir = dataDir;
    //}
    //
    //public void setPeriod(long period) {
    //    this.period = period;
    //}

}

package com.toocol.distribute.selector;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 主节点状态监听
 *
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/3/23 11:26
 */
public class MasterLeaderListener implements LeaderSelectorListener {
    private static final Logger logger = LoggerFactory.getLogger(MasterLeaderListener.class);
    private final AtomicBoolean end = new AtomicBoolean();

    private final String path;
    private final String id;
    private final Runnable winMasterRun;
    private final Runnable loseMasterRun;

    public MasterLeaderListener(String path, String id, Runnable winMasterRun, Runnable loseMasterRun) {
        this.path = path;
        this.id = id;
        this.winMasterRun = winMasterRun;
        this.loseMasterRun = loseMasterRun;
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        logger.info("当前节点选举成为主节点, path = {}, id = {}", client.getNamespace() + path, id);
        end.set(false);

        winMasterRun.run();
        while (true) {
            if (end.get()) {
                logger.info("当前节点失去主节点资格, path = {}, id = {}", client.getNamespace() + path, id);
                loseMasterRun.run();
                return;
            }
        }
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (client.getConnectionStateErrorPolicy().isErrorState(newState)) {
            end.set(true);
        }
    }
}

package cn.lz.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.logging.log4j.message.StringMapMessage;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ZNodeWatcherService {

    private final CuratorFramework curatorFramework;

    private Map<String, String> tokenTableMap;

    /**
     * 启动时同步zk上的数据
     * @param curatorFramework
     */
    public ZNodeWatcherService(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
        Properties properties = new Properties();
        try {
            // todo 不应耦合
            byte[] nodeData = curatorFramework.getData().forPath("/token");
            properties.load(new ByteArrayInputStream(nodeData));
            tokenTableMap = new ConcurrentHashMap<>((Map)properties);

        } catch (IOException e) {
            log.error("watcher load properties error", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String getTableNameByToken(String token) {
        String tableName = tokenTableMap.get(token);
        log.info("token : {} <=> tableName: {}", token, tableName);
        return tableName;
    }

    public void registerZNodeWatcher(String zNodePath) {
        try {
             CuratorCache cache = CuratorCache.build(curatorFramework, zNodePath);
            CuratorCacheListener listener = CuratorCacheListener.builder().forChanges((oldNode, node) -> {
                Properties properties = new Properties();
                try {
                    properties.load(new ByteArrayInputStream(node.getData()));
                    tokenTableMap = new ConcurrentHashMap<>((Map)properties);
                } catch (IOException e) {
                    log.error("watcher load properties error", e);
                    throw new RuntimeException(e);
                }
                log.info("oldNode path: {}, data: {}; \n newNode path: {}, data: {}", oldNode.getPath(), new String(oldNode.getData()), node.getPath(), new String(node.getData()));
            }).build();

            cache.listenable().addListener(listener);
            cache.start();
            log.info("Registration is successful");
        } catch (Exception e) {
            log.error("register watch error");
        }
    }
}

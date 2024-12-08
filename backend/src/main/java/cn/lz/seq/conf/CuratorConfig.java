package cn.lz.seq.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CuratorConfig {
    @Bean("curatorFramework")
    public CuratorFramework initZk(@Autowired ZKProp zkProp) {

        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkProp.getElapsedTimeMs(), zkProp.getRetryCount());
            CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                    .connectString(zkProp.getConnectString()).retryPolicy(retryPolicy)
                    .sessionTimeoutMs(zkProp.getSessionTimeoutMs())
                    .connectionTimeoutMs(zkProp.getConnectionTimeoutMs())
                    .namespace(zkProp.getPath())
                    .build();
            curatorFramework.start();

            return curatorFramework;
        } catch (Exception e) {
            log.error("连接zk失败", e);
            throw e;
        }
    }
}

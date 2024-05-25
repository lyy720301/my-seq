package cn.lz.zk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ZkRegisterRunner implements CommandLineRunner {

    private final ZNodeWatcherService zNodeWatcherService;

    @Autowired
    public ZkRegisterRunner(ZNodeWatcherService zNodeWatcherService) {
        this.zNodeWatcherService = zNodeWatcherService;
    }

    @Override
    public void run(String... args) {
        // 注册ZNode监听器
        zNodeWatcherService.registerZNodeWatcher("/token");
    }
}

package cn.lz.service;

import cn.lz.conf.DynamicDataSource;
import cn.lz.conf.DynamicDataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RandomRoutingStrategy implements RoutingStrategy {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    /**
     *
     */
    private void handleFaultDataSource() {
        Map<String, Long> faultDataSourceMap = dynamicDataSource.getFaultDataSourceMap();
        Set<Map.Entry<String, Long>> entries = faultDataSourceMap.entrySet();
        Iterator<Map.Entry<String, Long>> iterator = entries.iterator();
        List faultList = new ArrayList();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> next = iterator.next();
            if (System.currentTimeMillis() - next.getValue() > 10000) {
                faultDataSourceMap.remove(next.getKey());
                synchronized (dynamicDataSource.getAllAliveDataSourceKeys()) {
                    if (!dynamicDataSource.getAllAliveDataSourceKeys().contains(next.getKey())) {
                        dynamicDataSource.getAllAliveDataSourceKeys().add(next.getKey());
                    }
                }
            } else {
                faultList.add(next.getKey());
            }
        }
        if (!faultList.isEmpty()) {
            dynamicDataSource.getAllAliveDataSourceKeys().removeAll(faultList);


        }
    }

    @Override
    public String selectDb() {
        List<String> dataSourceKeys = dynamicDataSource.getAllAliveDataSourceKeys();
        if (!dynamicDataSource.getFaultDataSourceMap().isEmpty()) {
            handleFaultDataSource();
        }
        // 创建一个 Random 对象
        Random random = new Random();
        int randomIndex = random.nextInt(dataSourceKeys.size());
        String randomItem = dataSourceKeys.get(randomIndex);
        // 设置当前上下文的数据源No
        DynamicDataSourceContextHolder.setDateSourceNo(randomItem);
        return randomItem;
    }
}

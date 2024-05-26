package cn.lz.service;

import cn.lz.conf.DynamicDataSource;
import cn.lz.conf.DynamicDataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class RandomRoutingStrategy implements RoutingStrategy {

    @Autowired
    DynamicDataSource dynamicDataSource;

    @Override
    public String selectDb() {
        List<String> dataSourceKeys = dynamicDataSource.getDataSourceKeys();
        // 创建一个 Random 对象
        Random random = new Random();
        int randomIndex = random.nextInt(dataSourceKeys.size());
        String randomItem = dataSourceKeys.get(randomIndex);
        // 设置当前上下文的数据源No
        DynamicDataSourceContextHolder.setDateSourceNo(randomItem);
        return randomItem;
    }
}

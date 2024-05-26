package cn.lz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 路由策略工厂
 */
@Component
public class RoutingStrategyFactory {

    private final Map<String, RoutingStrategy> routingStrategyMap = new HashMap<>();
    ;

    @Autowired
    private RandomRoutingStrategy randomRoutingStrategy;

    private RoutingStrategyFactory() {
        this.routingStrategyMap.put("random", randomRoutingStrategy);
    }

    public RoutingStrategy getReceiptHandleStrategy(String receiptType) {
        RoutingStrategy routingStrategy = routingStrategyMap.get(receiptType);
        if (routingStrategy == null) {
            return randomRoutingStrategy;
        }
        return routingStrategy;
    }
}
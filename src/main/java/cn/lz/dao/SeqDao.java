package cn.lz.dao;


import cn.lz.conf.DynamicDataSource;
import cn.lz.conf.DynamicDataSourceContextHolder;
import cn.lz.service.RoutingStrategy;
import cn.lz.service.RoutingStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
public class SeqDao {

    @Resource(name = "dynamicDataSource")
    private DynamicDataSource dataSource;

    @Autowired
    private RoutingStrategyFactory routingStrategyFactory;

    public long getSeq(String tableName) {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            // todo 对接zk
            RoutingStrategy routingStrategy = routingStrategyFactory.getReceiptHandleStrategy("random");
            // 根据当前策略选择数据库
            var dataSourceNo = routingStrategy.selectDb();
            log.debug("当前策略选定的数据源No: {}", dataSourceNo);
            connection.setAutoCommit(true);
            String sql = "replace into " + tableName + " (stub) values ('0')";
            log.debug("执行sql: {}", sql);
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                long result = keys.getLong(1);
                log.debug("mysql get last insert id = {}", result);
                return result;
            }
            throw new RuntimeException("replace into error");
        } catch (SQLException e) {
            log.error("getSeq error", e);
            throw new RuntimeException(e);
        } finally {
            // threadLocal 中的数据不用后要即时清理
            DynamicDataSourceContextHolder.clearDateSourceNos();
        }
    }
}

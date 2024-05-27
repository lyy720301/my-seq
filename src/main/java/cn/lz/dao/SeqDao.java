package cn.lz.dao;


import cn.lz.conf.DynamicDataSourceContextHolder;
import cn.lz.service.RoutingStrategy;
import cn.lz.service.RoutingStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@Component
public class SeqDao {

    /**
     * 不知为什么，使用jdbcTemplate比直接使用DruidDataSource效率更高
     */
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoutingStrategyFactory routingStrategyFactory;

    public long getSeq(String tableName) {
        try {
            // todo 对接zk
            // todo 迁移至service
            RoutingStrategy routingStrategy = routingStrategyFactory.getReceiptHandleStrategy("random");
            // 根据当前策略选择数据库
            var dataSourceNo = routingStrategy.selectDb();
            log.debug("当前策略选定的数据源No: {}", dataSourceNo);

            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "replace into " + tableName + " (stub) values ('0')";
            log.debug("执行sql: {}", sql);
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // "id" 是主键列的列名

                    return ps;
                }
            }, keyHolder);
            var res = keyHolder.getKeyList().get(0).get("GENERATED_KEY");
            return (Long) res;
        } catch (Exception e) {
            log.error("从数据库获取id失败 ", e);
            throw e;
        } finally {
            // threadLocal 中的数据不用后要即时清理
            DynamicDataSourceContextHolder.clearDateSourceNos();
        }
    }
}

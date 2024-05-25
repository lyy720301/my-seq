package cn.lz.dao;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
public class SeqDao {

    @Resource(name = "edenDataSource")
    private DataSource dataSource;

    public long getSeq(String tableName) {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            log.info("ready to table: {}", tableName);
            statement.executeUpdate("replace into " + tableName + " (stub) values ('0')", Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                long result = keys.getLong(1);
                log.info("mysql get last insert id = {}",result);
                return result;
            }
            throw new RuntimeException("replace into error");
        } catch (SQLException e) {
            log.error("getSeq error", e);
            throw new RuntimeException(e);
        }
    }
}

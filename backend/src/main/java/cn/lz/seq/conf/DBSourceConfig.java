package cn.lz.seq.conf;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


@Configuration
public class DBSourceConfig {

    /*
    druid
     */

    @ConditionalOnProperty(name = "datasource.useHikari", havingValue = "false")
    @Bean(name = "edenDataSource")
    @ConfigurationProperties("datasource.druid.one")
    public DataSource dataSourceOne() {
        return new DruidDataSource();
    }

    @ConditionalOnProperty(name = "datasource.useHikari", havingValue = "false")
    @Bean(name = "oddDataSource")
    @ConfigurationProperties("datasource.druid.two")
    public DataSource dataSourceTwo() {
        return new DruidDataSource();
    }

    /*
    hikari
     */

    @ConditionalOnProperty(name = "datasource.useHikari", havingValue = "true")
    @Bean(name = "oddDataSource")
    @ConfigurationProperties("datasource.hikari.one")
    public DataSource hikariDataSourceOne() {
        return new HikariDataSource();
    }

    @ConditionalOnProperty(name = "datasource.useHikari", havingValue = "true")
    @Bean(name = "edenDataSource")
    @ConfigurationProperties("datasource.hikari.two")
    public DataSource hikariDataSourceTwo() {
        return new HikariDataSource();
    }

    /**
     * 用Primary标记来让jdbcTemplate依赖
     */
    @Primary
    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("edenDataSource") DataSource dataSourceOne,
                                               @Qualifier("oddDataSource") DataSource dataSourceTwo) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<String, DataSource> map = new HashMap<>();
        map.put("1", dataSourceOne);
        map.put("2", dataSourceTwo);
        dynamicDataSource.setTargetDataSources((Map) map);
        dynamicDataSource.setDefaultTargetDataSource(dataSourceOne);
        dynamicDataSource.setAllAliveDataSourceKeys(new CopyOnWriteArrayList<>(Arrays.asList("1", "2")));

        return dynamicDataSource;

    }

}

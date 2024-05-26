package cn.lz.conf;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DBSourceConfig {


    @Bean(name = "edenDataSource")
    @ConfigurationProperties("datasource.one")
    public DataSource dataSourceOne() {
        return new DruidDataSource();
    }

    @Bean(name = "oddDataSource")
    @ConfigurationProperties("datasource.two")
    public DataSource dataSourceTwo() {
        return new DruidDataSource();
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
        dynamicDataSource.setDataSourceKeys(new ArrayList<>(Arrays.asList("1", "2")));

        return dynamicDataSource;

    }
}

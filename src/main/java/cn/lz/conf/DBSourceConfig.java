package cn.lz.conf;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


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
}

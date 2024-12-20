package cn.lz.seq.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public DataSourceFailoverAspect dataSourceFailoverAspect() {
        return new DataSourceFailoverAspect();
    }
}

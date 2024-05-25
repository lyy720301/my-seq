package cn.lz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class}
)

public class IDGeneratorApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(IDGeneratorApplication.class);
        log.info("启动成功!");
    }
}
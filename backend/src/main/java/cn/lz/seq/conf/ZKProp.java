package cn.lz.seq.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "curator")
public class ZKProp {
    private String connectString;
    private String path;
    private int retryCount;
    private int elapsedTimeMs;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
}

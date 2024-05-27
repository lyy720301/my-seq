package cn.lz.conf;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private List<String> allDataSourceKeys;

    /**
     * 所有存活的节点
     */
    @Getter
    private List<String> allAliveDataSourceKeys;

    @Getter
    private Map<String, Long> faultDataSourceMap = new ConcurrentHashMap<>();


    @Override
    protected Object determineCurrentLookupKey() {
        String dateSourceType = DynamicDataSourceContextHolder.getDateSourceNo();
        log.debug("选取数据源No: {}", dateSourceType);
        return dateSourceType;
    }

    public void setAllAliveDataSourceKeys(List<String> allAliveDataSourceKeys) {
        this.allAliveDataSourceKeys = allAliveDataSourceKeys;
        this.allDataSourceKeys = new ArrayList<>(allAliveDataSourceKeys);
    }

    public void setFaultDataSourceMap(Map<String, Long> faultDataSourceMap) {
        this.faultDataSourceMap = faultDataSourceMap;
    }


}
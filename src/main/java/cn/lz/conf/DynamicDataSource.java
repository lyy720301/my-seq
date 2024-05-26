package cn.lz.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private List<String> dataSourceKeys;


    @Override
    protected Object determineCurrentLookupKey() {
        String dateSourceType = DynamicDataSourceContextHolder.getDateSourceNo();
        log.info("选取数据源No: {}", dateSourceType);
        return dateSourceType;
    }

    public List<String> getDataSourceKeys() {
        return dataSourceKeys;
    }

    public void setDataSourceKeys(List<String> dataSourceKeys) {
        this.dataSourceKeys = dataSourceKeys;
    }


}
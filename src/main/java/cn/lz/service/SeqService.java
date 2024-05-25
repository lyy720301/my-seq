package cn.lz.service;

import cn.lz.dao.SeqDao;
import cn.lz.zk.ZNodeWatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SeqService {

    @Autowired
    private SeqDao seqDao;

    @Autowired
    private ZNodeWatcherService zNodeWatcherService;

    public long getSeq(String token) {
        String tableName = zNodeWatcherService.getTableNameByToken(token);
        if (tableName == null) {
            return -1;
        }
        return seqDao.getSeq(tableName);
    }
}

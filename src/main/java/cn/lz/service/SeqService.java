package cn.lz.service;

import cn.lz.dao.SeqDao;
import cn.lz.zk.ZNodeWatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeqService {

    @Autowired
    private SeqDao seqDao;

    @Autowired
    private ZNodeWatcherService zNodeWatcherService;

    public long getSeq(String token) {
        String tableName = zNodeWatcherService.getTableNameByToken(token);
        // todo 应定义异常
        if (tableName == null) {
            return -1;
        }
        return seqDao.getSeq(tableName);
    }
}

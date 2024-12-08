package cn.lz.seq.service;

import cn.lz.seq.api.SeqService;
import cn.lz.seq.dao.SeqDao;
import cn.lz.seq.zk.ZNodeWatcherService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class SeqServiceImpl implements SeqService {

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

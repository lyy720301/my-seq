package cn.lz.seq.service;

import cn.lz.seq.api.SeqService;
import cn.lz.seq.dao.SeqDao;
import cn.lz.seq.zk.ZNodeWatcherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@DubboService
public class SeqServiceImpl implements SeqService {

    @Autowired
    private SeqDao seqDao;

    @Autowired
    private ZNodeWatcherService zNodeWatcherService;

    public long getSeq(String token) {
        String tableName = zNodeWatcherService.getTableNameByToken(token);
        if (tableName == null) {
            log.info("token: {}, seq: {}", token, -1);
            return -1;
        }
        long seq = seqDao.getSeq(tableName);
        log.info("token: {}, seq: {}", token, seq);
        return seq;
    }
}

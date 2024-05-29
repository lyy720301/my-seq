package cn.lz.conf;

import cn.lz.service.RoutingStrategy;
import cn.lz.service.RoutingStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

@Slf4j
@Aspect
public class DataSourceFailoverAspect {

    @Autowired
    private RoutingStrategyFactory routingStrategyFactory;

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Pointcut("execution(* cn.lz.dao.SeqDao.getSeq(..))")
    public void dataAccessOperation() {
    }

    /**
     * 选择路由的数据库
     */
    @Around("dataAccessOperation()")
    public Object afterThrowingDataAccessOperationException(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("Entering method {} of class {}", methodName, className);
        // 在每个数据访问操作之前，可以设置默认的数据源
        RoutingStrategy routingStrategy = routingStrategyFactory.getReceiptHandleStrategy("random");
        // 根据当前策略选择数据库
        var dataSourceNo = routingStrategy.selectDb();
        log.debug("当前策略选定的数据源No: {}", dataSourceNo);
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            // 判断异常类型，如果是数据访问异常，则尝试切换数据源
            if (e instanceof CannotGetJdbcConnectionException) {
                // 描黑当前数据源
                dynamicDataSource.getFaultDataSourceMap().put(DynamicDataSourceContextHolder.getDateSourceNo(), System.currentTimeMillis());
                DynamicDataSourceContextHolder.clearDateSourceNos();
                try {
                    // 重新选择选择数据库
                    var dataSourceNo2 = routingStrategy.selectDb();
                    log.info("当前数据源No: {} 故障，尝试进行转移 -> {}", dataSourceNo, dataSourceNo2);
                    return joinPoint.proceed();
                } catch (Throwable ex) {
                    log.info("指定切点失败，不再进行转移");
                    throw new RuntimeException(ex);
                }
            } else {
                log.error("获取id失败, 数据源No: {}", dataSourceNo, e);
                throw e;
            }
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            DynamicDataSourceContextHolder.clearDateSourceNos();
            log.debug("Exiting method {} of class {} took {} ms", methodName, className, elapsedTime);
        }
    }

}

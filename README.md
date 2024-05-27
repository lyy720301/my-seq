    通过 datasource.useHikari 配置选择特定的数据库连接池
    值为true时使用hikari，值为false时使用druid

已完成的

- 实现了不同业务之间id隔离
- 实现了从zk读取token与表名映射关系
- 提供简单的随机路由均衡策略
- 适配多数据源

准备做的

- 预取功能
- 批量id功能
- 通过zk配置负载均衡策略
- 提供其他负载均衡策略

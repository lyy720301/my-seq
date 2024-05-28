**已完成的**

- 实现了不同业务之间id隔离
- 实现了从zk读取token与表名映射关系
- 提供简单的随机路由均衡策略
- 适配多数据源
- 适配多种数据库连接池
- 基于AOP实现故障转移
- 通过zk配置负载均衡策略

**准备做的**

- 预取功能
- 批量id功能
- 提供其他负载均衡策略

**生成原理**

使用replace info语句插入数据时，如果唯一索引列有重复的话会删除重复的数据然后插入新的数据，新的主键id就可以作为生成的序列值。

不同数据库配置不同的起始值，多个数据库生成的id就不会重复

| 库                  | 起始值 | 步长  | 生成的序列       |
|--------------------|-----|-----|-------------|
| a                  | 1   | 3   | 1,4,7,10... |
| b                  | 2   | 3   | 2,5,8,11... |
| c                  | 3   | 3   | 3,6,9,12... |
| 依此类推，库越多可用性越高，性能越好 | ... | ... | ...         |

**程序配置**

*MySQL配置*

my.cnf

`a库所在实例`

```text
auto_increment_offset=1
auto_increment_increment=3
```

`b库所在实例`

```text
auto_increment_offset=2
auto_increment_increment=3
```

`c库所在实例`

```text
auto_increment_offset=3
auto_increment_increment=3
```

*yml文件配置*

```yaml
datasource:
  # 控制使用哪种数据库连接池，值为true时使用hikari，值为false时使用druid
  useHikari: true
```

*zk配置*

| 路径            | 值                                    | 解释                             |
|---------------|--------------------------------------|:-------------------------------|
| /seq/token    | video=video_seq <br/>myshop=shop_seq | key为token，用于鉴权。value为表名，用于取值   |
| /seq/strategy | random                               | 用于实时调整负载均衡策略，目前仅支持random，即随机路由 |


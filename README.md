# 分布式唯一序列系统
**已完成的**

- 实现了不同业务之间id隔离
- 实现了从zk读取token与表名映射关系
- 提供简单的随机路由均衡策略
- 适配多数据源
- 适配多种数据库连接池
- 基于AOP实现故障转移
- 通过zk配置负载均衡策略
- 支持 Http 或 Dubbo 调用

**准备做的**

- 预取功能
- 批量id功能
- 提供其他负载均衡策略

**生成原理**

使用`replace into`语句插入数据时，如果唯一索引列有重复的话会删除重复的数据然后插入新的数据，新的主键id就可以作为生成的序列值。

不同数据库配置不同的起始值，多个数据库生成的id就不会重复

| 库                  | 起始值 | 步长  | 生成的序列       |
|--------------------|-----|-----|-------------|
| a                  | 1   | 3   | 1,4,7,10... |
| b                  | 2   | 3   | 2,5,8,11... |
| c                  | 3   | 3   | 3,6,9,12... |
| 依此类推，库越多可用性越高，性能越好 | ... | ... | ...         |

本项目中使用的是两个库，分别生成偶数号和奇数号

## 运行

### 环境依赖

JDK17，MySQL5.7，Zookeeper3.9.2

### 程序配置

#### MySQL配置

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

#### yml文件配置

##### 数据库连接
```yaml
datasource:
  # 控制使用哪种数据库连接池
  useHikari: true
  hikari:
    one:
      jdbcUrl: jdbc:mysql://192.168.13.134:3306/seq?verifyServerCertificate=false&useSSL=true
      username: root
      password: 123456
      connectionTimeout: 1000
      minIdle: 20
      maxPoolSize: 20

    two:
      jdbcUrl: jdbc:mysql://192.168.13.134:3307/seq?verifyServerCertificate=false&useSSL=true
      username: root
      password: 123456
      connectionTimeout: 1000
      minIdle: 20
      maxPoolSize: 20
```

##### dubbo

###### 消费端

consumer/src/main/resources/application.yml

```yaml
dubbo:
  application:
    name: seq-provider
    logger: slf4j

  registry:
    #    id: zk-registry
    address: zookeeper://192.168.13.134:2181
    # This will enable application-level service discovery only (the recommended service discovery method for Dubbo3).
    # For users upgrading from Dubbo2.x, please set the value to 'all' for smooth migration.
    register-mode: instance
```

###### 生产端

backend/src/main/resources/application.yml

```yaml
dubbo:
  application:
    name: seq-provider
    logger: slf4j
  protocol:
    name: tri
    #    http dubbo共用接口
    port: 11111
  registry:
    #    id: zk-registry
    address: zookeeper://192.168.13.134:2181
    # This will enable application-level service discovery only (the recommended service discovery method for Dubbo3).
    # For users upgrading from Dubbo2.x, please set the value to 'all' for smooth migration.
    register-mode: instance
```

#### ZooKeeper初始化信息

| 路径            | 值                                    | 解释                             |
|---------------|--------------------------------------|:-------------------------------|
| /seq/token    | video=video_seq <br/>myshop=shop_seq | key为token，用于鉴权。value为表名，用于取值   |
| /seq/strategy | random                               | 用于实时调整负载均衡策略，目前仅支持random，即随机路由 |

#### curator配置

用于从ZooKeeper中读取配置信息，例如负载均衡策略、token值。

```yaml
curator:
connectString: 192.168.13.134:2181 # zookeeper 地址
path: seq
retryCount: 3 # 重试次数
elapsedTimeMs: 2000 # 重试间隔时间
sessionTimeoutMs: 120000 # session超时时间
connectionTimeoutMs: 15000 # 连接超时时间
```

### 调用方式

#### Http

`ip:11111/seq?token=${token}`

#### Dubbo

```java
@DubboReference
private SeqService seqService;

        void call(){
        String token="myshop";
        long seq=seqService.seq(token);
        log.info("seq no : {}",seq);
        }
```

详见调用示例 [consumer.Task.java](consumer/src/main/java/cn/lz/seq/demo/consumer/Task.java)
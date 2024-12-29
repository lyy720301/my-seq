#!/bin/bash
echo "ready to init"
# 这么使用 zkCli.sh 无法将zk节点的值设置换行值
/apache-zookeeper-3.9.2-bin/bin/zkCli.sh << EOF
create /seq
create /seq/strategy "random"
quit
EOF
# 这样才可以换行
/apache-zookeeper-3.9.2-bin/bin/zkCli.sh create /seq/token "video=video_seq
myshop=shop_seq"
echo "complete init"
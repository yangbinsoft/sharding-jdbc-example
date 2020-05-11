# sharding-jdbc-example

这是一个亲自手写的有用的项目，里面有数据库sql

分库分表策略运行截图
0bfa78e904931777795a3b11fa92468.png
![运行截图](https://github.com/yangbinsoft/sharding-jdbc-example/blob/master/screenshot/0bfa78e904931777795a3b11fa92468.png)

数据库截图
![数据库截图](https://github.com/yangbinsoft/sharding-jdbc-example/blob/master/screenshot/ae8cbfec26f42d370afd4050c0775ef.png)

我的微信号：y1250211588 小火柴卖的小孩
![数据库截图](https://github.com/yangbinsoft/sharding-jdbc-example/blob/master/screenshot/9a853241d12a3fb7aac80d1276719d2.png)

```yml
spring:
  shardingsphere:
    #数据源配置
    datasource:
      names: ds-master0,ds-master0-slave0,ds-master0-slave1,ds-master1,ds-master1-slave0,ds-master1-slave1
      ds-master0:
        jdbc-url: jdbc:mysql://localhost:3306/master0?characterEncoding=utf8&useSSL=false&allowMultiQueries=true&serverTimezone=Hongkong
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: root
        password: 123456
      ds-master0-slave0:
        jdbc-url: jdbc:mysql://localhost:3316/master0?characterEncoding=utf8&useSSL=false&allowMultiQueries=true&serverTimezone=Hongkong
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: root
        password: 123456
      ds-master0-slave1:
        jdbc-url: jdbc:mysql://localhost:3326/master0?characterEncoding=utf8&useSSL=false&allowMultiQueries=true&serverTimezone=Hongkong
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: root
        password: 123456
      ds-master1:
        jdbc-url: jdbc:mysql://localhost:3306/master1?characterEncoding=utf8&useSSL=false&allowMultiQueries=true&serverTimezone=Hongkong
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: root
        password: 123456
      ds-master1-slave0:
        jdbc-url: jdbc:mysql://localhost:3316/master1?characterEncoding=utf8&useSSL=false&allowMultiQueries=true&serverTimezone=Hongkong
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: root
        password: 123456
      ds-master1-slave1:
        jdbc-url: jdbc:mysql://localhost:3326/master1?characterEncoding=utf8&useSSL=false&allowMultiQueries=true&serverTimezone=Hongkong
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: root
        password: 123456

    sharding:
      #主从配置
      master-slave-rules:
        ds_0:
          slave-data-source-names: ds-master0-slave0, ds-master0-slave1
          master-data-source-name: ds-master0
          load-balance-algorithm-type: round_robin #从库负载均衡算法类型，可选值：ROUND_ROBIN，RANDOM。若`loadBalanceAlgorithmClassName`存在则忽略该配置
        ds_1:
          slave-data-source-names: ds-master1-slave0, ds-master1-slave1
          master-data-source-name: ds-master1
          load-balance-algorithm-type: round_robin
      #默认分库策略
      default-database-strategy:
        #标准分库
        standard:
          sharding-column: order_id
          #range-algorithm-class-name: com.shardingjdbc.algorithm.UserIdDatabaseAlgorithm
          precise-algorithm-class-name: com.shardingjdbc.algorithm.UserIdDatabaseAlgorithm
#        #内置
#        inline:
#          sharding-column: user_id
#          algorithm-expression: ds_$->{user_id % 2}
      #绑定表
      binding-tables: t_order,t_attendance
      #广播表
      broadcast-tables: t_config
      #分表策略
      tables:
        t_order:
          #真实表分布
          actual-data-nodes: ds_$->{0..1}.t_order$->{0..1}
          table-strategy:
            #内置分表策略
            inline:
              sharding-column: order_id
              algorithm-expression: t_order$->{order_id % 2}
          key-generator:
            column: order_id
            type: SNOWFLAKE
            #属性配置, 注意：使用SNOWFLAKE算法，需要配置worker.id与max.tolerate.time.difference.milliseconds属性。若使用此算法生成值作分片值，建议配置max.vibration.offset属性
            props:
              worker:
                id: 123

        t_attendance:
          #真实表分布 按月分表
          actual-data-nodes: ds_$->{0..1}.t_attendance$->{2019..2020}0$->{1..9},ds_$->{0..1}.t_attendance$->{2019..2020}$->{10..12},
          table-strategy:
            #标准分表策略
            standard:
              sharding-column: access_date
              precise-algorithm-class-name: com.shardingjdbc.algorithm.AttendanceShardingTableAlgorithm
          key-generator:
            column: attendance_id
            type: SNOWFLAKE
            props:
              worker:
                id: 123
              max:
                tolerate:
                  time:
                    difference:
                      milliseconds: 1000

      #数据脱敏
      encryptRule:
        encryptors:
          aes_encryptor:
            type: aes
            props:
              aes.key.value: 123456abc
        tables:
          t_user:
            columns:
              pwd:
                cipherColumn: pwd
                encryptor: aes_encryptor

    props:
      sql.show: true  #打印sql
      executor.size: 2 #工作线程数量，默认值: CPU核数
```
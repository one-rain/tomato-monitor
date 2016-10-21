# tomato-monitor

> 基于RESTEasy的监控报警项目。打包后可以直接部署到Tomcat，或其他容器中。


---

## 使用说明

### URL请求
    
用POST方式请求http://127.0.0.1/ssports-alarm/alarm/send?tag=&valid=

tag：报警服务标识，在后台配置。必传参数
valid：报警频率控制开关，如果想控制发送频率（频率在后台控制），可以设置valid=1，默认为不控制。非必传参数
content: 报警内容


### Java API

引用maven或引入jar包。

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.3.3</version>
</dependency>
```

代码调用方式：
```java
AlarmAPI alarmAPI = new EmailAlarmAPI();
alarmAPI.send("job-day", "这个一个客户端测试");
//alarmAPI.send("job-day", "这个一个客户端测试", 1);
```

---------

## Maven modules

* [tomato-monitor-commons](#tomato-monitor-commons)
* [tomato-monitor-alarm](#tomato-monitor-alarm)
* [tomato-monitor-client](#tomato-monitor-client)
    
  
---

## tomato-monitor-commons

该module为常用工具包。

---

## tomato-monitor-alarm

该module提供报警服务，将接收到报警信息进行存储（存储方式可以采用本地内存、数据库等，目前暂时只支持内存方式），并根据报警服务标识`serviceTag`对应的控制开关和报警频率参数，返回给使用方不同的结果。

服务端接收到报警信息后，并不一定会立即触发报警机制。这是由报警发送进程的轮询频率和`serviceTag`的报警频率参数共同决定的。

报警信息的删除策略由各存储方式实现。本地内存方式会根据`serviceTag`控制开关、报警频率、是否已发送等参数判断。

---

## tomato-monitor-client

该module为服务使用方接口，根据业务情况，需要报警时，直接加上代码调用即可。


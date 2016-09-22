# tomato-monitor

> 基于RESTEasy的监控报警项目。打包后可以直接部署到Tomcat，或其他容器中。


---

## 使用说明

### URL请求
    
用POST方式请求http://101.200.160.40/ssports-alarm/alarm/send?tag=&valid=

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
AlarmAPI alarmAPI = new EmailAlarmAPI()
```

---------

## Maven modules

  * [tomato-monitor-commons](#tomato-monitor-commons)
  * [tomato-monitor-alarm](#tomato-monitor-alarm)
  * [tomato-monitor-client](#tomato-monitor-client)
    
  
---

## tomato-monitor-commons


---

## tomato-monitor-alarm


---

## tomato-monitor-client


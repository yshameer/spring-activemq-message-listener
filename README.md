# Spring Activemq Message Listener

Listener listens to a queue and processes messages from the queue. 

It uses camel activemq libraries and provides option to control retry attempts on failure
and option to send to custom dead letter queue for reprocessing manually.

## Getting Started

Before running the project please update the application.properties under src/main/resources with your queue connection details

#Queue details
User name and password properties are optional default is set in spring-camel-activemq-starter
```
camel.activemq.userName=
camel.activemq.password=
camel.activemq.brokerUrl=failover://(tcp://queueconnectionurl:61616,tcp://backupurl:61616)?randomize=true

```

## Software required to build and run
```
Java: JDK 1.8+
Maven: Apache Maven 3.6.3+
```

### How to run

To run it locally you can use either maven or your favorite IDE.

Maven:
```
mvn spring-boot:run
```

To run as standalone:

1) build an artifact
```
mvn clean install
```

## Dependency
This service uses camel active mq starter https://github.com/yshameer/spring-camel-activemq-starter as a dependency. 
If you want to make any specific changes to starter you may download and update the reference.

```
<dependency>
    <groupId>com.github.yshameer</groupId>
    <artifactId>spring-camel-activemq-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
 ```


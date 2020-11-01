package com.wujiuye.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.data.r2dbc.connectionfactory.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;
//
//public class RoutingConnectionFactory extends AbstractRoutingConnectionFactory {
//
//    private final static String MASTER_DB = "master";
//    private final static String SLAVE_DB = "slave";
//
//    @Override
//    protected Mono<Object> determineCurrentLookupKey() {
//        return Mono.just(MASTER_DB);
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        Map<String, ConnectionFactory> factoryMap = new HashMap<>();
//        factoryMap.put(MASTER_DB, createFactory());
//        factoryMap.put(SLAVE_DB, createFactory());
//        setTargetConnectionFactories(factoryMap);
//        super.afterPropertiesSet();
//    }
//
//    private static ConnectionPool createFactory() {
//        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
//                .option(DRIVER, "mysql")
//                .option(HOST, "127.0.0.1")
//                .option(USER, "root")
//                .option(PORT, 3306)
//                .option(PASSWORD, "")
//                .option(DATABASE, "r2dbc_stu")
//                .option(CONNECT_TIMEOUT, Duration.ofSeconds(3))
//                .build();
//        ConnectionFactory connectionFactory = ConnectionFactories.get(options);
//
//        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
//                .maxIdleTime(Duration.ofMillis(1000))
//                .maxSize(20)
//                .build();
//        return new ConnectionPool(configuration);
//    }
//
//}

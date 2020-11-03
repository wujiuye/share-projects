package com.wujiuye.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.data.r2dbc.connectionfactory.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public class RoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    public final static String DB = "DB";
    public final static String MASTER_DB = "master";
    public final static String SLAVE_DB = "slave";

    public RoutingConnectionFactory() {
        Map<String, ConnectionFactory> map = new HashMap<>();
        map.put(MASTER_DB, createConnectionFactory("127.0.0.1", "root", "", "r2dbc_stu"));
        map.put(SLAVE_DB, createConnectionFactory("rm-wz9w2pj1crm4e2k80rw.mysql.rds.aliyuncs.com",
                "ycmanger_2b", "cdYqa!#sadi&^cdUYwe98==", "message"));
        setTargetConnectionFactories(map);
        setDefaultTargetConnectionFactory(map.get(MASTER_DB));
    }

    public static <T> Mono<T> warpDataSource(Mono<T> mono, String dataSource) {
        return Mono.subscriberContext().flatMap(context -> mono)
                .subscriberContext(context -> context.put(RoutingConnectionFactory.DB, dataSource));
    }

    public static <T> Flux<T> warpDataSource(Flux<T> flux, String dataSource) {
        return Mono.subscriberContext().flatMapMany(context -> flux)
                .subscriberContext(context -> context.put(RoutingConnectionFactory.DB, dataSource));
    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.subscriberContext().flatMap(context -> {
            if (context.hasKey(DB)) {
                return Mono.just(context.get(DB));
            }
            return Mono.just(MASTER_DB);
        });
    }

    public static ConnectionFactory createConnectionFactory(String host, String user, String pwd, String db) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, host)
                .option(USER, user)
                .option(PORT, 3306)
                .option(PASSWORD, pwd)
                .option(DATABASE, db)
                .option(CONNECT_TIMEOUT, Duration.ofSeconds(3))
                .build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(options);
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(5)
                .build();
        return new ConnectionPool(configuration);
    }

}

package com.wujiuye.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;

import io.r2dbc.spi.*;
import org.reactivestreams.Publisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Update;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.r2dbc.core.DatabaseClient;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public class R2dbcStuMain {

    public static void main(String[] args) throws InterruptedException {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, "127.0.0.1")
                .option(USER, "root")
                .option(PORT, 3306)
                .option(PASSWORD, "")
                .option(DATABASE, "r2dbc_stu")
                .option(CONNECT_TIMEOUT, Duration.ofSeconds(3))
                .build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(options);

        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(20)
                .build();
        // 将连接池替换为连接工厂即可
        connectionFactory = new ConnectionPool(configuration);

//        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();
//        Mono.from(connectionPublisher)
//                .flatMapMany(conn -> conn.createStatement(
//                        "insert into person (id,name,age) values ('22222','wujiuye',25)")
//                        .execute())
//                .flatMap(Result::getRowsUpdated)
//                .switchIfEmpty(Mono.just(0))
//                .onErrorResume(throwable -> {
//                    throwable.printStackTrace();
//                    return Mono.empty();
//                })
//                .subscribe(System.out::println);

        // spring-data-r2db提供的
        DatabaseClient client = DatabaseClient.create(connectionFactory);

        ReactiveTransactionManager tm = new R2dbcTransactionManager(connectionFactory);
        TransactionalOperator operator = TransactionalOperator.create(tm);

        Mono<Void> atomicOperation = client.execute("INSERT INTO person (id, name, age) VALUES(:id, :name, :age)")
                .bind("id", "joe")
                .bind("name", "Joe")
                .bind("age", 34)
                .fetch().rowsUpdated()
                .then(client.execute("INSERT INTO person (id, name) VALUES(:id, :name)")
                        .bind("id", "joe")
                        .bind("name", "Joe")
                        .fetch().rowsUpdated())
                .then();

        operator.transactional(atomicOperation).subscribe();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        ((ConnectionPool) connectionFactory).disposeLater().block();
    }

    @Table("person")
    public static class Person {
        @Id
        private String id;
        private String name;
        private int age;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}

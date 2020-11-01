package com.wujiuye.r2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableR2dbcRepositories
public class R2dbcApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(R2dbcApplication.class);
        R2dbcStuMain.Person person = new R2dbcStuMain.Person();
        PersonService personService = context.getBean(PersonService.class);
        person.setId("12347");
        person.setName("wjy");
        person.setAge(25);
        personService.addPerson(person, person)
                .doOnError(Throwable::printStackTrace)
                .subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

}

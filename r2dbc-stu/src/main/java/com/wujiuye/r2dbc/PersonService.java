package com.wujiuye.r2dbc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class PersonService {

    @Resource
    private PersonRepository personRepository;

    @Transactional(rollbackFor = Throwable.class)
    public Mono<Integer> addPerson(R2dbcStuMain.Person... persons) {
        Mono<Integer> txOp = null;
        for (R2dbcStuMain.Person person : persons) {
            if (txOp == null) {
                txOp = personRepository.insertPerson(person.getId(), person.getName(), person.getAge());
            } else {
                txOp = txOp.then(personRepository.insertPerson(person.getId(), person.getName(), person.getAge()));
            }
        }
        return txOp;
    }

}

package com.wujiuye.es;

import com.wujiuye.es.service.StuElasticsearchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author wjy
 */
@SpringBootApplication
public class ElasticsearchApplication {

    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = SpringApplication.run(ElasticsearchApplication.class);
            StuElasticsearchService stuElasticsearchService = applicationContext.getBean(StuElasticsearchService.class);
            stuElasticsearchService.testBatchInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

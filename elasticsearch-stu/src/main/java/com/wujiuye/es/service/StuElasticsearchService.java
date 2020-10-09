package com.wujiuye.es.service;

import com.wujiuye.es.config.ElasticSearchService;
import com.wujiuye.es.config.EsEntity;
import com.wujiuye.es.config.EsIndexMappings;
import com.wujiuye.es.model.Product;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StuElasticsearchService implements InitializingBean {

    @Resource
    private ElasticSearchService elasticService;
    private final static String INDEX = "text_index";

    @Override
    public void afterPropertiesSet() throws Exception {
        if (elasticService.existIndex(INDEX)) {
            elasticService.deleteIndex(INDEX);
        }
        elasticService.createIndex(INDEX, EsIndexMappings.byType(false, Product.class), 1, 1);
    }

    public void testBatchInsert() {
        List<Product> products = new ArrayList<>();
        String dateStr = "2020-03-04 15:36:00";
        for (int i = 1000000; i < 1000100; i++) {
            Product product = new Product();
            product.setLeId(i);
            product.setFistLetId(i % 20);
            product.setFistLetName("IN美妆_" + i);
            product.setType((i % 3) + 1);
            product.setLabelId((i % 2) + 1);
            product.setBrandId(i * 2);
            product.setBrandName("匡威_" + i % 5);
            product.setLetOrder(i - 1000000);
            product.setDate(dateStr);
            products.add(product);
        }
        try {
            elasticService.insertBatch(INDEX,
                    products.stream().map(EsEntity::objToElasticEntity).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

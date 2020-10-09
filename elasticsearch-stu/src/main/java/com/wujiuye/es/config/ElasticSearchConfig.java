package com.wujiuye.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ElasticSearchConfig {

    @Resource
    private ElasticSearchPropertys elasticSearchPropertys;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(elasticSearchPropertys.getHost(),
                        elasticSearchPropertys.getPort(), elasticSearchPropertys.getScheme()));
        return new RestHighLevelClient(restClientBuilder);
    }

}

package com.wujiuye.es.config;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 封装ES通用API
 *
 * @author wujiuye
 * @date 2020/03/04
 */
@Component
@ConditionalOnBean(RestHighLevelClient.class)
public class ElasticSearchService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    /**
     * 判断某个index是否存在
     *
     * @param index index名
     */
    public boolean existIndex(String index) throws Exception {
        return restHighLevelClient.indices()
                .exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
    }

    /**
     * 创建索引（仅测试使用）
     *
     * @param index    索引名称
     * @param mappings 索引描述
     * @param shards   分片数
     * @param replicas 副本数
     */
    public void createIndex(String index, EsIndexMappings mappings, int shards, int replicas) throws Exception {
        if (this.existIndex(index)) {
            return;
        }
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(Settings.builder()
                // 分片数
                .put("index.number_of_shards", shards)
                // 副本数
                .put("index.number_of_replicas", replicas));
        request.mapping(JSON.toJSONString(mappings), XContentType.JSON);
        CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        if (!res.isAcknowledged()) {
            throw new RuntimeException("所以创建失败！");
        }
    }

    /**
     * 插入或更新单条记录
     *
     * @param index  index
     * @param entity 对象
     */
    public void insertOrUpdate(String index, EsEntity entity) throws Exception {
        IndexRequest request = new IndexRequest(index);
        request.id(entity.getId());
        // data必须是map类型，或者直接是json字符串
        request.source(entity.getData(), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        if (response.status() != RestStatus.OK) {
            throw new RuntimeException(response.toString());
        }
    }

    /**
     * 批量插入数据
     *
     * @param index index
     * @param list  带插入列表
     */
    public void insertBatch(String index, List<EsEntity> list) throws Exception {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(index)
                .id(item.getId())
                .source(item.getData(), XContentType.JSON)));
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        if (response.hasFailures()) {
            throw new RuntimeException(response.buildFailureMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param index  index
     * @param idList 待删除列表
     */
    public void deleteBatch(String index, Collection<String> idList) throws Exception {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(index, item)));
        BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        if (bulkResponse.hasFailures()) {
            throw new RuntimeException(bulkResponse.buildFailureMessage());
        }
    }

    /**
     * 搜索
     *
     * @param index   index
     * @param builder 查询参数
     * @param c       结果对象类型
     */
    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) throws Exception {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<T> res = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            res.add(JSON.parseObject(hit.getSourceAsString(), c));
        }
        return res;
    }

    /**
     * 删除index
     *
     * @param index
     */
    public void deleteIndex(String index) throws Exception {
        if (!this.existIndex(index)) {
            return;
        }
        restHighLevelClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
    }

    /**
     * 条件删除
     *
     * @param index
     * @param builder
     */
    public void deleteByQuery(String index, QueryBuilder builder) throws Exception {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(builder);
        restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
    }

}

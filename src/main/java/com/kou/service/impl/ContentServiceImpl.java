package com.kou.service.impl;

import com.alibaba.fastjson.JSON;
import com.kou.pojo.Content;
import com.kou.service.ContentService;
import com.kou.util.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JIAJUN KOU
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean parseContent(String keywords) throws IOException {
        //解析出来得内容集合
        List<Content> contents = HtmlParseUtil.parseJD(keywords);
        //把解析出来的内容集合加入到es索引中。
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (int i = 0; i <contents.size() ; i++) {
            bulkRequest.add(new IndexRequest("jd_goods").id(""+(i+1))
            .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    @Override
    public List<Map<String, Object>> searchPage(String keyword, Integer pageNo, Integer pageSize) throws IOException {

        if(pageNo<=1){
            pageNo=1;
        }
        //条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        //通过SearchSourceBuilder构建器 构建
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);
        //精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        //只需要高亮一个字段
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        //执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //解析结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            //首先获取高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            //原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //解析高亮的字段;
            if(title!=null){
                Text[] fragments = title.fragments();
                String new_title="";
                for (Text fragment : fragments) {
                    new_title+=fragment;

                }
                //将原来的字段替换成高亮的字段
                sourceAsMap.put("title",new_title);
            }
            list.add(sourceAsMap);
        }
        return list;
    }

}

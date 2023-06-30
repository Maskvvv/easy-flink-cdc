package com.esflink.demo.controller;

import com.alibaba.fastjson.JSON;
import com.esflink.demo.OureaHomeSuggestionModel;
import com.esflink.demo.mapper.es.CompanyDocumentMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhouhongyin
 * @since 2023/6/10 11:36
 */
@RestController
@RequestMapping("ourea_home_v2")
public class OureaHomeSuggestiongV2Controller {

    @Autowired(required = false)
    private CompanyDocumentMapper companyDocumentMapper;

    @GetMapping("suggest")
    private List<OureaHomeSuggestionModel> getCompanies(String key) throws IOException {
        SearchRequest searchRequest = new SearchRequest("ourea-home-suggestion");

        List<OureaHomeSuggestionModel> result = new ArrayList<>();

        String[] highlightField = {"name.ik_pinyin", "name.completion", "name.chinese"};

        query(key, searchRequest);
        highlight(searchRequest, highlightField);

        SearchResponse search = companyDocumentMapper.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            OureaHomeSuggestionModel homeSuggestionModel = JSON.parseObject(sourceAsString, OureaHomeSuggestionModel.class);
            result.add(homeSuggestionModel);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            for (String field : highlightField) {
                HighlightField highlightField1 = highlightFields.get(field);
                if (highlightField1 == null) continue;
                Text[] fragments = highlightField1.getFragments();
                homeSuggestionModel.setHighlight(fragments[0].toString());
                break;
            }

        }
        return result;
    }

    private void highlight(SearchRequest searchRequest, String[] highlightField) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String field : highlightField) {
            highlightBuilder.field(field);
        }

        searchRequest.source().highlighter(highlightBuilder);
    }

    private void query(String key, SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> should = boolQueryBuilder.should();
        should.add(QueryBuilders
                .matchQuery("name.completion", key)
                .analyzer("keyword_pinyin_analyzer")
                .fuzziness(0).boost(2));


        should.add(QueryBuilders
                .matchQuery("name.ik_pinyin", key).analyzer("keyword_pinyin_analyzer"));

        should.add(QueryBuilders
                .termQuery("name.keyword", key).boost(10));


        if (isContainChinese(key)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name.chinese", key).operator(Operator.AND));
        }

        FieldValueFactorFunctionBuilder fieldValueFactorFunctionBuilder = new FieldValueFactorFunctionBuilder("sequence");
        fieldValueFactorFunctionBuilder.missing(0);
        fieldValueFactorFunctionBuilder.factor(1);


        FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(boolQueryBuilder, fieldValueFactorFunctionBuilder);
        searchRequest.source().query(functionScoreQueryBuilder);
    }

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}

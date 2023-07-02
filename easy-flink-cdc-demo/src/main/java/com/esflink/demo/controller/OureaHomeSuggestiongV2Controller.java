package com.esflink.demo.controller;

import com.alibaba.fastjson.JSON;
import com.esflink.demo.OureaHomeSuggestionModel;
import com.esflink.demo.mapper.es.CompanyDocumentMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

        String[] highlightFieldName = {"name.prefix", "name.standard", "name.full_pinyin", "name.first_letter.prefix", "name.first_letter"};

        query(key, searchRequest);
        highlight(searchRequest, highlightFieldName);

        SearchResponse search = companyDocumentMapper.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            OureaHomeSuggestionModel homeSuggestionModel = JSON.parseObject(sourceAsString, OureaHomeSuggestionModel.class);
            result.add(homeSuggestionModel);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            for (String hfName : highlightFieldName) {
                HighlightField hf = highlightFields.get(hfName);
                if (hf == null) continue;

                Text[] fragments = hf.getFragments();
                homeSuggestionModel.setHighlight(fragments[0].toString());
                break;
            }
        }
        return result;
    }

    private void highlight(SearchRequest searchRequest, String[] highlightField) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String field : highlightField) {
            highlightBuilder.field(field).highlighterType("plain");
        }

        searchRequest.source().highlighter(highlightBuilder);
    }

    private void query(String key, SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().minimumShouldMatch(1);
        boolQueryBuilder.filter(QueryBuilders.termQuery("onlined", 1));
        List<QueryBuilder> should = boolQueryBuilder.should();

        // 中文前缀
        should.add(QueryBuilders
                .termQuery("name.prefix", key)
                .boost(10));

        // 中文中缀
        should.add(QueryBuilders
                .matchPhraseQuery("name.standard", key)
                .boost(5f));


        // 拼音全拼前缀
        BoolQueryBuilder fullPinyinPrefixBoolQueryBuilder = new BoolQueryBuilder();
        should.add(fullPinyinPrefixBoolQueryBuilder);
        fullPinyinPrefixBoolQueryBuilder.minimumShouldMatch(1);
        fullPinyinPrefixBoolQueryBuilder.boost(3);

        fullPinyinPrefixBoolQueryBuilder.filter(
                QueryBuilders.matchPhrasePrefixQuery("name.full_pinyin.prefix", key)
                        .analyzer("full_pinyin_prefix_search_analyzer")
                        .maxExpansions(100));

        fullPinyinPrefixBoolQueryBuilder.should().add(
                QueryBuilders.matchPhrasePrefixQuery("name.full_pinyin", key)
                        .analyzer("full_pinyin_search_analyzer"));

        // 拼音全拼中缀
        should.add(QueryBuilders
                .matchPhrasePrefixQuery("name.full_pinyin", key)
                .analyzer("full_pinyin_search_analyzer")
                .boost(1.5f));

        // 拼音首字母前缀
        should.add(QueryBuilders
                .matchQuery("name.first_letter.prefix", key)
                .analyzer("first_letter_prefix_search_analyzer")
                .maxExpansions(100)
                .boost(1));

        // 拼音首字母中缀
        should.add(QueryBuilders
                .matchPhraseQuery("name.first_letter", key)
                .analyzer("first_letter_search_analyzer")
                .boost(0.8f));

        //FieldValueFactorFunctionBuilder fieldValueFactorFunctionBuilder = new FieldValueFactorFunctionBuilder("sequence");
        //fieldValueFactorFunctionBuilder.missing(0);
        //fieldValueFactorFunctionBuilder.factor(1);
        //FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(boolQueryBuilder, fieldValueFactorFunctionBuilder);


        searchRequest.source().query(boolQueryBuilder);
    }

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}

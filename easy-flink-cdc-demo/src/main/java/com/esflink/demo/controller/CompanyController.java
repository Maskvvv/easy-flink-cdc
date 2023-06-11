package com.esflink.demo.controller;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.alibaba.fastjson.JSONObject;
import com.esflink.demo.document.CompanyDocument;
import com.esflink.demo.mapper.es.CompanyDocumentMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhouhongyin
 * @since 2023/6/10 11:36
 */
@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired(required = false)
    private CompanyDocumentMapper companyDocumentMapper;

    @Autowired
    private RestHighLevelClient client;

    @GetMapping("suggest")
    private String getCompanies(String key) throws IOException {
        SearchRequest searchRequest = new SearchRequest("company");

        List<String> rest = new ArrayList<>();

        searchRequest.source().suggest(new SuggestBuilder()
                .addSuggestion("suggest",
                        SuggestBuilders.completionSuggestion("keyword")
                                .prefix(key).analyzer("ik_pinyin_analyzer").skipDuplicates(true).size(10)));

        SearchResponse search = companyDocumentMapper.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = search.getSuggest();
        List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries = suggest.getSuggestion("suggest").getEntries();

        for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry: entries) {
            for (Suggest.Suggestion.Entry.Option option : entry.getOptions()) {
                String keyword = option.getText().string();
                rest.add(keyword);
            }
        }
        return String.join(",", rest);
    }

}

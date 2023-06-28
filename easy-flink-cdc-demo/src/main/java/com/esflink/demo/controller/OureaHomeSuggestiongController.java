package com.esflink.demo.controller;

import com.alibaba.fastjson.JSON;
import com.esflink.demo.document.OureaHomeSuggestionDoc;
import com.esflink.demo.mapper.es.CompanyDocumentMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouhongyin
 * @since 2023/6/10 11:36
 */
@RestController
@RequestMapping("ourea_home")
public class OureaHomeSuggestiongController {

    @Autowired(required = false)
    private CompanyDocumentMapper companyDocumentMapper;

    @GetMapping("suggest")
    private List<OureaHomeSuggestionDoc> getCompanies(String key) throws IOException {
        SearchRequest searchRequest = new SearchRequest("ourea-home-suggestion");

        List<OureaHomeSuggestionDoc> rest = new ArrayList<>();

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("ourea-home-suggest",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix(key)
                        .skipDuplicates(true).size(10));

        searchRequest.source().suggest(suggestBuilder);

        SearchResponse search = companyDocumentMapper.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = search.getSuggest();
        List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries = suggest.getSuggestion("ourea-home-suggest").getEntries();

        for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry : entries) {
            for (Suggest.Suggestion.Entry.Option option : entry.getOptions()) {
                CompletionSuggestion.Entry.Option completion = ((CompletionSuggestion.Entry.Option) option);
                String sourceAsString = completion.getHit().getSourceAsString();

                rest.add(JSON.parseObject(sourceAsString, OureaHomeSuggestionDoc.class));
            }
        }

        return rest;
    }

}

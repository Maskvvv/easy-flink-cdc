package com.esflink.demo.controller;

import com.esflink.demo.mapper.es.CompanyDocumentMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<front color='red'>");
        highlightBuilder.postTags("</front>");
        highlightBuilder.field("keyword");
        searchRequest.source().highlighter(highlightBuilder);

        SearchResponse search = companyDocumentMapper.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = search.getSuggest();
        List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries = suggest.getSuggestion("suggest").getEntries();

        for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry : entries) {
            for (Suggest.Suggestion.Entry.Option option : entry.getOptions()) {
                String keyword = option.getText().string();
                rest.add(keyword);
            }
        }


        SearchHits hits = search.getHits();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> name = hit.getHighlightFields();
            HighlightField highlightField = name.get(name);
            System.out.println(highlightField);
        }
        return String.join(",", rest);
    }

}

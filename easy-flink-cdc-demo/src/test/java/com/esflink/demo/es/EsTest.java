//package com.esflink.demo.es;
//
//import com.alibaba.fastjson.JSON;
//import com.esflink.demo.document.CompanyDocument;
//import org.apache.http.HttpHost;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.xcontent.XContentType;
//
//import java.io.IOException;
//
///**
// * @author zhouhongyin
// * @since 2023/6/10 15:12
// */
//public class EsTest {
//
//    public static void main(String[] args) throws IOException {
//
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(new HttpHost("localhost", 9200, "http"))
//        );
//
//        IndexRequest request = new IndexRequest("company");
//        request.index("company").id("1001");
//
//        CompanyDocument companyDocument = new CompanyDocument();
//        companyDocument.setId("111");
//        companyDocument.setName("泰盈科技集团有限公司");
//        companyDocument.setNickName("泰盈科技");
//        companyDocument.setKeyword("泰盈科技集团有限公司");
//        String json = JSON.toJSONString(companyDocument);
//
//        request.source(json, XContentType.JSON);
//
//        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//
//        System.out.println(response);
//
//        client.close();
//
//    }
//
//}

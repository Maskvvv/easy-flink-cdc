package com.esflink.demo.sink.es;

import com.alibaba.fastjson.JSON;
import com.esflink.demo.document.CompanyDocument;
import com.esflink.demo.mapper.es.CompanyDocumentMapper;
import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p> 同步信息到 es </p>
 *
 * @author zhouhongyin
 * @since 2023/6/9 17:11
 */
@FlinkSink(value = "ourea", database = "ourea", table = "ourea.company")
public class EsSink implements FlinkJobSink {

    @Autowired(required = false)
    private CompanyDocumentMapper companyDocumentMapper;

    @Override
    public void update(DataChangeInfo value, Context context) throws Exception {
        String afterData = value.getAfterData();
        CompanyDocument companyDocument = JSON.parseObject(afterData, CompanyDocument.class);
        companyDocument.setKeyword(companyDocument.getName());
        companyDocumentMapper.insert(companyDocument);
    }

    @Override
    public void insert(DataChangeInfo value, Context context) throws Exception {
        String afterData = value.getAfterData();
        CompanyDocument companyDocument = JSON.parseObject(afterData, CompanyDocument.class);
        companyDocument.setKeyword(companyDocument.getName());
        companyDocumentMapper.insert(companyDocument);
    }
}

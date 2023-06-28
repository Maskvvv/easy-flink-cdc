package com.esflink.demo.sink.oureahome;

import com.alibaba.fastjson.JSON;
import com.esflink.demo.document.OureaHomeSuggestionDoc;
import com.esflink.demo.mapper.es.OureaHomeSuggestionDocMapper;
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
@FlinkSink(value = "ourea", database = "ourea", table = "ourea.occupation")
public class OccupationSink implements FlinkJobSink {

    @Autowired(required = false)
    private OureaHomeSuggestionDocMapper homeSuggestionDocMapper;

    @Override
    public void update(DataChangeInfo value, Context context) throws Exception {
        String afterData = value.getAfterData();
        OureaHomeSuggestionDoc homeSuggestionDoc = JSON.parseObject(afterData, OureaHomeSuggestionDoc.class);
        homeSuggestionDoc.setType(2);
        homeSuggestionDocMapper.insert(homeSuggestionDoc);
    }

    @Override
    public void insert(DataChangeInfo value, Context context) throws Exception {
        String afterData = value.getAfterData();
        OureaHomeSuggestionDoc homeSuggestionDoc = JSON.parseObject(afterData, OureaHomeSuggestionDoc.class);
        homeSuggestionDoc.setType(2);
        homeSuggestionDocMapper.insert(homeSuggestionDoc);
    }

    @Override
    public void delete(DataChangeInfo value, Context context) throws Exception {
        OureaHomeSuggestionDoc homeSuggestionDoc = JSON.parseObject(value.getBeforeData(), OureaHomeSuggestionDoc.class);
        homeSuggestionDocMapper.deleteById(homeSuggestionDoc.getId());
    }
}

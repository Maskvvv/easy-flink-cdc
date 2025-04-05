package com.esflink.demo.config;

import com.esflink.demo.pojo.CreditAPI_TotalOrder;
import com.esflink.demo.pojo.OdsRegion;
import com.esflink.demo.pojo.CreditAPI_Region;
import com.esflink.demo.pojo.OdsTotalOrder;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述
 *
 * @author xlh
 * @date 2024/12/27
 * @desc
 */
@Configuration
public class BeanMapperConfig {

    /**
     * 自定义工厂
     * @return
     */
    @Bean
    public MapperFactory myMapperFactory(){
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(CreditAPI_Region.class, OdsRegion.class)
                .field("regionid", "regionId")
                .field("parentid", "parentId")
                .field("regionname", "regionName")
                .byDefault().register();

        mapperFactory.classMap(CreditAPI_TotalOrder.class, OdsTotalOrder.class)
                .field("merchId", "merch_id")
                .field("thirdcode", "thirdCode")
                .field("submerorderno", "subMerOrderNo")

                .field("merno", "merNo")
                .field("storeid", "storeId")

                .field("payprice", "payPrice")
                .field("payaway", "payAway")

                .field("paytime", "payTime")
                .field("serialno", "serialNo")

                .field("operationid", "operationId")
                .field("ytprofit", "ytProfit")

                .field("thirdparyProfit", "thirdpary_profit")
                .field("ytaccount", "ytAccount")

                .field("channelid", "channelId")
                .field("provinceid", "provinceId")

                .field("cityid", "cityId")
                .field("districtid", "districtId")

                .field("isrefund", "isRefund")
                .field("issettle", "isSettle")

                .field("settletime", "settleTime")
                .field("timeoutexpress", "timeoutExpress")

                .field("notifyurl", "notifyUrl")
                .field("buyerid", "buyerId")

                .field("buyeraccount", "buyerAccount")
                .field("requestno", "requestNo")

                .field("salesno", "salesNo")
                .field("freezetype", "freezeType")

                .field("updatetime", "updateTime")
                .field("createtime", "createTime")

                .field("zyenablechannels", "zyEnableChannels")
                .field("zysettle", "zySettle")

                .field("certname", "certName")
                .field("certno", "certNo")

                .field("refundtype", "refundType")
                .field("isonline", "isOnline")

                .field("settlemessage", "settleMessage")
                .field("isytsettle", "isYtSettle")

                .field("fqflatrate", "fqFlatRate")
                .field("renewaltime", "renewalTime")

                .field("iscall", "isCall")
                .field("configprincipal", "configPrincipal")

                .field("mobileno", "mobileNo")
                .field("productid", "productId")

                .field("remarkrisk", "remarkRisk")
                .field("plansucc", "planSucc")
                .byDefault().register();
        return mapperFactory;
    }
}

package com.esflink.demo.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述
 *
 * @author xlh
 * @date 2025/1/7
 * @desc
 */
@Data
public class OdsTotalOrder {
    /// <summary>
    /// 平台订单号
    /// </summary>
    private String code ;
    /// <summary>
    /// 下游平台标识
    /// </summary>
    private int merch_id ;
    /// <summary>
    /// 下游提交单号
    /// </summary>
    private String thirdCode ;
    /// <summary>
    /// 商户订单号
    /// </summary>
    private String subMerOrderNo ;
    /// <summary>
    /// 商户号（下游定义）
    /// </summary>
    private String merNo ;
    /// <summary>
    /// 门店id（下游定义）
    /// </summary>
    private String storeId ;
    /// <summary>
    /// 订单主题
    /// </summary>
    private String subject ;
    /// <summary>
    /// 商品详情
    /// </summary>
    private String goods ;
    /// <summary>
    /// 订单金额（不包含手续费）
    /// </summary>
    private BigDecimal price ;
    /// <summary>
    /// 分期期数
    /// </summary>
    private int pers ;
    /// <summary>
    /// 承担手续费方式0：用户承担；100：商家承担
    /// </summary>
    private int percents ;
    /// <summary>
    /// 分期总金额（包含手续费）
    /// </summary>
    private BigDecimal payPrice ;
    /// <summary>
    /// 分期方式
    /// </summary>
    private String payAway ;
    /// <summary>
    /// 支付时间
    /// </summary>
    private Date payTime ;
    /// <summary>
    /// 支付单号（如：支付宝反回交易流水号）
    /// </summary>
    private String serialNo ;
    /// <summary>
    /// 余额宝质押返回
    /// </summary>
    private String operationId ;
    /// <summary>
    /// 订单状态
    /// </summary>
    private String status ;
    /// <summary>
    /// 银通利润
    /// </summary>
    private BigDecimal ytProfit ;
    /// <summary>
    /// 第三方平台分润
    /// </summary>
    private BigDecimal thirdpary_profit ;
    /// <summary>
    /// 银通帐号
    /// </summary>
    private String ytAccount ;
    /// <summary>
    /// 渠道编号
    /// </summary>
    private long channelId ;
    /// <summary>
    /// 省
    /// </summary>
    private int provinceId ;
    /// <summary>
    /// 市
    /// </summary>
    private int cityId ;
    /// <summary>
    /// 区
    /// </summary>
    private int districtId ;
    /// <summary>
    /// 退款状态
    /// </summary>
    private String isRefund ;
    /// <summary>
    /// 结算状态
    /// </summary>
    private String isSettle ;
    /// <summary>
    /// 结算时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date settleTime ;
    /// <summary>
    /// 订单备注
    /// </summary>
    private String remark ;
    /// <summary>
    /// 支付有效时间
    /// </summary>
    private String timeoutExpress ;
    /// <summary>
    /// 下游通知地址
    /// </summary>
    private String notifyUrl ;
    /// <summary>
    /// 顾客支付宝UID
    /// </summary>
    private String buyerId ;
    /// <summary>
    /// 顾客支付宝账号
    /// </summary>
    private String buyerAccount ;
    /// <summary>
    /// 支付请求流水号(唯一)
    /// </summary>
    private String requestNo ;
    /// <summary>
    /// 订单标识
    /// </summary>
    private String version ;
    /// <summary>
    /// 营业员工号
    /// </summary>
    private String salesNo ;
    /// <summary>
    /// 针对质押分期（0：本息）（1：本金）
    /// </summary>
    private int freezeType ;
    /// <summary>
    /// 更新时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime ;
    /// <summary>
    /// 创建时间
    /// </summary>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime ;
    /// <summary>
    /// 质押允许使用的种类
    /// </summary>
    private String zyEnableChannels ;
    /// <summary>
    /// 异业订单是否允许结算
    /// </summary>
    private int zySettle ;
    /// <summary>
    /// 用户姓名
    /// </summary>
    private String certName ;
    /// <summary>
    /// 身份证号
    /// </summary>
    private String certNo ;
    /// <summary>
    /// 1：正常解冻 2：退货;3:违约提前结清 5：提前结清
    /// </summary>
    private String refundType ;
    /// <summary>
    /// 是否是线上分期  (默认) 0 false 1 true
    /// </summary>
    private int isOnline ;
    /// <summary>
    /// 结算 失败的原因
    /// </summary>
    private String settleMessage ;
    /// <summary>
    /// 花呗c2b二维码
    /// </summary>
    private String qrCode ;
    /// <summary>
    /// 是否为银通帐号结算（0：否；1：是）
    /// </summary>
    private int isYtSettle ;
    /// <summary>
    /// 是否已提前结清，0：默认：2：违约：4：提前结清：10：违约或提前结清处理中
    /// </summary>
    private int unfrzType ;
    /// <summary>
    /// 渠道类型 -- 社会厅：SHT，自有厅：ZYT，线上：ONL
    /// </summary>
    private String tranType ;
    /// <summary>
    /// 用户费率
    /// </summary>
    private BigDecimal fqFlatRate ;
    /// <summary>
    /// 续约生效时间（格式：yyyyMMdd）
    /// </summary>
    private String renewalTime ;
    /// <summary>
    /// 是否已经Ai回访，1：是；0：否
    /// </summary>
    private String isCall ;
    /// <summary>
    /// 
    /// </summary>
    private String goodsImgUrl ;
    /// <summary>
    /// 分期配置金额
    /// </summary>
    private int configPrincipal ;
    /// <summary>
    /// 用户手机号码
    /// </summary>
    private String mobileNo ;
    /// <summary>
    /// 产品编号
    /// </summary>
    private String productId ;
    /// <summary>
    /// 风控备注信息
    /// </summary>
    private String remarkRisk ;
    /// <summary>
    /// 1：已退还款计划；0：未退还款计划
    /// </summary>
    private int planSucc ;

}

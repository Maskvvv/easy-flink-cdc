package com.esflink.demo.pojo;

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
public class CreditAPI_TotalOrder {
    /// <summary>
    /// 系统号
    /// </summary>
    private long id;
    /// <summary>
    /// 平台订单号
    /// </summary>
    private String code;
    /// <summary>
    /// 下游平台标识
    /// </summary>
    private int merchId;
    /// <summary>
    /// 下游提交单号
    /// </summary>
    private String thirdcode;
    /// <summary>
    /// 商户订单号
    /// </summary>
    private String submerorderno;
    /// <summary>
    /// 商户号（下游定义）
    /// </summary>
    private String merno;
    /// <summary>
    /// 门店id（下游定义）
    /// </summary>
    private String storeid;
    /// <summary>
    /// 订单主题
    /// </summary>
    private String subject;
    /// <summary>
    /// 商品详情
    /// </summary>
    private String goods;
    /// <summary>
    /// 订单金额（不包含手续费）
    /// </summary>
    private BigDecimal price;
    /// <summary>
    /// 分期期数
    /// </summary>
    private int pers;
    /// <summary>
    /// 承担手续费方式0：用户承担；100：商家承担
    /// </summary>
    private int percents;
    /// <summary>
    /// 分期总金额（包含手续费）
    /// </summary>
    private BigDecimal payprice;
    /// <summary>
    /// 分期方式
    /// </summary>
    private String payaway;
    /// <summary>
    /// 支付时间
    /// </summary>
    private Date paytime;
    /// <summary>
    /// 支付单号（如：支付宝反回交易流水号）
    /// </summary>
    private String serialno;
    /// <summary>
    /// 余额宝质押返回
    /// </summary>
    private String operationid;
    /// <summary>
    /// 订单状态
    /// </summary>
    private String status;
    /// <summary>
    /// 银通利润
    /// </summary>
    private BigDecimal ytprofit;
    /// <summary>
    /// 第三方平台分润
    /// </summary>
    private BigDecimal thirdparyProfit;
    /// <summary>
    /// 银通帐号
    /// </summary>
    private String ytaccount;
    /// <summary>
    /// 渠道编号
    /// </summary>
    private long channelid;
    /// <summary>
    /// 省
    /// </summary>
    private int provinceid;
    /// <summary>
    /// 市
    /// </summary>
    private int cityid;
    /// <summary>
    /// 区
    /// </summary>
    private int districtid;
    /// <summary>
    /// 退款状态
    /// </summary>
    private String isrefund;
    /// <summary>
    /// 结算状态
    /// </summary>
    private String issettle;
    /// <summary>
    /// 结算时间
    /// </summary>
    private Date settletime;
    /// <summary>
    /// 订单备注
    /// </summary>
    private String remark;
    /// <summary>
    /// 支付有效时间
    /// </summary>
    private String timeoutexpress;
    /// <summary>
    /// 下游通知地址
    /// </summary>
    private String notifyurl;
    /// <summary>
    /// 顾客支付宝UID
    /// </summary>
    private String buyerid;
    /// <summary>
    /// 顾客支付宝账号
    /// </summary>
    private String buyeraccount;
    /// <summary>
    /// 支付请求流水号(唯一)
    /// </summary>
    private String requestno;
    /// <summary>
    /// 订单标识
    /// </summary>
    private String version;
    /// <summary>
    /// 营业员工号
    /// </summary>
    private String salesno;
    /// <summary>
    /// 针对质押分期（0：本息）（1：本金）
    /// </summary>
    private int freezetype;
    /// <summary>
    /// 更新时间
    /// </summary>
    private Date updatetime;
    /// <summary>
    /// 创建时间
    /// </summary>
    private Date createtime;
    /// <summary>
    /// 质押允许使用的种类
    /// </summary>
    private String zyenablechannels;
    /// <summary>
    /// 异业订单是否允许结算
    /// </summary>
    private int zysettle;
    /// <summary>
    /// 用户姓名
    /// </summary>
    private String certname;
    /// <summary>
    /// 身份证号
    /// </summary>
    private String certno;
    /// <summary>
    /// 1：正常解冻 2：退货;3:违约提前结清 5：提前结清
    /// </summary>
    private String refundtype;
    /// <summary>
    /// 是否是线上分期  (默认) 0 false 1 true
    /// </summary>
    private int isonline;
    /// <summary>
    /// 结算 失败的原因
    /// </summary>
    private String settlemessage;
    /// <summary>
    /// 花呗c2b二维码
    /// </summary>
    private String qrcode;
    /// <summary>
    /// 是否为银通帐号结算（0：否；1：是）
    /// </summary>
    private int isytsettle;
    /// <summary>
    /// 是否已提前结清，0：默认：2：违约：4：提前结清：10：违约或提前结清处理中
    /// </summary>
    private int unfrztype;
    /// <summary>
    /// 渠道类型 -- 社会厅：SHT，自有厅：ZYT，线上：ONL
    /// </summary>
    private String trantype;
    /// <summary>
    /// 用户费率
    /// </summary>
    private BigDecimal fqflatrate;
    /// <summary>
    /// 续约生效时间（格式：yyyyMMdd）
    /// </summary>
    private String renewaltime;
    /// <summary>
    /// 是否已经Ai回访，1：是；0：否
    /// </summary>
    private String iscall;
    /// <summary>
    /// 
    /// </summary>
    private String goodsimgurl;
    /// <summary>
    /// 分期配置金额
    /// </summary>
    private int configprincipal;
    /// <summary>
    /// 用户手机号码
    /// </summary>
    private String mobileno;
    /// <summary>
    /// 产品编号
    /// </summary>
    private String productid;
    /// <summary>
    /// 风控备注信息
    /// </summary>
    private String remarkrisk;
    /// <summary>
    /// 1：已退还款计划；0：未退还款计划
    /// </summary>
    private int plansucc;

}

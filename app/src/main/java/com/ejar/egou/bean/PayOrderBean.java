package com.ejar.egou.bean;

/**
 * Created by Administrator on 2017\12\9 0009.
 *
 *
 * 将支付宝返回的信息 转换到此类 以json 形式返回给js前端
 */

public class PayOrderBean {

    String respCode;
    String respMsg;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}

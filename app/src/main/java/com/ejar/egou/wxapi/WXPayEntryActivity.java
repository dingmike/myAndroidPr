package com.ejar.egou.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ejar.egou.Constants_num;
import com.ejar.egou.R;
import com.ejar.egou.rx.MessageBean;
import com.ejar.egou.rx.RxBus;
import com.jaeger.library.StatusBarUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 此界面设置为完全透明，并且状态栏设置与Mainactivity一致，微信返回的结果在此页面post到主界面，并且结束此界面
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;


    private WeChartPayBack weChartPayBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent));
        api = WXAPIFactory.createWXAPI(this, Constants_num.APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            MessageBean messageBean = new MessageBean();
            messageBean.setTag("wecharPay");
            messageBean.setResultCode(resp.errCode + "");
            RxBus.getInstance().post(messageBean);//到MainActivity接收消息
            finish();
        }
    }

    private void setPayResult(BaseResp resp) {
        weChartPayBack.payBackInfo(resp.errCode);
        WXPayEntryActivity.this.finish();
    }


    public static interface WeChartPayBack {
        void payBackInfo(int payBackInfo);
    }
}
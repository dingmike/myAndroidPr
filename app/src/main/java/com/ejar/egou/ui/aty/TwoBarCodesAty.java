package com.ejar.egou.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ejar.egou.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * Created by Administrator on 2018\2\4 0004.
 * 二维码扫描
 */

public class TwoBarCodesAty extends AppCompatActivity implements QRCodeView.Delegate {
    private QRCodeView qrCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_two_bar);

        qrCodeView = (QRCodeView) findViewById(R.id.zxingview);
        qrCodeView.setDelegate(this);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("twoBarCode", result);
        setResult(9002, intent);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        qrCodeView.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        qrCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        qrCodeView.onDestroy();
        super.onDestroy();
    }


}

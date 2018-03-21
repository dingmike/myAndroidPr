package com.ejar.egou.ui.aty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ejar.chapp.baselibrary.TakeImgActivity;
import com.ejar.chapp.baselibrary.base.BaseActivity;
import com.ejar.chapp.baselibrary.net.RetrofitManager;
import com.ejar.chapp.baselibrary.utils.TU;
import com.ejar.egou.Constants_num;
import com.ejar.egou.R;
import com.ejar.egou.application.MyApplicationLike;
import com.ejar.egou.bean.AlipayResultBean;
import com.ejar.egou.bean.PayOrderBean;
import com.ejar.egou.bean.PhotoCallbackBean;
import com.ejar.egou.bean.WxCharLoginBean;
import com.ejar.egou.mvp.contract.MainAtyContract;
import com.ejar.egou.mvp.presenter.MainatyPresenter;
import com.ejar.egou.net.IdeaApiService;
import com.ejar.egou.rx.MessageBean;
import com.ejar.egou.rx.RxBus;
import com.ejar.egou.utils.ImageUtils;
import com.ejar.egou.wxapi.WXPayEntryActivity;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.mob.tools.utils.UIHandler;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements Handler.Callback {
    private static final String TAG = "egou MainActivity";
    private BridgeWebView webView;
    private CallBackFunction payCallBack;
    private CallBackFunction twoBarCodeCallBack;
    private MainatyPresenter presenter;
    private static final int SDK_PAY_FLAG = 1;
    //    private IWXAPI api;
    private WXPayEntryActivity.WeChartPayBack chartBack;

    // 统一消息处理
    private static final int MSG_USERID_FOUND = 1; // 用户信息已存在
    private static final int MSG_LOGIN = 2; // 登录操作
    private static final int MSG_AUTH_CANCEL = 3; // 授权取消
    private static final int MSG_AUTH_ERROR = 4; // 授权错误
    private static final int MSG_AUTH_COMPLETE = 5; // 授权完成


    @SuppressLint("SetJavaScriptEnabled")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainatyPresenter();
        webView = (BridgeWebView) findViewById(R.id.webview);
        StatusBarUtil.setTranslucent(this);


//        button.setOnClickListener(v -> {
//            Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
//            //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
//            weibo.SSOSetting(true);//false 表示 使用SSO 授权方式
//            weibo.setPlatformActionListener(new PlatformActionListener() {
//
//                @Override
//                public void onError(Platform arg0, int action, Throwable t) {
//                    // TODO Auto-generated method stub
////                    arg2.printStackTrace();t
//                    if (action == Platform.ACTION_USER_INFOR) {
//                        UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, MainActivity.this);
//                    }
//                    t.printStackTrace();
//                }
//
//                @Override
//                public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
//                    // TODO Auto-generated method stub
//                    //输出所有授权信息
//                    if (action == Platform.ACTION_USER_INFOR) {
//                        UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, MainActivity.this);
//                        // 业务逻辑处理：比如登录操作
//                        String userName = platform.getDb().getUserName(); // 用户昵称
//                        String userId = platform.getDb().getUserId();   // 用户Id
//                        String platName = platform.getName();     // 平台名称
//                        Log.e("sanf", "ag0" + platform.getDb().exportData() + "   ag2" + arg2.toString());
////                        login(platName, userName, res);
//                    }
//
//                }
//
//                @Override
//                public void onCancel(Platform arg0, int action) {
//                    // TODO Auto-generated method stub
//                    if (action == Platform.ACTION_USER_INFOR) {
//                        UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, MainActivity.this);
//                    }
//                }
//            });
//            weibo.showUser(null);//授权并获取用户信息
//        });


        initWebView();

        contactJs();

        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            url = "file:///android_asset/www/index.html";
        }
        webView.loadUrl(url);
        initPermission();

        chartBack = new WXPayEntryActivity.WeChartPayBack() {
            @Override
            public void payBackInfo(int payBackInfo) {
                PayOrderBean payOrderBean = new PayOrderBean();
                if (payBackInfo == 0) {
                    payOrderBean.setRespCode("0");
                    payOrderBean.setRespMsg("支付成功");

                } else if (payBackInfo == -1) {
                    payOrderBean.setRespCode("-1");
                    payOrderBean.setRespMsg("支付失败");
                } else if (payBackInfo == -2) {
                    payOrderBean.setRespCode("-2");
                    payOrderBean.setRespMsg("支付取消");
                }
                Logger.e(new Gson().toJson(payOrderBean));
                payCallBack.onCallBack(new Gson().toJson(payOrderBean));
            }
        };
    }


    @Override
    protected void startGetData() {

    }

    @Override
    protected void initView() {
        StatusBarUtil.setTranslucent(this);
        presenter = new MainatyPresenter();
        webView = (BridgeWebView) findViewById(R.id.webview);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }




    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case MSG_USERID_FOUND:
                Toast.makeText(this, "用户信息已存在，正在跳转登录操作......", Toast.LENGTH_SHORT).show();
                break;
            case MSG_LOGIN:
                Toast.makeText(this, "使用微信帐号登录中...", Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_CANCEL:
                Toast.makeText(this, "授权操作已取消", Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_ERROR:
                Toast.makeText(this, "授权操作遇到错误，请阅读Logcat输出", Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_COMPLETE:
                Toast.makeText(this, "授权成功，正在跳转登录操作…", Toast.LENGTH_SHORT).show();
                // 执行相关业务逻辑操作，比如登录操作
//                String userName = new Wechat(MainActivity.this).getDb().getUserName(); // 用户昵称
//                String userId = new Wechat(MainActivity.this).getDb().getUserId();   // 用户Id
//                String platName = new Wechat(MainActivity.this).getName();      // 平台名称

//                login(platName, userId, null);
                break;
        }
        return false;
    }


    private void initPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        TU.t(permission.name + " 权限被禁止，无法进行操作");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage("必须权限被禁止，请到 设置-权限 中给予")
                                .setPositiveButton("确定", (dialogInterface, i) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                })
                                .create();
                        dialog.show();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) { //浏览器后退
            webView.goBack();
        } else super.onBackPressed();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    AlipayResultBean payResult = new AlipayResultBean((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
//                    Log.e("resultStatus", resultStatus);
                    PayOrderBean payOrderBean = new PayOrderBean();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        payOrderBean.setRespCode("0");
                        payOrderBean.setRespMsg("支付成功");

                    } else if (TextUtils.equals(resultStatus, "4000") || TextUtils.equals(resultStatus, "6002")) {
                        payOrderBean.setRespCode("-1");
                        payOrderBean.setRespMsg("支付失败");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        payOrderBean.setRespCode("-2");
                        payOrderBean.setRespMsg("支付取消");
                    } else {
                        payOrderBean.setRespCode("-99");
                        payOrderBean.setRespMsg("未知错误");
                    }
                    payCallBack.onCallBack(new Gson().toJson(payOrderBean));
                    break;
                default:
                    break;
            }
        }

    };


    //图片 回调
    private CallBackFunction photoCallBack = null;

    //获取到的图片的地址
    private String imgPath;

    //获取图片
    private static final int REQUEST_GET_PHOTO = 101;


    //设定互调的方法
    private void contactJs() {
        webView.registerHandler("loadThreePay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                payCallBack = function;
                if (TextUtils.isEmpty(data)) {
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = "";
                    handler.sendMessage(msg);
                }
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String way = jsonObject.getString("pay");

                    if (way.equals("Alipay")) {
                        String singn = jsonObject.getString("data");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(MainActivity.this);
                                Map<String, String> result = alipay.payV2(singn, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        }).start();
                    } else if (way.equals("WeChat")) {
                        if (presenter.isWXcharAvilible(MainActivity.this)) {
                            JSONObject wchart = jsonObject.getJSONObject("data");
                            PayReq req = new PayReq();
                            req.appId = wchart.getString("appid");
                            req.nonceStr = wchart.getString("noncestr");
                            req.packageValue = wchart.getString("package");
                            req.partnerId = wchart.getString("partnerid");
                            req.prepayId = wchart.getString("prepayid");
                            req.sign = wchart.getString("sign");
                            req.timeStamp = wchart.getString("timestamp");
//                            Toast.makeText(MainActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            MyApplicationLike.api.sendReq(req);
                        } else {
                            TU.t("请安装微信客户端");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Flowable<MessageBean> flowable = RxBus.getInstance().register(MessageBean.class);
        flowable.subscribe(new Consumer<MessageBean>() {
            @Override
            public void accept(MessageBean messageBean) throws Exception {
                switch (messageBean.getTag()) {
                    case "wecharPay":
                        String s = messageBean.getResultCode();
                        Log.e("微信返回", s + "");
                        PayOrderBean payOrderBean = new PayOrderBean();
                        if (Integer.parseInt(s) == 0) {
                            payOrderBean.setRespCode("0");
                            payOrderBean.setRespMsg("支付成功");

                        } else if (Integer.parseInt(s) == -1) {
                            payOrderBean.setRespCode("-1");
                            payOrderBean.setRespMsg("支付失败");
                        } else if (Integer.parseInt(s) == -2) {
                            payOrderBean.setRespCode("-2");
                            payOrderBean.setRespMsg("支付取消");
                        } else {
                            payOrderBean.setRespCode("-99");
                            payOrderBean.setRespMsg("未知错误");
                        }
                        Log.e("微信返回", new Gson().toJson(payOrderBean));
                        payCallBack.onCallBack(new Gson().toJson(payOrderBean));
                        break;
                    case "wecharLogin"://微信登录
                        String token = messageBean.getResultCode();
                        RetrofitManager.create(IdeaApiService.class)//?appid=/{appid}&secret=/{secret}&code=/{code}&grant_type=authorization_code
                                .getWeCharMessage(Constants_num.APP_ID, Constants_num.SECRET, token, "authorization_code")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<WxCharLoginBean>() {
                                    @Override
                                    public void onNext(WxCharLoginBean wxCharLoginBean) {
                                        Logger.e(wxCharLoginBean.toString());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("ERROR" + e);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Logger.e("onComplete");
                                    }
                                });

                        break;
                    case "userRefuse"://用户拒绝授权
                        break;
                    case "userCancel"://用户取消授权
                        break;
                }
            }
        });


        //图片    type:"调用类型"（Camera：相机，Photo：相册）
        webView.registerHandler("loadPicture", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e(TAG, "拍照   " + data);
                photoCallBack = function;
                imgPath = "";

                Intent in = new Intent(MainActivity.this, TakeImgActivity.class);
                if (data.equals("{\"type\":\"Photo\"}")) {
                    //选取
                    in.putExtra("mode", 2);
                } else if (data.equals("{\"type\":\"Camera\"}")) {
                    in.putExtra("mode", 1);
                }
                startActivityForResult(in, REQUEST_GET_PHOTO);
//                SystemUtils.backgroundAlpha(MainActivity.this, 0.7f);
            }
        });


        //调用相机扫描二维码
        webView.registerHandler("loadScan", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("msg", "扫描 " + data);
                twoBarCodeCallBack = function;
                Intent intent = new Intent(MainActivity.this, TwoBarCodesAty.class);
                startActivityForResult(intent, 9001);
            }
        });
    }


    private void initWebView() {
        //webview 配置
        WebView.setWebContentsDebuggingEnabled(true);

        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheMaxSize(1024 * 1024 * 9);// 设置缓冲大小
        webView.setBackgroundColor(getResources().getColor(R.color.black));
        settings.setTextZoom(100);

        //先阻塞加载图片
        settings.setBlockNetworkImage(true);

        webView.setWebViewClient(new BridgeWebViewClient(webView) {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("加载url", url + " ");
                settings.setBlockNetworkImage(false);
                //判断webview是否加载了，图片资源
                if (!settings.getLoadsImagesAutomatically()) {
                    //设置wenView加载图片资源
                    settings.setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        SystemUtils.backgroundAlpha(this, 1f);
        //获取图片
        if (requestCode == REQUEST_GET_PHOTO) {

            PhotoCallbackBean pcb = new PhotoCallbackBean();
            PhotoCallbackBean.PhotoMsg psg = new PhotoCallbackBean.PhotoMsg();

            if (resultCode == RESULT_OK && data != null) {
                imgPath = data.getStringExtra("path");

                Bitmap bmp = BitmapFactory.decodeFile(imgPath);
//                String bmpString = "data:image/png;base64," + bitmapToBase64(bmp);


                psg.setData(ImageUtils.bitmapToBase64(bmp));
                pcb.setRet_code("1");
                pcb.setRet_msg(psg);


            } else {
                imgPath = "";
//                TU.t("获取图片失败");
                psg.setData("获取图片失败");
                pcb.setRet_code("0");
                pcb.setRet_msg(psg);


            }
            String info = new Gson().toJson(pcb);
            Log.e("照片回调", info);
            photoCallBack.onCallBack(info);
        }
        if (requestCode == 9001) {
            String twoBarCode = data.getStringExtra("twoBarCode");
            Log.e("twoBarCode", twoBarCode);
            twoBarCodeCallBack.onCallBack(twoBarCode);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregisterAll();
    }


}

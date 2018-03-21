package com.ejar.egou.net;

import com.ejar.egou.Constants_num;
import com.ejar.egou.bean.WxCharLoginBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public interface IdeaApiService {
    String WXCHAR_URL = "https://api.weixin.qq.com/sns/oauth2/";
    /**
     * 网络请求超时时间毫秒
     */
    int DEFAULT_TIMEOUT = 15000;

    String secret = Constants_num.SECRET;
    String appid = Constants_num.APP_ID;

    @GET("access_token")
    Observable<WxCharLoginBean> getWeCharMessage(@Query("appid") String appid,
                                                 @Query("secret") String secret,
                                                 @Query("code") String code,
                                                 @Query("grant_type") String type);
}

package com.ejar.egou.mvp.model;

import com.ejar.chapp.baselibrary.net.RetrofitManager;
import com.ejar.chapp.baselibrary.utils.SchedulerUtils;
import com.ejar.egou.bean.WxCharLoginBean;
import com.ejar.egou.net.IdeaApiService;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018\3\7 0007.
 */

public class MainModel {
    public Observable<WxCharLoginBean> requestWxchat() {
        return RetrofitManager.create(IdeaApiService.class).getWeCharMessage("", "", "", "")
                .compose(SchedulerUtils.INSTANCE.ioToMain());
    }
}

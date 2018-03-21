package com.ejar.egou.mvp.contract;

import android.content.Context;

import com.ejar.chapp.baselibrary.base.IBaseView;
import com.ejar.egou.bean.PermissionItem;
import com.ejar.egou.bean.WxCharLoginBean;

import java.util.List;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public interface MainAtyContract {
    interface MainPresenterIpl {

        void requestPermission(List<PermissionItem> permissionItems);

        void LoginThirdParty(String name);

        void requestWxChat(String aa,String bb,String cc,String dd);
    }

    interface MainAtyIpl extends IBaseView{
        Context getContext();

       void setWxChatData(WxCharLoginBean wxChatData);
    }
}

package com.ejar.egou.mvp.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ejar.chapp.baselibrary.base.BasePresenter;
import com.ejar.egou.bean.PermissionItem;
import com.ejar.egou.bean.WxCharLoginBean;
import com.ejar.egou.mvp.contract.MainAtyContract;
import com.ejar.egou.mvp.model.MainModel;
import com.ejar.egou.mvp.model.bean.MainBean;
import com.ejar.egou.ui.aty.MainActivity;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.weyye.hipermission.HiPermission;

/**
 * Created by Administrator on 2017\12\9 0009.
 */

public class MainatyPresenter {
    private static final int SDK_PAY_FLAG = 1;

    private MainModel mainModel = new MainModel();



    public boolean isWXcharAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pn = packageInfos.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


}

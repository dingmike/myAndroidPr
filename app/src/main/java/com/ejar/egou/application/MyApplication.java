package com.ejar.egou.application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by Administrator on 2017\12\19 0019.
 */

public class MyApplication extends TinkerApplication {
    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.ejar.egou.application.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}

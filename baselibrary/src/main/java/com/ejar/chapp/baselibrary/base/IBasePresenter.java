package com.ejar.chapp.baselibrary.base;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public interface IBasePresenter<V extends IBaseView> {
    void attachView(V mRootView);

    void detachView();
}

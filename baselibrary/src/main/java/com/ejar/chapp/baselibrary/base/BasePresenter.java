package com.ejar.chapp.baselibrary.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public class BasePresenter<T extends IBaseView> implements IBasePresenter<T> {
    public T mRootView = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(T mRootView) {
        mRootView = mRootView;
    }

    @Override
    public void detachView() {
        mRootView = null;
        //取消订阅
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    public boolean isViewAttached() {
        return mRootView != null;
    }

    public void addSubscription(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void checkViewAttached() {
        if (!isViewAttached()) {
//            throw new MvpViewNotAttachedException();
        }
    }

//    private class MvpViewNotAttachedException extends RuntimeException {
//        public MvpViewNotAttachedException() {
//            throw new RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter");
//        }
//
//    }


}

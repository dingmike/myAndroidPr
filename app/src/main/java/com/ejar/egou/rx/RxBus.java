package com.ejar.egou.rx;


import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by Administrator on 2017\12\22 0022.
 */

public class RxBus {
    private final FlowableProcessor<Object> mBus;

    public RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }


    private static class Holder{
        private static RxBus instance = new RxBus();
    }



    /**
     * 单例模式RxBus
     *
     * @return
     */
    public static RxBus getInstance() {

//        if (mInstance == null) {
//            synchronized (RxBus.class) {
//                if (mInstance == null) {
//                    mInstance = new RxBus();
//                }
//            }
//        }
        return Holder.instance;
    }

    /**
     * 发送消息
     *
     * @param object
     */
    public void post(@NonNull Object object) {

        mBus.onNext(object);

    }

    public <T> Flowable<T> register(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public void unregisterAll() {
        //解除注册
        mBus.onComplete();
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }


//    /**
//     * 接收消息
//     *
//     * @param eventType
//     * @param <T>
//     * @return
//     */
//    public <T> Observable<T> toObserverable(Class<T> eventType) {
//        return bus.ofType(eventType);
//    }

}

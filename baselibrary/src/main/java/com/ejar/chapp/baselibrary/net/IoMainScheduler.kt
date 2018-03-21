package com.ejar.chapp.baselibrary.net

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2018\3\7 0007.
 */

class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(),AndroidSchedulers.mainThread())

package com.ejar.chapp.baselibrary.utils

import com.ejar.chapp.baselibrary.net.IoMainScheduler

/**
 * Created by Administrator on 2018\3\8 0008.
 */
object SchedulerUtils {
    fun <T> ioToMain(): IoMainScheduler<T>{
        return IoMainScheduler()
    }
}
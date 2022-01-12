package com.gyf.cactus.callback

/**
 * 监听回调
 *
 */
interface CactusCallback {

    /**
     * do something
     */
    fun doWork(times: Int)

    /**
     * 停止时调用
     */
    fun onStop()
}
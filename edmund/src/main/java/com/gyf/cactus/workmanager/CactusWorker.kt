package com.gyf.cactus.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gyf.cactus.ext.*

/**
 * WorkManager定时器
 *
 */
class CactusWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * 停止标识符
     */
    private var mIsStop = false

    init {
        context.registerStopReceiver {
            mIsStop = true
        }
    }

    override fun doWork(): Result {
        context.apply {
            val cactusConfig = getConfig()
            log("${this@CactusWorker}-doWork")
            if (!isCactusRunning && !mIsStop && !isStopped) {
                register(cactusConfig)
            }
        }
        return Result.success()
    }
}
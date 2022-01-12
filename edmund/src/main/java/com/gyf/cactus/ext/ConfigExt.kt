package com.gyf.cactus.ext

import android.content.Context
import com.google.gson.Gson
import com.gyf.cactus.entity.CactusConfig
import com.gyf.cactus.entity.Constant

/**
 * 配置信息扩展
 *
 */

/**
 * 保存配置信息
 */
internal fun Context.saveConfig(cactusConfig: CactusConfig) {
    sCactusConfig = cactusConfig
    val serviceId = getServiceId()
    if (serviceId > 0) {
        cactusConfig.notificationConfig.serviceId = serviceId
    }
    getSharedPreferences(Constant.CACTUS_TAG, Context.MODE_PRIVATE).edit().apply {
        putString(Constant.CACTUS_CONFIG, Gson().toJson(cactusConfig))
        if (serviceId <= 0) {
            putInt(Constant.CACTUS_SERVICE_ID, cactusConfig.notificationConfig.serviceId)
        }
    }.apply()
}

/**
 * 获取配置信息
 *

 */
internal fun Context.getConfig() = sCactusConfig ?: getPreviousConfig() ?: CactusConfig()

/**
 * 获取Sp保存的配置信息
 *

 */
internal fun Context.getPreviousConfig() = getSharedPreferences(
    Constant.CACTUS_TAG,
    Context.MODE_PRIVATE
).getString(Constant.CACTUS_CONFIG, null)?.run {
    Gson().fromJson(this, CactusConfig::class.java)
}

/**
 * 保存JobId
 *
 */
internal fun Context.saveJobId(jobId: Int) =
    getSharedPreferences(
        Constant.CACTUS_TAG,
        Context.MODE_PRIVATE
    ).edit().putInt(Constant.CACTUS_JOB_ID, jobId).apply()

/**
 * 获得JobId
 *
 */
internal fun Context.getJobId() =
    getSharedPreferences(
        Constant.CACTUS_TAG,
        Context.MODE_PRIVATE
    ).getInt(Constant.CACTUS_JOB_ID, -1)

/**
 * 获得serviceId
 *
 */
private fun Context.getServiceId() = getSharedPreferences(
    Constant.CACTUS_TAG,
    Context.MODE_PRIVATE
).getInt(Constant.CACTUS_SERVICE_ID, -1)
package com.gyf.cactus.sample

import kotlin.reflect.jvm.jvmName


/**
 * SharedPreferences扩展函数
 */
inline fun <reified R, T> R.preference(defaultValue: T) =
    Preference(App.context, "", defaultValue, R::class.jvmName)
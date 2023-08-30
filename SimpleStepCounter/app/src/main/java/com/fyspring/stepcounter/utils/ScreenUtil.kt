package com.fyspring.stepcounter.utils

import android.content.Context



class ScreenUtil {
    companion object {
        fun getScreenWidth(mContext: Context): Int {

            val displayMetrics = mContext.resources.displayMetrics
            //获取屏幕宽高，单位是像素
            val widthPixels = displayMetrics.widthPixels
            val heightPixels = displayMetrics.heightPixels
            //获取屏幕密度倍数
            val density = displayMetrics.density

            return widthPixels
        }
    }
}
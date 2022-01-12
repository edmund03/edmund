package com.gyf.cactus.sample

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.gyf.cactus.ext.cactusRestart
import com.gyf.cactus.ext.cactusUnregister
import com.gyf.cactus.ext.cactusUpdateNotification
import kotlinx.android.synthetic.main.activity_main.*



@Suppress("DIVISION_BY_ZERO")
@SuppressLint("SetTextI18n")
class MainActivity : BaseActivity() {

    private var times = 0L

    private val list = listOf(
        Pair("今日头条", "抖音全世界通用"),
        Pair("微博", "赵丽颖冯绍峰离婚"),
        Pair("绿洲", "今天又是美好的一天"),
        Pair("QQ", "好友申请"),
        Pair("微信", "在吗？"),
        Pair("百度地图", "新的路径规划"),
        Pair("小米天气", "明日大风，注意出行"),
        Pair("信息", "1条文本信息"),
        Pair("淘宝天猫", "你关注的宝贝降价啦")
    )

    companion object {
        private const val TIME = 4000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        setListener()
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
       navView.setNavigationItemSelectedListener { item ->
           when (item.itemId) {
               R.id.navCall ->{
                   val intent = Intent()
                   intent.action = Intent.ACTION_DIAL
                   startActivity(intent)
               }
               R.id.navPhoto -> {
                   val intent = Intent("android.media.action.IMAGE_CAPTURE")
                   startActivity(intent)
               }
               R.id.navLocation ->{
                   checkApkExist(context = application,"com.autonavi.minimap")
                   val intent = Intent()
                   intent.action =Intent.ACTION_VIEW
                   intent.addCategory(Intent.CATEGORY_DEFAULT)
                   val uri = Uri.parse("androidamap://navi?sourceApplication=appname&amp;poiname=fangheng&amp;lat=36.547901&amp;lon=104.258354&amp;dev=1&amp;style=2")
                   intent.setData(uri)
                   startActivity(intent)
               }
               R.id.navMail ->{
                   checkApkExist(context = application,"com.tencent.androidqqmail")
                   startLaunchAPK(context = application,"com.tencent.androidqqmail","xinActivity")
               }
               R.id.navJibu ->{
                   checkApkExist(context = application,"com.fyspring.stepcounter")
                   startLaunchAPK(context = application,"com.fyspring.stepcounter","newActivity")
               }
           }
           true
       }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.backup -> Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show()
        }
        return true
    }
    //根据包名 判断某APP是否安装
    fun checkApkExist(context: Context,packageName: String) :Boolean{
        //  检查app是否有安装
        if (TextUtils.isEmpty(packageName))
            return  false
        try {
            val info = context.packageManager
                    .getApplicationInfo(
                            packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES
                    )
            // Timber.d(info.toString()) // Timber 是我打印 log 用的工具，这里只是打印一下 log
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            // Timber.d(e.toString()) // Timber 是我打印 log 用的工具，这里只是打印一下 log

            return false
        }

    }

    //通过包名启动第三方应用
    fun startLaunchAPK(context: Context, packageName: String, activityName:String) {
        var mainAct: String? = null
        val pkgMag = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
        //如果已经启动apk，则直接将apk从后台调到前台运行（类似home键之后再点击apk图标启动），如果未启动apk，则重新启动
        @SuppressLint("WrongConstant")
        val list = pkgMag.queryIntentActivities(
                intent,
                PackageManager.GET_ACTIVITIES
        )
        for (i in list.indices) {
            val info = list[i]
            if (info.activityInfo.packageName == packageName) {
                mainAct = info.activityInfo.name
                break
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return
        }
        // 启动指定的activity页面
        //intent.component = ComponentName(packageName,activityName)
        //启动到app的主页或启动到原来留下的位置
        intent.component = ComponentName(packageName,mainAct!!)
        //启动app
        context.startActivity(intent)

    }

    private fun initData() {

        tvVersion.text = "Version(版本)：${BuildConfig.VERSION_NAME}"
        App.mEndDate.observe(this, Observer {
            tvLastDate.text = it
        })
        App.mLastTimer.observe(this, Observer<String> {
            tvLastTimer.text = it
        })
        App.mTimer.observe(this, Observer<String> {
            tvTimer.text = it
        })
        App.mStatus.observe(this, Observer {
            tvStatus.text = if (it) {
                "Operating status(运行状态):Running(运行中)"
            } else {
                "Operating status(运行状态):Stopped(已停止)"
            }
        })
    }

    private fun setListener() {
        //更新通知栏信息
        btnUpdate.onClick {
            val num = (0..8).random()
            cactusUpdateNotification {
                setTitle(list[num].first)
                setContent(list[num].second)
            }
        }
        //停止
        btnStop.onClick {
            cactusUnregister()
        }
        //重启
        btnRestart.onClick {
            cactusRestart()
        }
        //奔溃
        btnCrash.setOnClickListener {
            Toast.makeText(
                this,
                "The app will crash after three seconds(3s后奔溃)",
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({
                2 / 0
            }, 3000)
        }
        fab.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    private inline fun View.onClick(crossinline block: () -> Unit) {
        setOnClickListener {
            val nowTime = System.currentTimeMillis()
            val intervals = nowTime - times
            if (intervals > TIME) {
                times = nowTime
                block()
            } else {
                Toast.makeText(
                    context,
                    ((TIME.toFloat() - intervals) / 1000).toString() + "秒之后再点击",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
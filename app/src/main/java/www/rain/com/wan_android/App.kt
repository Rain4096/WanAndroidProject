package www.rain.com.wan_android

import android.app.Application
import www.xx.com.base_init.BaseInit

/**
 * @author Rain
 * @date 2023/5/9 11:22
 * @version 1
 * @description application
 */
class App :Application() {

    override fun onCreate() {
        super.onCreate()

        BaseInit.initMMKV(this)
    }

}
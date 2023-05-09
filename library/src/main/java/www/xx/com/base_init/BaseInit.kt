package www.xx.com.base_init

import android.app.Application
import www.xx.com.base_utils.mmkv.MMKVUtil

/**
 * Author: Rain
 * Date: 2023/5/9 15:35
 * Version: 1
 * Description: 初始化lib
 */
object BaseInit {

    fun initMMKV(application: Application) {
        MMKVUtil.initMMKV(application)
    }
}
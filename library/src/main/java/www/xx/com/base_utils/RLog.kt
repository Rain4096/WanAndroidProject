package www.xx.com.base_utils

import android.util.Log
import www.xx.com.BuildConfig

/**
 * Author: Rain
 * Date: 2023/5/9 15:01
 * Version: 1
 * Description: 日志输出
 */
object RLog {
    private const val tag = "================="
    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }
}
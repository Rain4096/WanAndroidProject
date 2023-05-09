package www.xx.com.base_utils.mmkv

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import www.xx.com.BuildConfig
import www.xx.com.mkvId

/**
 * Author: Rain
 * Date: 2023/5/9 16:40
 * Version: 1
 * Description: MMKV 操作其他逻辑
 */
object MMKVUtil {

    private val kv: MMKV by lazy { MMKV.mmkvWithID(mkvId) }

    /**
     * 初始化 mkv
     */
    fun initMMKV(application: Application) {
        MMKV.initialize(application)
        MMKV.setLogLevel(
            if (BuildConfig.DEBUG) {
                MMKVLogLevel.LevelDebug
            } else {
                MMKVLogLevel.LevelError
            }
        )
    }

    fun kv() = kv

    /**
     * 清理某项的Value
     * @param key 需要清理的键
     */
    fun clearCache(key: String) {
        kv.run {
            remove(key)
        }
    }

    /**
     * 清除MMKV中非基础数据（保存的Key值不以"_"开头）
     */
    fun deleteCache() {
        kv.run {
            val keys = allKeys()
            if (keys != null) {
                for (i in keys.indices) {
                    if (!keys[i].startsWith("_")) {
                        remove(keys[i])
                    }
                }
            }
        }
    }

    /**
     * 获取MMKV中是否有传递进来的key[key],true：存在传递进来的key值；false：不存在传递进来的key值
     */
    fun hasKey(key: String): Boolean = kv.containsKey(key)

}
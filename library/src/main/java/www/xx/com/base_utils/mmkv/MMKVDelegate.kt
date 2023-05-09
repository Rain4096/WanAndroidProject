package www.xx.com.base_utils.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import www.xx.com.base_utils.RLog
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Author: Rain
 * Date: 2023/5/9 14:54
 * Version: 1
 * Description: MKV 保存代理工具类
 * @param key 保存的 key
 * @param default 获取的默认值 或者保存的默认值
 * @param isCache 是否在特定情况下删除 true 不删除 false 删除 默认删除
 */
class MMKVDelegate<T>(
    key: String, private val default: T, isCache: Boolean = false
) : ReadWriteProperty<Any?, T> {
    private var mKey = if (isCache) key else "_$key" // 以下划线判断是否可以删除
    private val kv: MMKV by lazy { MMKVUtil.kv() }
    private var mOldData: T = default // 之前的数据

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = readData(mKey)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putData(mKey, value)
    }

    private fun readData(key: String): T {
        mOldData = when (default) {
            is String -> kv.decodeString(key, default) as T
            is Int -> kv.decodeInt(key, default) as T
            is Boolean -> kv.decodeBool(key, default) as T
            is Float -> kv.decodeFloat(key, default) as T
            is Long -> kv.decodeLong(key, default) as T
            is Parcelable -> kv.decodeParcelable(key, default.javaClass) as T
            else -> throw IllegalArgumentException("Unknown Type.")
        }
        RLog.i("getValue from delegate is key: $key \n read : $mOldData")
        return mOldData
    }


    private fun putData(key: String, value: T) {
        if (mOldData != value) {
            RLog.i("setValue from delegate is key: $key \n value : $value")
            when (value) {
                is String -> kv.encode(key, value)
                is Boolean -> kv.encode(key, value)
                is Int -> kv.encode(key, value)
                is Float -> kv.encode(key, value)
                is Long -> kv.encode(key, value)
                is Parcelable -> kv.encode(key, value)
            }
        }else{
            RLog.i("之前的数据对象跟现在数据一样，不进行保存....")
        }
    }

}
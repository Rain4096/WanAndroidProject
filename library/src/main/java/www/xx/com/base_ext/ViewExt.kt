package www.xx.com.base_ext

import android.app.Activity
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import www.xx.com.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @ClassName ViewExt
 * @Author Rain
 * @Date 2022/9/28 18:23
 * @Version 1.0
 * @Description view的常用工具类
 */

/**
 * 设置view显示
 */
fun View?.visible() {
    this?.apply {
        if (!isVisible) {
            visibility = View.VISIBLE
        }
    }
}


/**
 * 设置view占位隐藏
 */
fun View?.invisible() {
    this?.apply {
        if (!isInvisible) {
            visibility = View.INVISIBLE
        }
    }
}

/**
 * 设置view隐藏
 */
fun View?.gone() {
    this?.apply {
        if (!isGone) {
            visibility = View.GONE
        }
    }
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View?.visible(flag: Boolean) {
    if (flag) visible() else gone()
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View?.invisible(flag: Boolean) {
    if (flag) visible() else invisible()
}


//region 防止重复点击 800毫秒 View
var lastClickTime = 800L

private var View.lastClick: Long? by viewTags(R.id.tag_last_click_time)

/**
 * 设置防止重复点击事件  优化 添加是否关联其他按钮
 * @param views 需要设置点击事件的view集合
 * @param isSharingInterval 是否关联其他按钮监听
 * @param interval 时间间隔 默认0.5秒
 * @param onClick 点击触发的方法
 */
fun setOnclickNoRepeat(
    vararg views: View?,
    isSharingInterval: Boolean = false,
    interval: Long = lastClickTime,
    onClick: (View) -> Unit
) {
    views.forEach {
        it?.clickNoRepeat(interval = interval, isSharingInterval = isSharingInterval) { view ->
            onClick.invoke(view)
        }
    }
}

/**
 * 防止重复点击事件 默认0.8秒内不可重复点击
 * @param interval 时间间隔 默认0.8秒
 * @param action 执行方法
 */
fun View.clickNoRepeat(
    interval: Long = lastClickTime,
    isSharingInterval: Boolean = false,
    action: (view: View) -> Unit
) {
    setOnClickListener a@{
        val view = if (isSharingInterval) (context as Activity).window?.decorView ?: this else this
        val currentTime = System.currentTimeMillis()
        if ((currentTime - (view.lastClick ?: 0L) < interval)) {
            return@a
        }
        view.lastClick = currentTime
        action(it)
    }
}

/**
 * 代理 view点击时间 的 属性
 */
fun <T> viewTags(key: Int) = object : ReadWriteProperty<View, T?> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: View, property: KProperty<*>) =
        thisRef.getTag(key) as? T

    override fun setValue(thisRef: View, property: KProperty<*>, value: T?) =
        thisRef.setTag(key, value)
}
//endregion
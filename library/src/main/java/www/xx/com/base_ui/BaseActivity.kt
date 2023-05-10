package www.xx.com.base_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import www.xx.com.R

/**
 * Author: Rain
 * Date: 2023/5/9 17:38
 * Version: 1
 * Description: activity  基类
 */
abstract class BaseActivity : AppCompatActivity() {

    private val immersionBar: ImmersionBar by lazy { createStatusBarConfig() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
        initView(savedInstanceState)
        initData()
    }

    /**
     * 设置布局
     */
    open fun setContentLayout() {
        setContentView(getLayoutId())

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()
        }

    }

    /**
     * 初始化视图
     * @return Int 布局id
     * 仅用于不继承BaseDataBindActivity类的传递布局文件
     */
    abstract fun getLayoutId(): Int


    /**
     * 初始化布局
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun initData()

    /**
     * 是否使用沉浸式状态栏
     */
    protected open fun isStatusBarEnabled(): Boolean {
        return true
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    open fun getStatusBarConfig(): ImmersionBar = immersionBar

    /**
     * 初始化沉浸式状态栏
     */
    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this) // 默认状态栏字体颜色为黑色
            .statusBarDarkFont(isStatusBarDarkFont()) // 指定导航栏背景颜色
            .navigationBarColor(R.color.white) // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * 状态栏字体深色模式
     */
    open fun isStatusBarDarkFont(): Boolean {
        return true
    }
}
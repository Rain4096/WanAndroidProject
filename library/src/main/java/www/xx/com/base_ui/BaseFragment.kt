package www.xx.com.base_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Author: Rain
 * Date: 2023/5/10 10:02
 * Version: 1
 * Description: fragment 基类
 */
abstract class BaseFragment : Fragment() {

    private var loading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        loading = false
        return getContentView(inflater, container)
    }

    open fun getContentView(
        inflater: LayoutInflater, container: ViewGroup?
    ): View {
        return inflater.inflate(getLayoutResId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    abstract fun getLayoutResId(): Int

    /**
     * 初始化布局
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    open fun initData() {}

    override fun onResume() {
        super.onResume()
        if (!loading){
            loading = true
            initData()
        }
    }

}
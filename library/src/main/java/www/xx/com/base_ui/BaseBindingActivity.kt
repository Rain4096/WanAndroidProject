package www.xx.com.base_ui

import androidx.viewbinding.ViewBinding
import www.xx.com.base_binding.ActivityBinding
import www.xx.com.base_binding.ActivityBindingDelegate

/**
 * Author: Rain
 * Date: 2023/5/9 17:20
 * Version: 1
 * Description: activity 基类
 */
abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate() {

}
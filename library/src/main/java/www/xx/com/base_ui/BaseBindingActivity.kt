package www.xx.com.base_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import www.xx.com.base_binding.ActivityBinding
import www.xx.com.base_binding.ActivityBindingDelegate

/**
 * Author: Rain
 * Date: 2023/5/9 17:20
 * Version: 1
 * Description: activity 基类
 */
class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
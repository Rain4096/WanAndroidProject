package www.xx.com.base_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import www.xx.com.base_binding.FragmentBinding
import www.xx.com.base_binding.FragmentBindingDelegate

/**
 * Author: Rain
 * Date: 2023/5/9 17:20
 * Version: 1
 * Description: fragment 基类
 */
class BaseBindingFragment<VB : ViewBinding> : Fragment(),
    FragmentBinding<VB> by FragmentBindingDelegate() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = createViewWithBinding(inflater, container)

}
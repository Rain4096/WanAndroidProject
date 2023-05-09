package www.xx.com.base_binding

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * Author: Rain
 * Date: 2023/5/9 17:17
 * Version: 1
 * Description: fragment的 viewBind 初始化
 */
interface FragmentBinding<VB : ViewBinding> {
    val binding: VB
    fun Fragment.createViewWithBinding(inflater: LayoutInflater, container: ViewGroup?): View
}

class FragmentBindingDelegate<VB : ViewBinding> : FragmentBinding<VB> {
    private var _binding: VB? = null
    private val handler by lazy { Handler(Looper.getMainLooper()) }


    override val binding: VB
        get() = requireNotNull(_binding)

    override fun Fragment.createViewWithBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        if (_binding == null) {
            _binding = ViewBindingUtil.getBindingWithGeneric(this, inflater, container, false)
            viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) { handler.post { _binding = null } }
            })
        }
        return _binding!!.root
    }

}
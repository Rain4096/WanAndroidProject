@file:Suppress("UNCHECKED_CAST")

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
import www.xx.com.R
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @ClassName ViewBindingExt
 * @Author Rain
 * @Date 2022/9/29 15:41
 * @Version 1.0
 * @Description 通过反射 获取ViewBinding
 */

/**
 * 通过反射拿到xxxViewBinding的bind方法
 */
fun <VB : ViewBinding> View.getBinding(clazz: Class<VB>) =
    getTag(R.id.tag_view_binding) as? VB ?: (clazz.getMethod("bind", View::class.java)
        .invoke(null, this) as VB).also { setTag(R.id.tag_view_binding, it) }


inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
    VB::class.java.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
        .invoke(null, layoutInflater, parent, attachToParent) as VB


inline fun <reified VB : ViewBinding> View.getBinding() = getBinding(VB::class.java)


/***
 * 在fragment 中使用
 * binding  通过布局直接使用
 * 通过inflate 加载...
 */
enum class Method { BIND, INFLATE }

inline fun <reified VB : ViewBinding> Fragment.binding() =
    FragmentBindingProperty(VB::class.java)

inline fun <reified VB : ViewBinding> Fragment.binding(method: Method) =
    if (method == Method.BIND) FragmentBindingProperty(VB::class.java) else FragmentInflateBindingProperty(
        VB::class.java
    )


class FragmentBindingProperty<VB : ViewBinding>(private val clazz: Class<VB>) :
    ReadOnlyProperty<Fragment, VB> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB =
        requireNotNull(thisRef.view) { "The constructor missing layout id or the property of ${property.name} has been destroyed." }
            .getBinding(clazz)
}

class FragmentInflateBindingProperty<VB : ViewBinding>(private val clazz: Class<VB>) :
    ReadOnlyProperty<Fragment, VB> {
    private var binding: VB? = null
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (binding == null) {
            try {
                @Suppress("UNCHECKED_CAST")
                binding = (clazz.getMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, thisRef.layoutInflater) as VB)
            } catch (e: IllegalStateException) {
                throw IllegalStateException("The property of ${property.name} has been destroyed.")
            }
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    handler.post { binding = null }
                }
            })
        }
        return binding!!
    }
}


/**
 * 自定义布局中使用
 */
inline fun <reified VB : ViewBinding> ViewGroup.inflate() =
    inflateBinding<VB>(LayoutInflater.from(context), this, true)

inline fun <reified VB : ViewBinding> ViewGroup.binding(attachToParent: Boolean = false) = lazy(
    LazyThreadSafetyMode.NONE
) {
    inflateBinding<VB>(
        LayoutInflater.from(context),
        if (attachToParent) this else null,
        attachToParent
    )
}


package www.xx.com.base_binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import www.xx.com.R
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * @ClassName ViewBindingUtil
 * @Author Rain
 * @Date 2022/9/30 15:48
 * @Version 1.0
 * @Description java调用
 */
object ViewBindingUtil {

    fun <VB : ViewBinding> inflateWithGeneric(
        genericOwner: Any,
        layoutInflater: LayoutInflater
    ): VB =
        withGenericBindingClass(genericOwner) { clazz ->
            clazz.getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, layoutInflater) as VB
        }

    fun <VB : ViewBinding> inflateWithGeneric(genericOwner: Any, parent: ViewGroup): VB =
        inflateWithGeneric(genericOwner, LayoutInflater.from(parent.context), parent, false)

    fun <VB : ViewBinding> inflateWithGeneric(
        genericOwner: Any,
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): VB =
        withGenericBindingClass<VB>(genericOwner) { clazz ->
            clazz.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
                .invoke(null, layoutInflater, parent, attachToParent) as VB
        }

    fun <VB : ViewBinding> getBindingWithGeneric(
        genericOwner: Any,
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): VB =
        withGenericBindingClass<VB>(genericOwner) { clazz ->
            clazz.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
                .invoke(null, layoutInflater, parent, attachToParent) as VB
        }

    fun <VB : ViewBinding> getBindingWithGeneric(genericOwner: Any, view: View): VB =
        view.getTag(R.id.tag_view_binding) as? VB ?: bindWithGeneric<VB>(genericOwner, view)
            .also { view.setTag(R.id.tag_view_binding, it) }

    private fun <VB : ViewBinding> bindWithGeneric(genericOwner: Any, view: View): VB =
        withGenericBindingClass<VB>(genericOwner) { clazz ->
            clazz.getMethod("bind", View::class.java).invoke(null, view) as VB
        }


    private fun <VB : ViewBinding> withGenericBindingClass(
        genericOwner: Any,
        block: (Class<VB>) -> VB
    ): VB {
        var genericSuperclass = genericOwner.javaClass.genericSuperclass
        var superclass = genericOwner.javaClass.superclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                genericSuperclass.actualTypeArguments.forEach {
                    try {
                        return block.invoke(it as Class<VB>)
                    } catch (e: NoSuchMethodException) {
                    } catch (e: ClassCastException) {
                    } catch (e: InvocationTargetException) {
                        var tagException: Throwable? = e
                        while (tagException is InvocationTargetException) {
                            tagException = e.cause
                        }
                        throw tagException
                            ?: IllegalArgumentException("ViewBinding generic was found, but creation failed.")
                    }
                }
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        throw IllegalArgumentException("There is no generic of ViewBinding.")
    }
}
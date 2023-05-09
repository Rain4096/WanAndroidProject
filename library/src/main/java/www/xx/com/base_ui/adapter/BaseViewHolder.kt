package www.xx.com.base_ui.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import www.xx.com.base_ext.gone
import www.xx.com.base_ext.invisible
import www.xx.com.base_ext.setOnclickNoRepeat
import www.xx.com.base_ext.visible

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews: SparseArray<View> = SparseArray()

    companion object {
        fun get(parent: ViewGroup, @LayoutRes layoutId: Int): BaseViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return get(itemView)
        }

        fun get(@NonNull view: View) = BaseViewHolder(view)
    }

    fun <T : View> getView(viewId: Int): T {
        var view = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        @Suppress("UNCHECKED_CAST")
        return view as T
    }

    fun setText(viewId: Int, text: String): BaseViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setText(viewId: Int, text: String, isSelect: Boolean = false): BaseViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        tv.isSelected = isSelect
        return this
    }

    fun setImageSource(viewId: Int, imageId: Int): BaseViewHolder {
        val iv = getView<ImageView>(viewId)
        iv.setImageResource(imageId)
        return this
    }

    fun setImageSource(viewId: Int, imageId: Int, isSelect: Boolean = false): BaseViewHolder {
        val iv = getView<ImageView>(viewId)
        iv.isSelected = isSelect
        iv.setImageResource(imageId)
        return this
    }

    fun visibleOrGone(viewId: Int, isVisible: Boolean) {
        if (isVisible) visible(viewId) else gone(viewId)
    }

    fun visibleOrInvisible(viewId: Int, isVisible: Boolean) {
        if (isVisible) visible(viewId) else invisible(viewId)
    }

    fun gone(viewId: Int): BaseViewHolder {
        getView<View>(viewId).gone()
        return this
    }

    fun visible(viewId: Int): BaseViewHolder {
        getView<View>(viewId).visible()
        return this
    }

    fun invisible(viewId: Int): BaseViewHolder {
        getView<View>(viewId).invisible()
        return this
    }

    fun onChildClickListener(
        vararg viewIds: Int,
        call: (v: View, id: Int) -> Unit
    ): BaseViewHolder {
        viewIds.forEach { viewId ->
            val tv = getView<View>(viewId)
            setOnclickNoRepeat(tv) {
                call(it, viewId)
            }
        }
        return this
    }
}
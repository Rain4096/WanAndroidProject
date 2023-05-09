package www.xx.com.base_ui.adapter

import android.view.ViewGroup
import java.util.*

abstract class BaseRecyclerViewAdapter<T> @JvmOverloads constructor(
        private val itemLayoutRes: Int, list: ArrayList<T> = ArrayList()
) : BaseMultiRecyclerViewAdapter<T>(list) {

    override fun addItemViewType(position: Int): Int = TYPE_ITEM

    override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.get(parent, itemLayoutRes)
    }
}
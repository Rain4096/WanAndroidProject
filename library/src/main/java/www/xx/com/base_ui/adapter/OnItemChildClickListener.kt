package www.xx.com.base_ui.adapter

import android.view.View

/**
 * @ClassName: OnItemChildClickListener
 * @Description: item Child 的点击事件
 * @Author: Rain
 * @Version: 1.0
 */
interface OnItemChildClickListener<T> {
    fun onItemClick(view: View, item: T, position: Int)
}

interface OnItemClickListener<T> {
    fun onItemClick(view: View, item: T, position: Int)
}

interface OnItemLongClickListener<T> {
    fun onItemLongClick(view: View, item: T, position: Int)
}

interface OnLoadMoreListener {
    fun onLoadMore()
    fun onRetry()

    open class SimpleOnLoadMoreListener : OnLoadMoreListener {
        override fun onRetry() {
        }

        override fun onLoadMore() {
        }
    }
}
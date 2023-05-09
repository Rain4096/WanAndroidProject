package www.xx.com.base_ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import www.xx.com.R
import www.xx.com.base_ext.setOnclickNoRepeat

abstract class BaseMultiRecyclerViewAdapter<T> @JvmOverloads constructor(
    private val dataSourceList: ArrayList<T> = ArrayList()
) : RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        internal const val TYPE_ITEM = 10000
        private const val TYPE_LOAD_MORE = 10003
        internal const val TYPE_EMPTY = 10004
    }

    private var isOpenLoadMore = false
    private var isLoadingMore = false
    private var isLoadMoreEnd = false
    private var mLoadMoreViewHolder: BaseViewHolder? = null

    private var mEmptyViewHolder: BaseViewHolder? = null

    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private var mOnItemChildClickListener: OnItemChildClickListener<T>? = null
    private var mOnLoadMoreListener: OnLoadMoreListener? = null
    private var showEmptyView: Boolean = true
    private var mEmptyIcon: Int = 0
    private var mEmptyText: String = "暂无数据..."

    fun setOnItemChildClickListener(listener: OnItemChildClickListener<T>) {
        this.mOnItemChildClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>) {
        this.mOnItemLongClickListener = listener
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = listener
        this.isOpenLoadMore = true
    }

    /**
     * 用于保存需要设置点击事件的 item
     */
    private val childClickViewIds = LinkedHashSet<Int>()

    private fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }

    /**
     * 设置需要点击事件的子view
     * @param viewIds IntArray
     */
    fun addChildClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childClickViewIds.add(viewId)
        }
    }

    override fun getItemCount(): Int {
        val size = dataSourceList.size
        return when {
            size > 0 -> {
                var count = 0
                if (isOpenLoadMore) {
                    count++
                }
                count += size
                count
            }
            showEmptyView -> {
                1
            }
            else -> {
                size
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmptyPosition(position)) {
            TYPE_EMPTY
        } else {
            if (isOpenLoadMore && itemCount == position + 1) {
                TYPE_LOAD_MORE
            } else addItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            TYPE_EMPTY -> {
                mEmptyViewHolder = BaseViewHolder.get(parent, R.layout.base_adapter_empty_view)
                return mEmptyViewHolder!!
            }
            TYPE_LOAD_MORE -> {
                mLoadMoreViewHolder = BaseViewHolder.get(parent, R.layout.base_adapter_load_more_view)
                setOnclickNoRepeat(mLoadMoreViewHolder!!.itemView) a@{
                    if (isLoadingMore || isLoadMoreEnd) return@a
                    retry()
                }

                return mLoadMoreViewHolder!!
            }
            else -> {
                val holder = onCreateBaseViewHolder(parent, viewType)
                setOnclickNoRepeat(holder.itemView) { v ->
                    mOnItemClickListener?.let {
                        val position = holder.layoutPosition
                        it.onItemClick(v, dataSourceList[position], position)
                    }
                }
                holder.itemView.setOnLongClickListener { v ->
                    mOnItemLongClickListener?.let {
                        val position = holder.layoutPosition
                        it.onItemLongClick(v, dataSourceList[position], position)
                    }
                    true
                }
                mOnItemChildClickListener?.let {
                    for (id in getChildClickViewIds()) {
                        holder.itemView.findViewById<View>(id)?.let { childView ->
                            if (!childView.isClickable) {
                                childView.isClickable = true
                            }
                            setOnclickNoRepeat(childView) a@{ v ->
                                var position = holder.layoutPosition
                                if (position == RecyclerView.NO_POSITION) {
                                    return@a
                                }
                                it.onItemClick(v, dataSourceList[position], position)
                            }
                            childView.setOnLongClickListener { view ->
                                mOnItemLongClickListener?.let {
                                    val position = holder.layoutPosition
                                    it.onItemLongClick(view, dataSourceList[position], position)
                                }
                                true
                            }
                        }
                    }
                }
                return holder
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_EMPTY -> {
                setEmptyView()
            }
            TYPE_LOAD_MORE -> startLoadMore()
            else -> convert(holder, dataSourceList[position], position)
        }
    }

    private fun setEmptyView() {
        mEmptyViewHolder?.apply {
            setText(R.id.textEmptyTv, mEmptyText)
            setImageSource(R.id.iconEmptyIv, mEmptyIcon)
        }
    }

    /**
     * 判断当前数据是否有数据
     */
    private fun isEmptyPosition(position: Int): Boolean {
        val count = dataSourceList.size
        return position == 0 && showEmptyView && count == 0
    }

    fun setEmptyData(icon: Int, text: String) {
        mEmptyIcon = icon
        mEmptyText = text
    }

    /**
     * 设置空布局显示
     */
    fun showEmptyView(isShow: Boolean) {
        if (isShow != showEmptyView) {
            showEmptyView = isShow
            notifyDataSetChanged()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null && layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isOpenLoadMore && itemCount == position + 1) {
                        layoutManager.spanCount
                    } else 1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            val position = holder.adapterPosition
            if (isOpenLoadMore && itemCount == position + 1) {
                lp.isFullSpan = true
            }
        }
    }

    fun getItem(position: Int): T = dataSourceList[position]

    fun getAllItem(): List<T> = dataSourceList

    fun cleanItem() {
        dataSourceList.clear()
        notifyDataSetChanged()
    }

    fun setNewItem(list: Collection<T>) {
        cleanItem()
        if (list.isNotEmpty()) {
            dataSourceList.addAll(list)
        }
        notifyItemRangeInserted(0, dataSourceList.size)
        // 所有数据加载完成 = false
        isLoadMoreEnd = false
        isLoadingMore = false
    }

    fun setItem(list: Collection<T>) {
        if (list.isEmpty()) {
            loadMoreEnd()
        } else {
            dataSourceList.addAll(list)
            notifyItemRangeChanged(itemCount, list.size)
        }
        isLoadingMore = false
    }

    fun setItem(t: T) {
        dataSourceList.add(t)
        notifyItemRangeChanged(itemCount, 1)
    }

    private fun startLoadMore() {
        if (!isOpenLoadMore || mOnLoadMoreListener == null || dataSourceList.isEmpty() || isLoadingMore
            || isLoadMoreEnd
        ) return
        loadMoreLoading()
        mOnLoadMoreListener!!.onLoadMore()
    }

    private fun retry() {
        if (!isOpenLoadMore || mOnLoadMoreListener == null || dataSourceList.isEmpty() || isLoadingMore
            || isLoadMoreEnd
        ) return
        loadMoreLoading()
        mOnLoadMoreListener!!.onRetry()
    }

    private fun loadMoreLoading() {
        isLoadingMore = true
        loadMoreStatus(true, "正在加载...")
    }

    @JvmOverloads
    fun loadMoreFail(message: String = "加载失败，点击重试") {
        isLoadingMore = false
        loadMoreStatus(false, message)
    }

    @JvmOverloads
    fun loadMoreEnd(message: String = "没有更多了 (=・ω・=)") {
        isLoadingMore = false
        isLoadMoreEnd = true
        loadMoreStatus(false, message)
    }

    private fun loadMoreStatus(isVisible: Boolean, message: String) {
        mLoadMoreViewHolder?.let {
            if (it.itemView is ViewGroup) {
                val viewGroup = it.itemView as ViewGroup
                for (i in 0 until viewGroup.childCount) {
                    val childView = viewGroup.getChildAt(i)
                    if (childView is ProgressBar) {
                        childView.setVisibility(if (isVisible) View.VISIBLE else View.GONE)
                    } else if (childView is TextView) {
                        childView.text = message
                    }
                }
            }
        }
    }

    abstract fun addItemViewType(position: Int): Int

    abstract fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder

    abstract fun convert(holder: BaseViewHolder, item: T, position: Int)

    /**添加单个数据[data]并刷新显示*/
    fun addSingleData(data: T) {
        dataSourceList.add(data)
        notifyItemRangeInserted(dataSourceList.indexOf(data), 1)
    }

    fun deleteData(data: T) {
        notifyItemRemoved(dataSourceList.indexOf(data))
        dataSourceList.remove(data)
    }

    /**添加多个数据[data]并刷新显示*/
    fun loadMore(data: List<T>) {
        val curIndex = dataSourceList.size - data.size
        dataSourceList.addAll(data)
        notifyItemRangeInserted(curIndex, data.size)
    }


    /**修改位于[position]数据[data]并刷新显示*/
    fun singleDataChange(data: T, position: Int) {
        dataSourceList.removeAt(position)
        dataSourceList.add(position, data)
        notifyItemChanged(position)
    }

    /**删除位于[position]数据并刷新显示*/
    fun deleteData(position: Int) {
        dataSourceList.removeAt(position)
        notifyItemRemoved(position)
    }
}
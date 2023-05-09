package www.xx.com.base_ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import www.xx.com.R
import www.xx.com.base_ext.gone

class FooterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            context.resources.getDimensionPixelOffset(R.dimen.dimen_40)
        )
        layoutParams = params

        val progress = ProgressBar(context)
        val size = context.resources.getDimensionPixelOffset(R.dimen.dimen_24)
        progress.apply {
            val proPar = LayoutParams(size,size)
            layoutParams = proPar
            gone()
        }
        addView(progress)

        val remind = TextView(context)
        remind.apply {
            val remPar = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams = remPar
        }
        addView(remind)
    }
}
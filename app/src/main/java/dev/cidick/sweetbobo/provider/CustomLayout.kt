package dev.cidick.sweetbobo.provider

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class CustomLayout(context: Context?) : LinearLayoutManager(context) {
    private var isScrollEnabled = true
    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}

package com.jingtian.mtdemo.UI.Refresh

enum class RefreshMode {
    DISABLE, PULL_FROM_START, PULL_FROM_END, BOTH;
    companion object {
        fun getDefault(): RefreshMode {
            return DISABLE
        }
    }

    fun permitsPulltoRefresh() = this != DISABLE
    fun permitsPullFromStart() = (this == BOTH) || (this == PULL_FROM_START)
    fun permitsPullFromEnd() = (this == BOTH) || (this == PULL_FROM_END)
}
package com.algolia.instantsearch.helper.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

public fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

public inline fun <reified T> ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): T {
    return inflate(layoutId, attachToRoot) as T
}

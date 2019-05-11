package com.algolia.instantsearch.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel

inline val AppCompatActivity.app get() = applicationContext as App
inline val AndroidViewModel.app get() = getApplication<App>()

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun <reified T> ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): T {
    return inflate(layoutId, attachToRoot) as T
}
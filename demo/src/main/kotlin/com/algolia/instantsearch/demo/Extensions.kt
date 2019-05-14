package com.algolia.instantsearch.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline val AppCompatActivity.app get() = applicationContext as App
inline val AndroidViewModel.app get() = getApplication<App>()

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun <reified T> ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): T {
    return inflate(layoutId, attachToRoot) as T
}

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline completion: (T?) -> Unit) {
    observe(owner, Observer { data -> completion(data) })
}

inline fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, crossinline completion: (T) -> Unit) {
    observe(owner, Observer { data -> if (data != null) completion(data) })
}
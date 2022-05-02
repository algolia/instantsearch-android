package com.algolia.client.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import java.io.Closeable

private const val JobKey = "com.algolia.instantsearch.android.lifecycle.ViewModelLifecycleOwner.JOB_KEY"

public val ViewModel.viewModelLifecycleOwner: LifecycleOwner
    get() {
        val viewModelClass = findViewModelClass()
        return viewModelClass.getTag(JobKey) ?: viewModelClass.setTagIfAbsent(JobKey, CloseableLifecycleOwner())
    }

private fun ViewModel.findViewModelClass(): Class<in ViewModel> {
    var superclass = this.javaClass.superclass
    while (superclass != null && superclass.name.toString() != "androidx.lifecycle.ViewModel") {
        superclass = superclass.superclass
    }
    return superclass ?: throw ClassNotFoundException("Couldn't find ViewModel class from super classes")
}

private fun Class<in ViewModel>.getTag(tag: String): LifecycleOwner? =
    getDeclaredMethod("getTag", String::class.java).run {
        isAccessible = true
        invoke(this, tag) as? CloseableLifecycleOwner
    }

private fun Class<in ViewModel>.setTagIfAbsent(tag: String, obj: Any): CloseableLifecycleOwner =
    getDeclaredMethod("setTagIfAbsent", String::class.java, Any::class.java).run {
        isAccessible = true
        invoke(this, tag, obj) as CloseableLifecycleOwner
    }

internal class CloseableLifecycleOwner : LifecycleOwner, Closeable {

    private val registry = LifecycleRegistry(this)

    init {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun close() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    override fun getLifecycle(): Lifecycle = registry
}

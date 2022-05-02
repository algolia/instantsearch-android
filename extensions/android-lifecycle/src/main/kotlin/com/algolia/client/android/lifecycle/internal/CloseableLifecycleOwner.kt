package com.algolia.client.android.lifecycle.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import java.io.Closeable

private const val JOB_KEY = "com.algolia.instantsearch.android.LifecycleOwner.JOB_KEY"

/**
 * [LifecycleOwner] tied to this [ViewModel].
 */
internal val ViewModel.lifecycleOwner: LifecycleOwner
    get() = getTag(JOB_KEY) ?: setTagIfAbsent(JOB_KEY, CloseableLifecycleOwner())

private fun ViewModel.findViewModelClass(): Class<in ViewModel> {
    var superclass = javaClass.superclass
    while (superclass != ViewModel::class.java) {
        superclass = superclass.superclass
    }
    return superclass
}

private fun ViewModel.getTag(tag: String): LifecycleOwner? =
    findViewModelClass().getDeclaredMethod("getTag", String::class.java).run {
        isAccessible = true
        invoke(this@getTag, tag) as? CloseableLifecycleOwner
    }

private fun ViewModel.setTagIfAbsent(tag: String, obj: Any): CloseableLifecycleOwner =
    findViewModelClass().getDeclaredMethod("setTagIfAbsent", String::class.java, Any::class.java).run {
        isAccessible = true
        invoke(this@setTagIfAbsent, tag, obj) as CloseableLifecycleOwner
    }

private class CloseableLifecycleOwner : LifecycleOwner, Closeable {

    private val registry = LifecycleRegistry(this)

    init {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun close() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    override fun getLifecycle(): Lifecycle = registry
}

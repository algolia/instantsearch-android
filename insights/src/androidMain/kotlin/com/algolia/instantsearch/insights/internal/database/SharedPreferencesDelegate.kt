package com.algolia.instantsearch.insights.internal.database

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


internal sealed class SharedPreferencesDelegate<T>(
    protected val default: T,
    protected val key: kotlin.String? = null
) : ReadWriteProperty<SharedPreferences, T> {

    class Int(default: kotlin.Int, key: kotlin.String? = null) : SharedPreferencesDelegate<kotlin.Int>(default, key) {

        override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): kotlin.Int {
            return thisRef.getInt(key ?: property.name, default)
        }

        override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: kotlin.Int) {
            thisRef.edit().putInt(key ?: property.name, value).apply()
        }
    }

    class String(default: kotlin.String? = null, key: kotlin.String? = null) : SharedPreferencesDelegate<kotlin.String?>(default, key) {

        override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): kotlin.String? {
            return thisRef.getString(key ?: property.name, default)
        }

        override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: kotlin.String?) {
            thisRef.edit().putString(key ?: property.name, value).apply()
        }
    }

    class StringSet(default: Set<kotlin.String>, key: kotlin.String? = null) : SharedPreferencesDelegate<Set<kotlin.String>>(default, key) {

        override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): Set<kotlin.String> {
            return thisRef.getStringSet(key ?: property.name, default) ?: setOf()
        }

        override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Set<kotlin.String>) {
            thisRef.edit().putStringSet(key ?: property.name, value).apply()
        }
    }
}

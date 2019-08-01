package com.algolia.instantsearch.demo

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel

inline val AppCompatActivity.app get() = applicationContext as App
inline val AndroidViewModel.app get() = getApplication<App>()
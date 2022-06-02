package com.algolia.instantsearch.benchmark

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

fun UiDevice.type(into: UiObject2, input: String) {
    var seq = ""
    for (char in input) {
        seq += char
        into.text = seq
        wait(1000)
    }
}

fun UiDevice.wait(timeout: Long) {
    wait(Until.hasObject(By.desc("I do not exist!")), timeout)
}

fun UiDevice.waitObject(bySelector: BySelector, timeout: Long = 500): UiObject2 {
    wait(Until.hasObject(bySelector), timeout)
    return findObject(bySelector)
}

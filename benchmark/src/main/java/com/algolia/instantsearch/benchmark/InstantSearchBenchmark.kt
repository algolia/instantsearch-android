package com.algolia.instantsearch.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@OptIn(ExperimentalBaselineProfilesApi::class)
@RunWith(AndroidJUnit4::class)
class InstantSearchBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    val appPackageName = "com.algolia.instantsearch.examples.android"

    @Test
    fun startup() {
        benchmarkRule.measureRepeated(
            packageName = appPackageName,
            metrics = listOf(StartupTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.COLD
        ) {
            pressHome()
            startActivityAndWait()

            val recyclerHasChild = By.hasChild(By.res(packageName, "list"))
            val hasObject = Until.hasObject(recyclerHasChild)
            device.wait(hasObject, 5_000)
        }
    }

    @Test
    fun baseline() =
        baselineProfileRule.collectBaselineProfile(packageName = appPackageName) {
            pressHome()
            startActivityAndWait()

            // go to declarative ui
            device.waitObject(By.desc("guide_declarative_ui")).click()
            device.waitForIdle()

            // type into search bar
            val searchBox = device.waitObject(By.desc("search box"))
            searchBox.click()
            val textField = searchBox.children.first()
            device.type(textField, "phone")
            device.pressBack()
            device.waitForIdle()

            // open filters
            device.waitObject(By.desc("filters list")).click()
            device.waitForIdle()

            // apply filter
            device.waitObject(By.text("Cell Phones")).click()
            device.findObject(By.text("Cell Phone Accessories")).click()
            device.waitForIdle()
        }
}

package com.algolia.instantsearch.benchmark

import androidx.activity.ComponentActivity
import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearcherBenchmark {

    @get:Rule
    val rule = SimpleAndroidBenchmarkRule()

    @Test
    fun startup() {
        rule.activityTestRule.runOnUiThread {
            val i = 1 + 1
            println(i)
        }
    }

    class SimpleAndroidBenchmarkRule() : TestRule {
        @Suppress("DEPRECATION")
        val activityTestRule = androidx.test.rule.ActivityTestRule(ComponentActivity::class.java)

        val benchmarkRule = BenchmarkRule()

        override fun apply(base: Statement, description: Description): Statement {
            return RuleChain.outerRule(benchmarkRule)
                .around(activityTestRule)
                .apply(base, description)
        }
    }
}

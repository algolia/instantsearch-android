package com.algolia.instantsearch.benchmark.utils

import androidx.activity.ComponentActivity
import androidx.benchmark.junit4.BenchmarkRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class AndroidBenchmarkRule : TestRule {
    @Suppress("DEPRECATION")
    val activityTestRule = androidx.test.rule.ActivityTestRule(ComponentActivity::class.java)

    val benchmarkRule = BenchmarkRule()

    override fun apply(base: Statement, description: Description): Statement {
        return RuleChain.outerRule(benchmarkRule)
            .around(activityTestRule)
            .apply(base, description)
    }
}

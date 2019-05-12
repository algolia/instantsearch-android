package filter

import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test


class TestFilterPresenter {

    private val attribute = Attribute("color")

    @Test
    fun facet() {
        val string = Filter.Facet(attribute, "value")
        val integer = Filter.Facet(attribute, 0)
        val boolean = Filter.Facet(attribute, true)

        FilterPresenter(string) shouldEqual "value"
        FilterPresenter(integer) shouldEqual "color: 0"
        FilterPresenter(boolean) shouldEqual "color"
    }

    @Test
    fun tag() {
        val filter = Filter.Tag("value")

        FilterPresenter(filter) shouldEqual "value"
    }

    @Test
    fun numeric() {
        val range = Filter.Numeric(attribute, 0 until 10)
        val comparison = Filter.Numeric(attribute, NumericOperator.Greater, 1f)

        FilterPresenter(range) shouldEqual "color: 0 to 9"
        FilterPresenter(comparison) shouldEqual "color > 1.0"
    }
}
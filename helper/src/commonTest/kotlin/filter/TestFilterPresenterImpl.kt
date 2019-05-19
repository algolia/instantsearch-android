package filter

import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test


class TestFilterPresenterImpl {

    private val attribute = Attribute("color")

    @Test
    fun facet() {
        val string = Filter.Facet(attribute, "value")
        val integer = Filter.Facet(attribute, 0)
        val boolean = Filter.Facet(attribute, true)

        FilterPresenterImpl(string) shouldEqual "value"
        FilterPresenterImpl(integer) shouldEqual "color: 0"
        FilterPresenterImpl(boolean) shouldEqual "color"
    }

    @Test
    fun tag() {
        val filter = Filter.Tag("value")

        FilterPresenterImpl(filter) shouldEqual "value"
    }

    @Test
    fun numeric() {
        val range = Filter.Numeric(attribute, 0 until 10)
        val comparison = Filter.Numeric(attribute, NumericOperator.Greater, 1f)

        FilterPresenterImpl(range) shouldEqual "color: 0 to 9"
        FilterPresenterImpl(comparison) shouldEqual "color > 1.0"
    }
}
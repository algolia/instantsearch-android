package filter

import com.algolia.instantsearch.filter.DefaultFilterPresenter
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test

class TestFilterPresenterImpl {

    private val attribute = Attribute("color")

    @Test
    fun facet() {
        val presenter = DefaultFilterPresenter()
        val string = Filter.Facet(attribute, "value")
        val integer = Filter.Facet(attribute, 0)
        val boolean = Filter.Facet(attribute, true)

        presenter(string) shouldEqual "value"
        presenter(integer) shouldEqual "color: 0"
        presenter(boolean) shouldEqual "color"
    }

    @Test
    fun tag() {
        val presenter = DefaultFilterPresenter()
        val filter = Filter.Tag("value")

        presenter(filter) shouldEqual "value"
    }

    @Test
    fun numeric() {
        val presenter = DefaultFilterPresenter()
        val range = Filter.Numeric(attribute, 0 until 10)
        val comparison = Filter.Numeric(attribute, NumericOperator.Greater, 1f)

        presenter(range) shouldEqual "color: 0 to 9"
        presenter(comparison) shouldEqual "color > 1.0"
    }
}

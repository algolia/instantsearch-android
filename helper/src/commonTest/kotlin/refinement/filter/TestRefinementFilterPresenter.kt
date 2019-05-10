package refinement.filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test


class TestRefinementFilterPresenter {

    private val attribute = Attribute("color")

    @Test
    fun facet() {
        val string = Filter.Facet(attribute, "value")
        val integer = Filter.Facet(attribute, 0)
        val boolean = Filter.Facet(attribute, true)

        RefinementFilterPresenter(string) shouldEqual "value"
        RefinementFilterPresenter(integer) shouldEqual "color: 0"
        RefinementFilterPresenter(boolean) shouldEqual "color"
    }

    @Test
    fun tag() {
        val filter = Filter.Tag("value")

        RefinementFilterPresenter(filter) shouldEqual "value"
    }

    @Test
    fun numeric() {
        val range = Filter.Numeric(attribute, 0 until 10)
        val comparison = Filter.Numeric(attribute, NumericOperator.Greater, 1f)

        RefinementFilterPresenter(range) shouldEqual "color: 0 to 9"
        RefinementFilterPresenter(comparison) shouldEqual "color > 1.0"
    }
}
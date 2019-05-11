package selectable.filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test


class TestSelectableFilterPresenter {

    private val attribute = Attribute("color")

    @Test
    fun facet() {
        val string = Filter.Facet(attribute, "value")
        val integer = Filter.Facet(attribute, 0)
        val boolean = Filter.Facet(attribute, true)

        SelectableFilterPresenter(string) shouldEqual "value"
        SelectableFilterPresenter(integer) shouldEqual "color: 0"
        SelectableFilterPresenter(boolean) shouldEqual "color"
    }

    @Test
    fun tag() {
        val filter = Filter.Tag("value")

        SelectableFilterPresenter(filter) shouldEqual "value"
    }

    @Test
    fun numeric() {
        val range = Filter.Numeric(attribute, 0 until 10)
        val comparison = Filter.Numeric(attribute, NumericOperator.Greater, 1f)

        SelectableFilterPresenter(range) shouldEqual "color: 0 to 9"
        SelectableFilterPresenter(comparison) shouldEqual "color > 1.0"
    }
}
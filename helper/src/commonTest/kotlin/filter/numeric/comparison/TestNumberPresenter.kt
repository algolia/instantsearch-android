package filter.numeric.comparison

import com.algolia.instantsearch.helper.filter.numeric.comparison.NumberPresenterImpl
import shouldEqual
import kotlin.test.Test


class TestNumberPresenter {

    @Test
    fun isNull() {
        NumberPresenterImpl(null) shouldEqual "-"
    }

    @Test
    fun other() {
        NumberPresenterImpl(0) shouldEqual "0"
    }
}
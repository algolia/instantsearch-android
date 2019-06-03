package number

import com.algolia.instantsearch.core.number.NumberPresenterImpl
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
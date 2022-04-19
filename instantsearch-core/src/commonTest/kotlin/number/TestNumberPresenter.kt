package number

import com.algolia.instantsearch.core.number.DefaultNumberPresenter
import shouldEqual
import kotlin.test.Test

class TestNumberPresenter {

    @Test
    fun isNull() {
        DefaultNumberPresenter(null) shouldEqual "-"
    }

    @Test
    fun other() {
        DefaultNumberPresenter(0) shouldEqual "0"
    }
}

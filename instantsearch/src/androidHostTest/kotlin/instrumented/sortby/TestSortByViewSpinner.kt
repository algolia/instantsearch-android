package instrumented.sortby

import android.R
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.android.sortby.SortByViewSpinner
import instrumented.applicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestSortByViewSpinner {

    private val elements = listOf("A", "B", "C")

    private fun view(defaultSelection: Int): SortByViewSpinner {
        val adapter = ArrayAdapter<String>(applicationContext, R.layout.simple_list_item_1).apply {
            addAll(elements)
        }

        return SortByViewSpinner(Spinner(applicationContext), adapter, defaultSelection)
    }

    @Test
    fun defaultSelectionShouldUpdateSelectedItem() {
        val view = view(1)

        view.spinner.selectedItemPosition shouldEqual view.defaultSelection
    }

    @Test
    fun setSelectionShouldUpdateSelectedItemPosition() {
        val view = view(0)
        val selected = 1

        view.spinner.selectedItemPosition shouldEqual view.defaultSelection
        view.setSelected(selected)
        view.spinner.selectedItemPosition shouldEqual selected
    }

    @Test
    fun setSelectionShouldFallbackOnDefaultSelection() {
        val view = view(0)
        val selected = 1

        view.setSelected(selected)
        view.spinner.selectedItemPosition shouldEqual selected
        view.setSelected(null)
        view.spinner.selectedItemPosition shouldEqual view.defaultSelection
    }

    @Test
    fun setItemShouldUpdateAdapter() {
        val view = view(0)

        view.setMap(mapOf(0 to "D"))
        view.adapter.count shouldEqual 1
        view.adapter.getItem(0) shouldEqual "D"
    }

    @Test
    fun onItemSelectedShouldCallOnClick() {
        val view = view(0)
        var onClickCalled = false

        view.onSelectionChange = { onClickCalled = true }
        view.setSelected(1)
        onClickCalled shouldEqual true
    }
}

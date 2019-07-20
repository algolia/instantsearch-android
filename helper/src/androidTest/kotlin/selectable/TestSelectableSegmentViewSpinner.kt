package selectable

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.helper.android.index.IndexSegmentViewSpinner
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSelectableSegmentViewSpinner {

    private val elements = listOf("A", "B", "C")

    private fun view(defaultSelection: Int): IndexSegmentViewSpinner {
        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1).apply {
            addAll(elements)
        }

        return IndexSegmentViewSpinner(Spinner(applicationContext), adapter, defaultSelection)
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
package selectable

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.helper.android.filter.FilterSegmentViewRadioGroup
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
class TestSelectableSegmentViewRadioGroup {

    private fun view(): FilterSegmentViewRadioGroup {
        return FilterSegmentViewRadioGroup(
            RadioGroup(applicationContext).apply {
                addView(RadioButton(applicationContext))
            }
        )
    }

    @Test
    fun callSetIsSelectedShouldUpdateIsChecked() {
        val view = view()

        view.setSelected(1)
        view.radioGroup.checkedRadioButtonId shouldEqual 1
    }

    @Test
    fun callSetItemShouldUpdateChildren() {
        val view = view()
        val text = "text"
        val child = view.radioGroup.getChildAt(0) as RadioButton

        view.setMap(mapOf(child.id to text))
        child.text shouldEqual text
    }

    @Test
    fun callSetSelectedShouldNotCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onSelectionChange = { onClickCalled = true }
        view.setSelected(1)
        onClickCalled shouldEqual false
    }

    @Test
    fun onCheckedChangeShouldCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onSelectionChange = { onClickCalled = true }
        view.radioGroup.check(1)
        onClickCalled shouldEqual true
    }
}
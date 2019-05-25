package selectable

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.helper.android.selectable.SelectableRadioGroup
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSelectableRadioGroup {

    private fun view(): SelectableRadioGroup {
        val context = ApplicationProvider.getApplicationContext<Context>()

        return SelectableRadioGroup(
            RadioGroup(context).apply {
                addView(RadioButton(context))
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
    fun callSetItemsShouldUpdateChildren() {
        val view = view()
        val text = "text"
        val child = view.radioGroup.getChildAt(0) as RadioButton

        view.setItems(mapOf(child.id to text))
        child.text shouldEqual text
    }

    @Test
    fun callSetSelectedShouldNotCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onClick = { onClickCalled = true }
        view.setSelected(1)
        onClickCalled shouldEqual false
    }

    @Test
    fun onCheckedChangeShouldCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onClick = { onClickCalled = true }
        view.radioGroup.check(1)
        onClickCalled shouldEqual true
    }
}
package selectable

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSelectableRadioGroup {

    private fun widget(): SelectableRadioGroup {
        val context = ApplicationProvider.getApplicationContext<Context>()

        return SelectableRadioGroup(
            RadioGroup(context).apply {
                addView(RadioButton(context))
            }
        )
    }

    @Test
    fun callSetIsSelectedShouldUpdateIsChecked() {
        val widget = widget()

        widget.setSelected(1)
        widget.view.checkedRadioButtonId shouldEqual 1
    }

    @Test
    fun callSetItemsShouldUpdateChildren() {
        val widget = widget()
        val text = "text"
        val child = widget.view.getChildAt(0) as RadioButton

        widget.setItems(mapOf(child.id to text))
        child.text shouldEqual text
    }

    @Test
    fun callSetSelectedShouldNotCallOnClick() {
        val widget = widget()
        var onClickCalled = false

        widget.onClick = { onClickCalled = true }
        widget.setSelected(1)
        onClickCalled shouldEqual false
    }

    @Test
    fun onCheckedChangeShouldCallOnClick() {
        val widget = widget()
        var onClickCalled = false

        widget.onClick = { onClickCalled = true }
        widget.view.check(1)
        onClickCalled shouldEqual true
    }
}
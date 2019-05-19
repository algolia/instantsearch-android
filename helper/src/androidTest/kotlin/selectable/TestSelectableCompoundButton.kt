package selectable

import android.widget.Switch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.helper.android.selectable.SelectableCompoundButton
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class TestSelectableCompoundButton  {

    private fun widget() =
        SelectableCompoundButton(Switch(ApplicationProvider.getApplicationContext()))

    @Test
    fun callSetIsSelectedShouldUpdateIsChecked() {
        val widget = widget()

        widget.setIsSelected(true)
        widget.view.isChecked shouldEqual true
    }

    @Test
    fun callSetTextShouldUpdateText() {
        val widget = widget()

        widget.setText("text")
        widget.view.text shouldEqual "text"
    }

    @Test
    fun callSetIsSelectedShouldNotCallOnClick() {
        val widget = widget()
        var onClickCalled = false

        widget.onClick = { onClickCalled = true }
        widget.setIsSelected(true)
        onClickCalled shouldEqual false
    }

    @Test
    fun onCheckedChangeShouldCallOnClick() {
        val widget = widget()
        var onClickCalled = false

        widget.onClick = { onClickCalled = true }
        widget.view.isChecked = true
        onClickCalled shouldEqual true
    }
}
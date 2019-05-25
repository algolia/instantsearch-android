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

    private fun view() =
        SelectableCompoundButton(Switch(ApplicationProvider.getApplicationContext()))

    @Test
    fun callSetIsSelectedShouldUpdateIsChecked() {
        val view = view()

        view.setIsSelected(true)
        view.compoundButton.isChecked shouldEqual true
    }

    @Test
    fun callSetTextShouldUpdateText() {
        val view = view()

        view.setText("text")
        view.compoundButton.text shouldEqual "text"
    }

    @Test
    fun callSetIsSelectedShouldNotCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onClick = { onClickCalled = true }
        view.setIsSelected(true)
        onClickCalled shouldEqual false
    }

    @Test
    fun onCheckedChangeShouldCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onClick = { onClickCalled = true }
        view.compoundButton.isChecked = true
        onClickCalled shouldEqual true
    }
}
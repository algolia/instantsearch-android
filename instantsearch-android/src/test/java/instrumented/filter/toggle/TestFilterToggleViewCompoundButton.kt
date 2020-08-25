package instrumented.filter.toggle

import android.os.Build
import android.widget.Switch
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import instrumented.applicationContext
import com.algolia.instantsearch.helper.android.filter.toggle.FilterToggleViewCompoundButton
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestFilterToggleViewCompoundButton {

    private fun view() =
        FilterToggleViewCompoundButton(Switch(applicationContext))

    @Test
    fun callSetIsSelectedShouldUpdateIsChecked() {
        val view = view()

        view.setIsSelected(true)
        view.compoundButton.isChecked shouldEqual true
    }

    @Test
    fun callSetItemShouldUpdateText() {
        val view = view()

        view.setItem("text")
        view.compoundButton.text shouldEqual "text"
    }

    @Test
    fun callSetIsSelectedShouldNotCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onSelectionChanged = { onClickCalled = true }
        view.setIsSelected(true)
        onClickCalled shouldEqual false
    }

    @Test
    fun onCheckedChangeShouldCallOnClick() {
        val view = view()
        var onClickCalled = false

        view.onSelectionChanged = { onClickCalled = true }
        view.compoundButton.isChecked = true
        onClickCalled shouldEqual true
    }
}

package instrumented

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.algolia.instantsearch.helper.android.R


val applicationContext: Application get() = ApplicationProvider.getApplicationContext<Application>().apply {
    setTheme(R.style.Theme_MaterialComponents)
}

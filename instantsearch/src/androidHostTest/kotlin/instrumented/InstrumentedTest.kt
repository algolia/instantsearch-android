package instrumented

import android.app.Application
import androidx.test.core.app.ApplicationProvider

val applicationContext: Application
    get() = ApplicationProvider.getApplicationContext<Application>().apply {
        setTheme(com.google.android.material.R.style.Theme_MaterialComponents)
    }

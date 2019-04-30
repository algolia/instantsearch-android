package logging

import android.util.Log
import android.widget.Toast

actual fun debugLog(msg: String) {
    Log.e("com.algolia.instantsearch", msg)
}
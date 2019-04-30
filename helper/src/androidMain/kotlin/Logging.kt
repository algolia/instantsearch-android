package logging

import android.util.Log
import android.widget.Toast

//FIXME Remove
actual fun debugLog(msg: String) {
    Log.e("com.algolia.instantsearch", msg)
}
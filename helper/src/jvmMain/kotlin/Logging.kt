package logging

actual fun debugLog(msg: String) {
    System.err.println(msg)
}
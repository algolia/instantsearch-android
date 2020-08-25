import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass
import kotlin.test.*


internal infix fun <T> T.shouldEqual(expected: T) {
    assertEquals(expected, this)
}

internal infix fun <T> T.shouldNotEqual(expected: T) {
    assertNotEquals(expected, this)
}

internal fun Any?.shouldBeNull() {
    assertNull(this)
}

internal fun Any?.shouldNotBeNull() {
    assertNotNull(this)
}

internal fun Boolean.shouldBeTrue() {
    assertTrue(this)
}

internal fun Boolean.shouldBeFalse() {
    assertFalse(this)
}

internal infix fun <T> Collection<T>.shouldEqual(expected: Collection<T>) {
    assertEquals(expected, this)
}

internal infix fun <T> Collection<T>.shouldNotEqual(expected: Collection<T>) {
    assertNotEquals(expected, this)
}

internal fun <T> Collection<T>.shouldBeEmpty() {
    this.isEmpty().shouldBeTrue()
}

internal fun <T> Collection<T>.shouldNotBeEmpty() {
    this.isNotEmpty().shouldBeTrue()
}

internal fun <K, V> Map<K, V>.shouldBeEmpty() {
    this.isEmpty().shouldBeTrue()
}

internal fun <K, V> Map<K, V>.shouldNotBeEmpty() {
    this.isNotEmpty().shouldBeTrue()
}

internal infix fun <T : Throwable> KClass<T>.shouldFailWith(block: suspend () -> Unit): T {
    return assertFailsWith(this, null) {
        blocking {
            block()
        }
    }
}

internal fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}

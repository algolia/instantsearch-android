import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

infix fun <T> T.shouldEqual(expected: T) {
    assertEquals(expected, this)
}

infix fun <T> T.shouldNotEqual(expected: T) {
    assertNotEquals(expected, this)
}

fun Any?.shouldBeNull() {
    assertNull(this)
}

fun Any?.shouldNotBeNull() {
    assertNotNull(this)
}

fun Boolean.shouldBeTrue() {
    assertTrue(this)
}

fun Boolean.shouldBeFalse() {
    assertFalse(this)
}

infix fun <T> Collection<T>.shouldEqual(expected: Collection<T>) {
    assertEquals(expected, this)
}

infix fun <T> Collection<T>.shouldNotEqual(expected: Collection<T>) {
    assertNotEquals(expected, this)
}

fun <T> Collection<T>.shouldBeEmpty() {
    this.isEmpty().shouldBeTrue()
}

fun <T> Collection<T>.shouldNotBeEmpty() {
    this.isNotEmpty().shouldBeTrue()
}

fun <K, V> Map<K, V>.shouldBeEmpty() {
    this.isEmpty().shouldBeTrue()
}

fun <K, V> Map<K, V>.shouldNotBeEmpty() {
    this.isNotEmpty().shouldBeTrue()
}

infix fun <T : Throwable> KClass<T>.shouldFailWith(block: suspend () -> Unit): T {
    return assertFailsWith(this, null) {
        blocking {
            block()
        }
    }
}

fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}

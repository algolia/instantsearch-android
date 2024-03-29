import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

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

fun TestScope.setUnconfinedMain() {
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    Dispatchers.setMain(testDispatcher)
}

import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

internal infix fun <T> Collection<T>.shouldEqual(collection: Collection<T>) {
    assertEquals(collection, this)
}

internal infix fun <T> Collection<T>.shouldNotEqual(collection: Collection<T>) {
    assertNotEquals(collection, this)
}

internal fun <T> Collection<T>.shouldBeEmpty() {
    this.isEmpty().shouldBeTrue()
}

internal fun <T> Collection<T>.shouldNotBeEmpty() {
    this.isNotEmpty().shouldBeTrue()
}

internal infix fun <T> Collection<T>.shouldContain(element: T) {
    this.contains(element)
}

internal infix fun <T : Throwable> KClass<T>.shouldFailWith(block: suspend () -> Unit): T {
    return assertFailsWith(this, null) {
        blocking {
            block()
        }
    }
}

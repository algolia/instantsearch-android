# Guidelines for Great Java DX

## Default parameters

### Constructors
Any constructor with default value(s) MUST have an explicit `constructor` with `@JvmOverloads` to make the default(s) available to Java users.

```kotlin
public data class User(val name: String, val admin: Boolean = false) // BAD!
public data class User @JvmOverloads constructor(
    val words: String, 
    val admin: Boolean = false
) // Good
```

### Methods in the public API
Any exposed method with default value(s) MUST have a `@JvmOverloads` to make the default(s) available to Java users.
```kotlin
public fun print(text: String, withNewline: Boolean = true) // BAD!
@JvmOverloads public fun print(text: String, withNewline: Boolean = true) // Good
```


## Operator overloading 

Any overloaded operator like `plusAssign`, `invoke`, etc. SHOULD 
be:

- hidden from Java, using `@JvmSynthetic`
- made available as a regular method, with a meaningful name, for Java users


```kotlin
public class Processor: (String) -> Int {
    override fun invoke(p1: String): Int = p1.length
} // BAD!

public class Processor: (String) -> Int {
    @JvmSynthetic 
    override fun invoke(p1: String): Int = count(p1)
    
    public fun count(input: String): Int = input.length
} // Good
```

## Method overloading
### TODO: Might actually be bad API for Kotlin DX too, update guidelines once a good design for both is found
When a method is exposed with several overloads having default parameter values, marking them with `@JvmOverloads` to expose appropriate overloads to Java users might trigger a compile error:
```
FacetListConnection.kt: (16, 1): Platform declaration clash: The following declarations have the same JVM signature (connectFilterState(Lcom/algolia/instantsearch/helper/filter/facet/FacetListViewModel;Lcom/algolia/instantsearch/helper/filter/state/FilterState;Lcom/algolia/search/model/Attribute;)Lcom/algolia/instantsearch/core/connection/Connection;):
    fun FacetListViewModel.connectFilterState(filterState: FilterState, attribute: Attribute, groupID: FilterGroupID = ...): Connection defined in com.algolia.instantsearch.helper.filter.facet in file FacetListConnection.kt
    fun FacetListViewModel.connectFilterState(filterState: FilterState, attribute: Attribute, operator: FilterOperator = ...): Connection defined in com.algolia.instantsearch.helper.filter.facet in file FacetListConnection.kt
```

To avoid this issue, choose which method should have a `@JvmOverloads` : the main use-case should be made simpler, while it is acceptable to require all parameters on alternatives:
```kotlin
@JvmOverloads public fun createUser(name: String, isAdult: Boolean  = true) {/*...*/}
@JvmOverloads public fun createUser(name: String, age: Int = 0) {/*...*/} // BAD! The two overloads will clash, preventing compilation
@JvmOverloads 
public fun createUser(name: String, isAdult: Boolean  = true) {/*...*/}
public fun createUser(name: String, age: Int = 0) {/*...*/} // Good
```
## Internal members
Any object with an internal member MUST mark its getter as `@JvmSynthetic`, to ensure the getter doesn't show up in Java:

```kotlin
public class ContactBook(internal val friends: List<String>) 
// BAD! Java users would see book.getFriends$core_commonMain

public class ContactBook(val friends: List<String>) {
    internal val friends = friends
        @JvmSynthetic get 
} // Good
```

## Constants
Any file containing constants MUST be given a meaningful `@file:JvmName`. Its constants MUST all be marked as const to expose `File.constant` instead of requiring Java users to call `File.getConstant()`: 

```kotlin
public val fooBarInMillis = 200L //BAD! Java users would see File.getFooBarInMillis()

public const val fooBarInMillis = 200L // Good
```

## Fields
Any field that has a backing field, is not `private`, does not have `open`, `override` or `const` modifiers, and is not a delegated property; SHOULD be exposed as a @JVMField to simplify usage from Java.

```kotlin
public class User(val name: String) // BAD! Java: user.getName()

public class User(@JvmField val name: String) // Good
```

### `OrNull` fields
Any `foo: Foo` field exposed in a null-safe way (`fooOrNull: Foo?`) MUST mark the `orNull` field's **getter** as `@JvmSynthetic`, to ensure Java users only see the first field. (Both would behave the same on their end, so the second one would be pure noise)

```kotlin
public class User {
    val addressOrNull: Address? = null
    val address: Address = addressOrNull!!
} // BAD! Java users would see two names for the same value
public class User {
    val addressOrNull: Address? = null
        @JvmSynthetic get
    val address: Address = addressOrNull!!
} // Good
```

## Related extensions in separated files
Any related extensions defined in several files MUST be marked as `@JvmMultifileClass` and given a common `@JvmName`.

```kotlin
// StuffConnectFoo.kt
@file:JvmName("StuffFooUtils")
fun Stuff.connectFoo(foo: Foo) {}
// StuffConnectBar.kt
@file:JvmName("StuffBarUtils")
fun Stuff.connectBar(bar: Bar) {} // Bad! Names are more complex than needed

// StuffConnectFoo.kt
@file:JvmName("StuffUtils")
@file:JvmMultifileClass
fun Stuff.connectFoo(foo: Foo) {}
// StuffConnectBar.kt
@file:JvmName("StuffUtils")
@file:JvmMultifileClass
fun Stuff.connectBar(bar: Bar) {} // Good
```

## Typealiases
Any `typealias` will only be visible from Kotlin, forcing our Java users to use the non-aliased work. Typealiases SHOULD be replaced by subclasses or subinterfaces to provide them a good DX.

```kotlin
public typealias FilterCurrentViewModel = MapViewModel<FilterAndID, Filter> // BAD! Java users would need to use MapViewModel<...> explicitly
public class FilterCurrentViewModel(items: Map<FilterAndID, Filter> = mapOf()) : MapViewModel<FilterAndID, Filter>(items) // Good

public typealias FacetListView = SelectableListView<Facet> // Bad! same problem
public interface FacetListView : SelectableListView<Facet> // Good
```

# Remaining tasks for a great Java DX

- Apply each guideline in the Client
- Apply each guideline in `androidMain`
- Solve FIXMEs in the DX tests: these highlight an API issue, sometimes only in the IDE (e.g. `Index index = searcherSingleIndex.index;`)
- Solve TODOs in the DX tests (applying the same pattern to other widgets)
- Try to leverage [@JvmDefault](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-default/index.html) to expose default implementations on interfaces
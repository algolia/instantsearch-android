---
name: create-widget
description: Create a new InstantSearch widget following the View, ViewModel, Connector, and Compose State pattern. Use when the user wants to add a new search widget, filter widget, UI component, connector, or compose state to the InstantSearch library.
---

# Create Widget

InstantSearch widgets follow a layered architecture with four components. Each layer lives in a specific module.

## Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│  instantsearch-compose   →  *State (Compose UI)     │
├─────────────────────────────────────────────────────┤
│  instantsearch           →  *Connector (glue)       │
│                          →  *View (interface)        │
│                          →  *ViewModel (logic)       │
│                          →  Android ViewHolder       │
├─────────────────────────────────────────────────────┤
│  instantsearch-core      →  Base interfaces          │
└─────────────────────────────────────────────────────┘
```

## Step 1: Define the View interface

**Location:** `instantsearch/src/commonMain/kotlin/com/algolia/instantsearch/<feature>/`

The View is a platform-agnostic interface describing what the UI can display and what events it produces.

```kotlin
package com.algolia.instantsearch.<feature>

public interface <Feature>View {
    public fun setItems(items: List<ItemType>)
    public var onSelectionChanged: Callback<ItemType>?
}
```

If it maps to an existing core abstraction, use a typealias:

```kotlin
public typealias <Feature>View = SelectableListView<ItemType>
```

## Step 2: Create the ViewModel

**Location:** `instantsearch/src/commonMain/kotlin/com/algolia/instantsearch/<feature>/`

The ViewModel holds state and business logic, independent of any UI framework.

```kotlin
package com.algolia.instantsearch.<feature>

public class <Feature>ViewModel(
    items: List<ItemType> = emptyList(),
) {
    public val items: SubscriptionValue<List<ItemType>> = SubscriptionValue(items)

    // Telemetry
    init {
        trace<Feature>()
    }
}
```

Use `SubscriptionValue` for observable state. Keep the ViewModel free of Android imports.

## Step 3: Create connect extensions

**Location:** `instantsearch/src/commonMain/kotlin/com/algolia/instantsearch/<feature>/`

Create extension functions to connect the ViewModel to a Searcher and/or FilterState. Each returns a `Connection`.

```kotlin
package com.algolia.instantsearch.<feature>

internal fun <Feature>ViewModel.connectSearcher(
    searcher: SearcherForHits<*>,
): Connection {
    return object : AbstractConnection() {
        override fun connect() {
            super.connect()
            // Wire ViewModel to Searcher responses
        }

        override fun disconnect() {
            super.disconnect()
            // Unsubscribe
        }
    }
}

internal fun <Feature>ViewModel.connectView(
    view: <Feature>View,
): Connection {
    return object : AbstractConnection() {
        override fun connect() {
            super.connect()
            // Wire ViewModel state to View callbacks
        }

        override fun disconnect() {
            super.disconnect()
            // Unsubscribe
        }
    }
}
```

## Step 4: Create the Connector

**Location:** `instantsearch/src/commonMain/kotlin/com/algolia/instantsearch/<feature>/`

The Connector composes multiple connections and provides a single `connect()`/`disconnect()` entry point.

```kotlin
package com.algolia.instantsearch.<feature>

/**
 * <Brief description of the widget>.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/<feature>/android/)
 */
public data class <Feature>Connector(
    public val viewModel: <Feature>ViewModel,
    public val searcher: SearcherForHits<*>,
    // other params...
) : AbstractConnection() {

    init {
        trace<Feature>Connector()
    }

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    public constructor(
        searcher: SearcherForHits<*>,
        // convenience constructor params...
    ) : this(
        viewModel = <Feature>ViewModel(/* ... */),
        searcher = searcher,
    )

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
```

Key patterns:
- Use `data class` when possible
- Internal `private constructor` with a `Wrapper` sealed class if multiple searcher types are supported (see `FacetListConnector` for reference)
- Call `super.connect()` / `super.disconnect()` first
- Compose inner connections

## Step 5: Create Android ViewHolder (optional, Views API)

**Location:** `instantsearch/src/androidMain/kotlin/com/algolia/instantsearch/android/<feature>/`

```kotlin
package com.algolia.instantsearch.android.<feature>

public abstract class <Feature>ViewHolder(
    public val view: View,
) : RecyclerView.ViewHolder(view) {

    public interface Factory {
        public fun createViewHolder(parent: ViewGroup): <Feature>ViewHolder
    }
}
```

## Step 6: Create Compose State

**Location:** `instantsearch-compose/src/main/java/com/algolia/instantsearch/compose/<feature>/`

### Public interface

```kotlin
package com.algolia.instantsearch.compose.<feature>

/**
 * [<Feature>View] for compose.
 */
public interface <Feature>State : <Feature>View {
    public val items: List<ItemType>
}

public fun <Feature>State(
    items: List<ItemType> = emptyList(),
): <Feature>State {
    return <Feature>StateImpl(items)
}
```

### Internal implementation

**Location:** `instantsearch-compose/src/main/java/com/algolia/instantsearch/compose/<feature>/internal/`

```kotlin
package com.algolia.instantsearch.compose.<feature>.internal

import androidx.compose.runtime.*

internal class <Feature>StateImpl(
    items: List<ItemType>,
) : <Feature>State {

    override var items: List<ItemType> by mutableStateOf(items)
        private set

    override fun setItems(items: List<ItemType>) {
        this.items = items
    }

    // other View interface methods...

    init {
        trace()
    }
}
```

Key patterns:
- Public interface + factory function (not class) as public API
- Implementation is `internal` in a nested `internal` package
- Use `mutableStateOf` for Compose-observable state
- Delegate to core state classes when possible (e.g. `by SelectableListState(items)`)

## Step 7: Write tests

### ViewModel / Connector tests

**Location:** `instantsearch/src/commonTest/` or `instantsearch/src/jvmTest/`

```kotlin
class Test<Feature>ViewModel {
    @Test
    fun testInitialState() { /* ... */ }
}

class Test<Feature>ConnectSearcher {
    @Test
    fun testConnectionWiring() { /* ... */ }
}
```

### Compose State tests

**Location:** `instantsearch-compose/src/test/`

```kotlin
class <Feature>StateTest {
    @Test
    fun testStateUpdates() { /* ... */ }
}
```

## File naming conventions

| Component | File name | Location module |
|-----------|-----------|-----------------|
| View | `<Feature>View.kt` | `instantsearch` commonMain |
| ViewModel | `<Feature>ViewModel.kt` | `instantsearch` commonMain |
| Connector | `<Feature>Connector.kt` | `instantsearch` commonMain |
| Connect extensions | `<Feature>Connect*.kt` | `instantsearch` commonMain |
| ViewHolder | `<Feature>ViewHolder.kt` | `instantsearch` androidMain |
| Compose State | `<Feature>State.kt` | `instantsearch-compose` |
| Compose Impl | `<Feature>StateImpl.kt` | `instantsearch-compose` internal/ |
| Tests | `Test<Feature>*.kt` | commonTest/jvmTest |

## Checklist

- [ ] View interface defined (or typealias to core type)
- [ ] ViewModel created with `SubscriptionValue` for state
- [ ] Connect extensions wire ViewModel to Searcher/FilterState
- [ ] Connector composes all connections
- [ ] Connector calls `super.connect()` / `super.disconnect()`
- [ ] Compose State public interface + factory function
- [ ] Compose StateImpl is `internal` with `mutableStateOf`
- [ ] Telemetry `trace*()` calls added
- [ ] KDoc on all public API
- [ ] Tests cover ViewModel logic and connection wiring
- [ ] All public API has explicit visibility modifiers

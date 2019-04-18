package selection

import kotlin.properties.Delegates


public abstract class SelectionListPresenter<T>(limit: Int = 10) {

    protected abstract val comparator: Comparator<SelectableItem<T>>

    public var values: List<SelectableItem<T>> = listOf()
        set(value) {
            field = computeValues(value)
            onValuesChange.forEach { it(field) }
        }

    public val onValuesChange: MutableList<(List<SelectableItem<T>>) -> Unit> = mutableListOf()

    public var limit by Delegates.observable(limit) { _, _, _ ->
        values = computeValues(values)
    }

    protected fun computeValues(value: List<SelectableItem<T>>): List<SelectableItem<T>> {
        return value.sortedWith(comparator).take(limit)
    }
}
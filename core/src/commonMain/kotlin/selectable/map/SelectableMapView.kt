package selectable.map


public interface SelectableMapView<K, V> {

    var onClick: ((K) -> Unit)?

    fun setSelected(selected: K?)

    fun setItems(items: Map<K, V>)
}
package filter

import com.algolia.search.model.Attribute


public sealed class FilterGroupID {

    public abstract val name: String

    public data class And(override val name: String) : FilterGroupID() {
        constructor(attribute: Attribute): this(attribute.raw)
    }

    public data class Or(override val name: String) : FilterGroupID() {
        constructor(attribute: Attribute): this(attribute.raw)
    }
}
package filter


public sealed class FilterGroupID {

    public abstract val name: String

    public data class And(override val name: String) : FilterGroupID()

    public data class Or(override val name: String) : FilterGroupID()
}
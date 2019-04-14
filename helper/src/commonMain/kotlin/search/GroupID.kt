package search


public sealed class GroupID {

    public abstract val name: String

    public data class And(override val name: String) : GroupID()

    public data class Or(override val name: String) : GroupID()
}
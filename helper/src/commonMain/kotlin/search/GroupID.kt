package search


sealed class GroupID {

    abstract val name: String

    data class And(override val name: String) : GroupID()

    data class Or(override val name: String) : GroupID()
}